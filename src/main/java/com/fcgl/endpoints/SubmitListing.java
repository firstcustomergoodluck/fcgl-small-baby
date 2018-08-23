package com.fcgl.endpoints;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SubmitListing {
    
    //TODO: This should probably call a factory to generate a listing in a particular Vendor
    //TODO: Should we submit a feed in multiple places?
    //TODO: Should we submit different feeds on different sites?
    //TODO: Should there be different message queues for each vendor???

    /**
     * Same Product on multiple sites:
     * Pros: More viewership, could sell faster, might generate better profits, easier to design
     * Cons: Processing power increases by 2, We might sell something on multiple websites and then have to cancel. Would look bad for company image.
     * Notes: Maybe only post on multiple sites if we "enough" of the same product
     */

    /**
     * Specific site for specific product:
     * Pros: Less processing power, Would be able to keep a quantity on sites instead of an availability. So It'll de-list when we have 0 items.
     * Cons: Less viewership, sell slower, worse profit (Basically anti the pros of first case)
     */

    /**
     * A Variation of both... Put the product on the sites that make the most sense. Example: If it makes sense to put the product on 2 out of the 5 sites, then do so
     * Have pros and costs of both mentioned above.
     */


    //TODO: Need to decide on a design so we can work off that.
    //TODO: Question: Can we submit a listing to different vendors at the same time by using a different thread for both? Or should we do it synchronously?
    @PostMapping("/v1/submitFeed")
    public ResponseEntity postListing() {
        return ResponseEntity.status(200).body("OK");
    }

    @GetMapping("/v1/FeedStatus")
    public ResponseEntity listingStatus() {
        //TODO: This should probably call a factory to generate a listing in a particular Vendor
        return ResponseEntity.status(200).body("OK");
    }
}
