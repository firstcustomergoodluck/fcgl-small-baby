package com.fcgl.Listing.Vendors.Amazon.Feed.GenerateFeedSubmissionList;

import com.amazonaws.mws.MarketplaceWebService;
import com.amazonaws.mws.MarketplaceWebServiceException;
import com.amazonaws.mws.model.FeedSubmissionInfo;
import com.amazonaws.mws.model.GetFeedSubmissionListRequest;
import com.amazonaws.mws.model.GetFeedSubmissionListResponse;
import com.amazonaws.mws.model.GetFeedSubmissionListResult;
import com.amazonaws.mws.model.ResponseMetadata;
import com.fcgl.Listing.Response.Response;
import com.fcgl.Listing.Vendors.Amazon.AbstractAMZService;
import com.fcgl.MessageQueue.IMessageQueueSender;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * Part TWO of AMAZON submit listing workflow.
 * Checks if submissions have passed the PROCESSING state
 */
public class GenerateFeedSubmissionList  extends AbstractAMZService {
  private final String requestId;
  private IMessageQueueSender messageQueueSender;
  private final int[] retryWaitTime = {1000, 4000, 16000, 30000};//This was recommended by Amazon //TODO: Should put this somewhere else
  private static final Logger logger = LogManager.getLogger("GenerateFeedSubmissionList");
  private String PROCESSING_COMPLETE = "ProcessingComplete";
  private String FEED_CANCELED = "FeedCanceled";
  private String SAFETY_NET = "SafetyNet";
  private String FEED_PROCESSING = "FeedProcessing";


  public GenerateFeedSubmissionList(String requestId, IMessageQueueSender messageQueueSender) {
    this.requestId = requestId;
    this.messageQueueSender = messageQueueSender;
  }

  /**
   * Calls the invokeGetFeedSubmissionList and returns a Response based on the List returned by that
   * method. Also handles the Exception thrown by invokeGetFeedSubmissionList
   * @return Response
   */
  public Response generateFeedSubmissionList(List<String> submissions) {
    MarketplaceWebService service = instantiateService();
    GetFeedSubmissionListRequest request = new GetFeedSubmissionListRequest();
    request.setMerchant(getAmazonConfig().getMWSAuthToken());
    Response response = null;
    boolean success = false;
    int retry = 3;//TODO: I've been doing retry logic in a lot of places and the retry has always been 3... Maybe I should put that in a config somewhere?
    while (!success && retry != 0) {
      try {
        List<String> feedStatus = invokeGetFeedSubmissionList(service, request);
        String message = String.format("RequestID: %s, Feed Submissions Processed: %d,"
            + " Status Code: %d", requestId, feedStatus.size(), 200);
        handleFeedProcessingStatus(feedStatus, submissions);
        response = new Response(200, requestId, message);
        success = true;
      } catch (MarketplaceWebServiceException e) {
        e.getResponseHeaderMetadata();
        response = handleErrorResponse(e, "GenerateFeedSubmissionList", requestId);
        retry--;
      }
    }

    return response;
  }

