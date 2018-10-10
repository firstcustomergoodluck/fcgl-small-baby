package com.fcgl.endpoints;

import com.fcgl.Listing.MessageQueueReceiveListing.ReceiveListingMessages;
import com.fcgl.Listing.Response.IResponse;
import com.fcgl.endpoints.RequestModels.SubmitFeedRequestModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ManageListing {


  @PostMapping("/v1/submitFeed")
  public ResponseEntity postListing(@RequestBody SubmitFeedRequestModel submitFeedRequestModel) {
    String requestId = submitFeedRequestModel.getRequestId();
    ReceiveListingMessages receiveListingMessages = new ReceiveListingMessages(requestId);
    IResponse response = receiveListingMessages.processMessages();
    return ResponseEntity.status(response.getStatusCode()).body(response.getMessage());

  }

  @GetMapping("/v1/FeedStatus")
  public ResponseEntity listingStatus() {
    //TODO: This should probably call a factory to generate a listing in a particular Vendor
    return ResponseEntity.status(200).body("OK");
  }
}
