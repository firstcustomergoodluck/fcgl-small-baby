package com.fcgl.Listing.Vendors.Amazon.XML_Files;

//TODO: We can either make this very dynamic or fit the general layout of an AMAZON xml file.
//TODO: I like making it fit Amazon's style better because I don't forsee us using XML besides Amazon... However
//TODO: I haven't looked to see if we need xml for other Amazon services that we might need to use.

import com.fcgl.Listing.Vendors.ProductInformation;

import java.util.ArrayList;

public interface IXMLGenerator {

    //Note: The general structure for a product XML file can be seen under the 'feeds' package.
    //The only thing I'm not sure about is if MSRP is the price we are giving it or if that's supposed to go elsewhere.
    // Also There probably needs to be some Shipment information in there.

    /**
     * Generates the XML for listing products in Amazon.
     * All XML files generated should go in the 'feeds' package. The format for a file name is:
     * (TBD: I was thinking data with epoch time, or UTC time or a mixture of date with the requestId)
     * @param productInformation List of List of ProductInformation objects which hold the data needed for the XML.
     * @return The location of the XML file generated
     */
    String generateProductXML(ArrayList<ArrayList<ProductInformation>> productInformation);
}