  /**
   * TODO: THis method could probably be private. It wouldn't be hard to mock the incomming data...
   * //TODO: I would like all of these amazon API calls to be the same, but they are different for right now
   * //TODO: I will come up with a better solution once finishing other tasks.
   * Calls the getFeedSubmissionList Amazon API. It returns the status for feed submissions.
   * Calls the handleFeedProcessingStatus() method which adds the feedSubmissionIds to a specified
   * Queue for more processing.
   * @param service instance of MarketplaceWebService service
   * @param request a GetFeedSubmissionListRequest
   * @return Response Object
   */
  private List<String> invokeGetFeedSubmissionList(MarketplaceWebService service, GetFeedSubmissionListRequest request)
      throws MarketplaceWebServiceException {
    List<String> feedStatus = new ArrayList<>();
    StringBuilder builder = new StringBuilder();
      GetFeedSubmissionListResponse response = service.getFeedSubmissionList(request);
      builder.append("GetFeedSubmissionList Action Response\n");
      builder.append("=============================================================================\n\n");
      builder.append("    GetFeedSubmissionListResponse\n");
      if (response.isSetGetFeedSubmissionListResult()) {
        builder.append("        GetFeedSubmissionListResult\n");
        GetFeedSubmissionListResult getFeedSubmissionListResult = response.getGetFeedSubmissionListResult();
        if (getFeedSubmissionListResult.isSetNextToken()) {
          builder.append("            NextToken\n                ");
          builder.append(getFeedSubmissionListResult.getNextToken());
          builder.append("\n");
        }

        if (getFeedSubmissionListResult.isSetHasNext()) {
          builder.append("            HasNext\n                ");
          builder.append(getFeedSubmissionListResult.isHasNext());
          builder.append("\n");
        }

        List<FeedSubmissionInfo> feedSubmissionInfoList = getFeedSubmissionListResult.getFeedSubmissionInfoList();
        Iterator i$ = feedSubmissionInfoList.iterator();
        while(i$.hasNext()) {
          FeedSubmissionInfo feedSubmissionInfo = (FeedSubmissionInfo)i$.next();
          builder.append("            FeedSubmissionInfo\n");
          if (feedSubmissionInfo.isSetFeedSubmissionId()) {
            builder.append("                FeedSubmissionId\n                    ");
            builder.append(feedSubmissionInfo.getFeedSubmissionId());
            builder.append("\n");
          }

          if (feedSubmissionInfo.isSetFeedType()) {
            builder.append("                FeedType\n                    ");
            builder.append(feedSubmissionInfo.getFeedType());
            builder.append("\n");
            
          }

          if (feedSubmissionInfo.isSetSubmittedDate()) {
            builder.append("                SubmittedDate\n                    ");
            builder.append(feedSubmissionInfo.getSubmittedDate());
            builder.append("\n");
          }

          if (feedSubmissionInfo.isSetFeedProcessingStatus()) {
            String feedProcessingStatus = feedSubmissionInfo.getFeedProcessingStatus();
            builder.append("                FeedProcessingStatus\n                    ");
            builder.append(feedProcessingStatus);
            builder.append("\n");
            feedStatus.add(feedProcessingStatus);
          }

          if (feedSubmissionInfo.isSetStartedProcessingDate()) {
            builder.append("                StartedProcessingDate\n                    ");
            builder.append(feedSubmissionInfo.getStartedProcessingDate());
            builder.append("\n");
          }

          if (feedSubmissionInfo.isSetCompletedProcessingDate()) {
            builder.append("                CompletedProcessingDate\n                    ");
            builder.append(feedSubmissionInfo.getCompletedProcessingDate());
            builder.append("\n");
          }
        }
      }

      String amzRequestId = "";
      if (response.isSetResponseMetadata()) {
        builder.append("        ResponseMetadata\n");
        ResponseMetadata responseMetadata = response.getResponseMetadata();
        if (responseMetadata.isSetRequestId()) {
          amzRequestId = responseMetadata.getRequestId();
          builder.append("            RequestId\n                ");
          builder.append(amzRequestId);
          builder.append("\n");
        }
      }

      builder.append("\n");
      builder.append(response.getResponseHeaderMetadata());
      builder.append("\n\n");
      logger.info(builder.toString());
      return feedStatus;
  }

  /**
   * Adds a message to a queue based on the feed processing status that Amazon returns.
   * _DONE_:          Should be sent to the PROCESSING_COMPLETE queue to continue the Amazon workflow
   * _CANCELLED_:     Should be sent to the FEED_CANCELED queue to investigate why there was a fatal error.
   * _IN_SAFETY_NET_: Should be sent to a SAFETY_NET queue to investigate
   * Default:         Should be requeued because the feedSubmission is still processing
   * @param statuses: Statuses returned by Amazon
   */
  private void handleFeedProcessingStatus(List<String> statuses, List<String> submissions) {
    for (int i = 0; i < statuses.size(); i++) {
      String status = statuses.get(i);
      String feedSubmissionid = submissions.get(i);
      switch(status) {
        case "_DONE_":
          messageQueueSender.sendMessage(feedSubmissionid, PROCESSING_COMPLETE);
          break;
        case "_CANCELLED_":
          messageQueueSender.sendMessage(feedSubmissionid, FEED_CANCELED);
          break;
        case "_IN_SAFETY_NET_":
          messageQueueSender.sendMessage(feedSubmissionid, SAFETY_NET);
          break;
        default:
          messageQueueSender.sendMessage(feedSubmissionid, FEED_PROCESSING);
      }
    }
  }

}
