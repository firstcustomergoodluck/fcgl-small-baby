# Listing API

Amazon Docs: http://docs.developer.amazonservices.com/en_US/dev_guide/DG_RequiredRequestParameters.html

Amazon Developer Docs: http://docs.developer.amazonservices.com/en_US/dev_guide/index.html

### About Amazon MarketPlace Services API

 
#####Required Request Parameters:
```
{
    AWSAccessKeyId: ...,(
    Action: ...,
    MWSAuthToken: ...,
    SellerId: ...,
    Signature: ...,
    SignatureMethod: ...,
    SignatureVersion: ...,
    Timestamp: ...,
    Version ...,
}
```

* ```AWSAccessKeyId```: Your Amazon MWS account is identified by your access key Id, which Amazon MWS uses to look up your Secret Access Key.
* ```Action```: The action you want to perform on the endpoint
* ```MWSAuthToken```: Represents the authorization of a specific developer by a specific Amazon seller.
* ```SellerId```: Your seller or merchant identifier.
* ```Signature```: Part of the authentication process that is used for identifying and verifying who is sending a request.
* ```SignatureMethod```: The HMAC hash algorithm you are using to calculate your signature. Both HmacSHA256 and HmacSHA1 are supported hash algorithms, but Amazon recommends using HmacSHA256.
* ```SignatureVersion```: Which signature version is being used. This is Amazon MWS-specific information that tells Amazon MWS the algorithm you used to form the string that is the basis of the signature.
* ```Timestamp```: Each request must contain the timestamp of the request. Depending on the API operation you are using, you can provide an expiration date and time for the request instead of the timestamp. In ISO 8601 date time format.
* ```Version```: The version of the API section being called.

##Submit Feed

 * Feed size is limited to 2GB. Good Practice to submit feeds with a size limit of 30,0000 records/items
    * ```Throttling```: Request Quota 15 Requests.
    * ```Restore Rate```: One request every two minutes.
    * ```Hourly request quota```: 30 request per hour


#####Request Parameters:

* ```FeedContent```: The actual content of the feed itself, in XML or flat file format.
Must include the FeedContent in the body of the HTTP request.
* ```FeedType```: A FeedType value indicating how the data should be processed.
* ```MarketplaceIdList```: List of marketplaceIds you want the feed to be applied to
* ```PurgeAndReplace``` Boolean value that enables the purge and replace functionality. Set to
true to purge and replace the existing data; otherwise false. Usage is throttles to allow only one purge and replace
within a 24-hour period
* ```ContentMD5Value```: An MD5 hash of the feed content. Amazon MWS uses this value to determine
if the feed data has been corrupted or tampered with during transit. This replaced the
Content-MD5 header. http://docs.developer.amazonservices.com/en_US/feeds/Feeds_MD5.html

```Feed Submission Process Overview```: http://docs.developer.amazonservices.com/en_US/feeds/Feeds_Overview.html

```FeedTypes```: http://docs.developer.amazonservices.com/en_US/feeds/Feeds_FeedType.html


####Example Requests:

#####Get Request:
```
https://mws.amazonservices.com/CustomerInformation/2014-03-01/
?AWSAccessKeyId=AKIAEXAMPLEFWR4TJ7ZQ
&Action=ListCustomers
&DateRangeEnd=2014-04-30T00%3A06%3A07.000Z
&DateRangeStart=2014-04-01T00%3A06%3A07.000Z
&DateRangeType=AssociatedDate
&MWSAuthToken=amzn.mws.4ea38b7b-f563-7709-4bae-87aeaEXAMPLE
&MarketplaceId=ATVPDKIKX0DER
&SellerId=A1IMEXAMPLEWRC
&Signature=FUbIEXAMPLETUGtTS6sqNDt3OuLH8tbhz5YEXAMPLEw%3D
&SignatureMethod=HmacSHA256
&SignatureVersion=2
&Timestamp=2014-04-01T21%3A53%3A02Z
&Version=2014-03-01
```

#####Post Request:
```
POST /Feeds/2009-01-01 HTTP/1.1
Content-Type: x-www-form-urlencoded
Host: mws.amazonservices.com
User-Agent: <Your User Agent Header>

?AWSAccessKeyId=0PB842ExampleN4ZTR2
&Action=SubmitFeed
&FeedType=_POST_PRODUCT_DATA_
&MWSAuthToken=amzn.mws.4ea38b7b-f563-7709-4bae-87aeaEXAMPLE
&MarketplaceIdList.Id.1=ATVExampleDER
&SellerId=A1XExample5E6
&ContentMD5Value=ExampleMd5HashOfHttpBodyAsPerRfc2616Example
&SignatureMethod=HmacSHA256
&SignatureVersion=2
&Timestamp=2009-01-26T23%3A51%3A31.315Z
&Version=2009-01-01
&Signature=SvSExamplefZpSignaturex2cs%3D
```

## GetFeedSubmissionList
* Returns a list og all feed submissions submitted in the previous 90 days

* ```Throttling```: Request Quota 10 Requests.
* ```Restore Rate```: One request every 45 seconds.
* ```Hourly request quota```: 80 request per hour

##### Request Parameters:

* ```FeedSubmissionIdList```: A structured list of no more than 100 FeedSubmmissionId values.
* ```MaxCount```: Integer that indicates the maximum number of feed submissions to return in the list.
* ```FeedTypeList```: A structured list of one or more FeedType values.
* ```FeedProcessingStatusList```: A structured list of one or more feed processing statuses.
* ```SubmittedFromDate```: The earliest submission date that you are looking for
* ```SubmittedToDate```: The latest submission date that you are looking for

#### Feed Processing Status
* ```_AWAITING_ASYNCHRONOUS_REPLY_```: The request is being processed, but is waiting for external
 information before it can complete.
* ```_CANCELLED_```: The request has been aborted due to a fatal error.
* ```_DONE_```The request has been processed. You can call the GetFeedSubmissionResult operation
to receive a processing report that describes which records in the feed were successful and which
records generated errors.
* ```_IN_PROGRESS_	```: The request is being processed.
* ```_IN_SAFETY_NET_```: The request is being processed, but the system has determined that
there is a potential error with the feed (for example, the request will remove all inventory
from a seller's account.) An Amazon seller support associate will contact the seller
to confirm whether the feed should be processed.
* ```_SUBMITTED_```: The request has been received, but has not yet started processing.
* ```_UNCONFIRMED_```: The request is pending.

##Endpoints & MarketplaceId

Endpoints: 
* North America (includes Canada, US, and Mexico): https://mws.amazonservices.com

Marketplaces:

* US: {MarketplaceId: ATVPDKIKX0DER, Country code: US}

More info can be found here: http://docs.developer.amazonservices.com/en_US/dev_guide/DG_Endpoints.html

## Tax Codes:
https://sellercentral.amazon.com/gp/tax-manager/popups/generalNA.html

* Books: ```A_BOOKS_GEN```

## XML FORMATING

* The product feed XML files should be formatted DATE_EPOCHTIME.xml
    * Example: 08_09_2018_153470948
* Product Feed Schema:  https://sellercentral.amazon.com/gp/help/help.html?ie=UTF8&itemID=200386810&
* Look at the .xsd files for more information


## Error Handling

http://docs.developer.amazonservices.com/en_US/dev_guide/DG_Errors.html

* Every error should be logged with a status code and descriptive message
* When MWS returns a status code >= 500 this indicates that our request was not incorrect and should therefore be resent
