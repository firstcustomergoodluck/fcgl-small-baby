package com.fcgl.Listing.Vendors.Amazon.FeedStatus;

public class FeedStatusAMZ {
  //    /**
//     * Returns a list of feed submissions. Used to determined status of feed submission
//     *
//     * The GetFeedSubmissionList request can return a maximum of 100 results. If there are additional results to return,
//     * HasNext is returned in the response with a true value. To retrieve all the results, you can pass the value of the
//     * NextToken parameter to the GetFeedSubmissionListByNextToken operation and repeat until HasNext is false.
//     *
//     * Throttling: Request Quota: 10 requests, Restore Rate: 1 every 45 seconds, Hourly quota: 80 requests
//     * Amazon Docs: http://docs.developer.amazonservices.com/en_US/feeds/Feeds_GetFeedSubmissionList.html
//     * @param feedSubmissionId
//     */
//    //TODO: What to do when if it's not done? Should I add it back to the queue??? Should I wait until I get that it is done??? If that's the case then I need an async metodology...
//
//    private boolean getFeedSubmissionList(String feedSubmissionId, MarketplaceWebService service) {//TODO: feedSubmissionId can be a list.. I wonder why that is... I guess maybe some submissions take a while?
//        GetFeedSubmissionListRequest request = new GetFeedSubmissionListRequest();
//        request.setMerchant( merchantId );
//        request.setMWSAuthToken(MWSAuthToken);
//        request.setFeedSubmissionIdList(new IdList(Arrays.asList(feedSubmissionId)));
//
//        // @TODO: set request parameters here
//
//        invokeGetFeedSubmissionList(service, request);
//
//        return true;
//    }
//
//    /**
//     * API: Return the feed processing report and Content-MD5 header
//     */
//    private boolean getFeedSubmissionResult() {
//        return true;
//    }
}
