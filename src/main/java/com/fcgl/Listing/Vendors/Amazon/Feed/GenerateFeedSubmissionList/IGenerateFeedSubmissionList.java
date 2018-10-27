package com.fcgl.Listing.Vendors.Amazon.Feed.GenerateFeedSubmissionList;

import com.fcgl.Listing.Response.Response;
import java.util.List;

public interface IGenerateFeedSubmissionList {

  Response generateFeedSubmissionList(List<String> submissions);
}
