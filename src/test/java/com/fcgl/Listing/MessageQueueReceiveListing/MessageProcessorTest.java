package com.fcgl.Listing.MessageQueueReceiveListing;

import com.fcgl.Listing.Vendors.Vendor;
import com.fcgl.Listing.Vendors.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


class MessageProcessorTest {
    private List<String> messages;
    private String[] parameters = {"vendorId", "sku", "quantity", "title", "description", "currency", "barcode", "barcodeType", "price", "latency"};
    private char[] parameterType = {'i', 's', 'i', 's', 's', 's', 's', 's', 'd', 'i'};
    @BeforeEach
    void setUp() {
        messages = new ArrayList<>();
    }


    @Test
    void messageToProductInformationInsertTest() {
        String[] values = {"1", "1", "1", "FCGL", "Startup for dummies", Currency.USD.getName(), "1234567", ProductIdentifierType.ISBN.getName(), "12.50", "1"};
        String message1 = generateJsonFormattedMessage(values, parameters);
        messages.add(message1);
        MessageProcessor messageProcessor = new MessageProcessor(messages);
        MessageProcessor.MessageToProductInformationResponse response = messageProcessor.messageToProductInformation(message1);
        assertEquals(MessageProcessor.State.INSERT, response.getState());
        assertEquals(Vendor.AMAZON, response.getVendor());
        assertEquals("1_1", response.getVendorSKUId());
        IProductInformation productInformation = response.getProductInformation();
        Inventory inventory = (Inventory) productInformation.getInventory();
        Price price = (Price) productInformation.getPrice();
        String sku = productInformation.getSKU();
        assertEquals(inventory.getQuantity(), 1);
        assertEquals(inventory.getFulfillLatency(), 1);
        assertEquals(price.getPrice(), 12.50);
        assertEquals(price.getCurrency(), Currency.USD);
        assertEquals(sku, values[1]);
        Product product = (Product) productInformation.getProduct();
        ProductDescriptionData productDescriptionData = product.getProductDescriptionData();
        ProductIdentifier productIdentifier = product.getProductIdentifier();
        assertEquals(productDescriptionData.getDescription(), values[4]);
        assertEquals(productDescriptionData.getTitle(), values[3]);
        assertEquals(productIdentifier.getProductIdentifierType(), ProductIdentifierType.ISBN);
    }

    @Test
    void messageToProductInformationMissingParamsTest() {
        String[] values = {"1", "1", "1", "FCGL", "Startup for dummies", Currency.USD.getName(), "1234567", ProductIdentifierType.ISBN.getName()};
        String message1 = generateJsonFormattedMessage(values, parameters);
        messages.add(message1);
        MessageProcessor messageProcessor = new MessageProcessor(messages);
        MessageProcessor.MessageToProductInformationResponse response = messageProcessor.messageToProductInformation(message1);
        assertEquals(MessageProcessor.State.ERROR, response.getState());
    }

    @Test
    void messageToProductInformationUpdate() {
        String[] values = {"1", "1", "1", "FCGL", "Startup for dummies", Currency.USD.getName(), "1234567", ProductIdentifierType.ISBN.getName(), "12.50", "1"};
        String message1 = generateJsonFormattedMessage(values, parameters);
        messages.add(message1);
        MessageProcessor messageProcessor = new MessageProcessor(messages);
        MessageProcessor.MessageToProductInformationResponse response = messageProcessor.messageToProductInformation(message1);
        String vendorSKUId = response.getVendorSKUId();
        Vendor vendor = response.getVendor();
        ArrayList<IProductInformation> productInformationList = new ArrayList<>();
        productInformationList.add(response.getProductInformation());
        messageProcessor.getVendorProductInformation().put(vendor, productInformationList);
        messageProcessor.getVendorSKUIndexLocation().put(vendorSKUId, 0);
        MessageProcessor.MessageToProductInformationResponse response2 = messageProcessor.messageToProductInformation(message1);
        assertEquals(response2.getState(), MessageProcessor.State.UPDATE);
        assertEquals(response2.getQuantity(), new Integer(1));
    }

    @Test
    void processMessagesInsertTest() {
        String[] values = {"1", "1", "1", "FCGL", "Startup for dummies", Currency.USD.getName(), "1234567", ProductIdentifierType.ISBN.getName(), "12.50", "1"};
        String message1 = generateJsonFormattedMessage(values, parameters);
        messages.add(message1);
        MessageProcessor messageProcessor = new MessageProcessor(messages);
        MessageProcessorResponse response = messageProcessor.processMessages();
        assertEquals(0, response.getBadRequests().size());
        assertEquals(1, response.getVendorProductInformation().get(Vendor.AMAZON).size());
    }

    @Test
    void processMessagesUpdateTest() {
        String[] values = {"1", "1", "1", "FCGL", "Startup for dummies", Currency.USD.getName(), "1234567", ProductIdentifierType.ISBN.getName(), "12.50", "1"};
        String message1 = generateJsonFormattedMessage(values, parameters);
        messages.add(message1);
        messages.add(message1);
        MessageProcessor messageProcessor = new MessageProcessor(messages);
        MessageProcessorResponse response = messageProcessor.processMessages();
        assertEquals(0, response.getBadRequests().size());
        assertEquals(1, response.getVendorProductInformation().get(Vendor.AMAZON).size());
    }

    @Test
    void processMessageMultipleInsertTest() {
        String[] values1 = {"1", "1", "1", "FCGL", "Startup for dummies", Currency.USD.getName(), "1234567", ProductIdentifierType.ISBN.getName(), "12.50", "1"};
        String[] values2 = {"1", "2", "2", "FCGL2", "Startup for dummies2", Currency.USD.getName(), "12345672", ProductIdentifierType.ISBN.getName(), "12.52", "2"};
        String message1 = generateJsonFormattedMessage(values1, parameters);
        String message2 = generateJsonFormattedMessage(values2, parameters);
        messages.add(message1);
        messages.add(message2);
        MessageProcessor messageProcessor = new MessageProcessor(messages);
        MessageProcessorResponse response = messageProcessor.processMessages();
        assertEquals(0, response.getBadRequests().size());
        assertEquals(2, response.getVendorProductInformation().get(Vendor.AMAZON).size());
    }

    @Test
    void processMessageBadVendor() {
        String[] values1 = {"1", "1", "1", "FCGL", "Startup for dummies", Currency.USD.getName(), "1234567", ProductIdentifierType.ISBN.getName(), "12.50", "1"};
        String[] values2 = {"2", "1", "1", "FCGL", "Startup for dummies", Currency.USD.getName(), "1234567", ProductIdentifierType.ISBN.getName(), "12.50", "1"};
        String message1 = generateJsonFormattedMessage(values1, parameters);
        String message2 = generateJsonFormattedMessage(values2, parameters);
        messages.add(message1);
        messages.add(message2);
        MessageProcessor messageProcessor = new MessageProcessor(messages);
        MessageProcessorResponse response = messageProcessor.processMessages();
        assertEquals(1, response.getBadRequests().size());
        assertEquals(1, response.getVendorProductInformation().get(Vendor.AMAZON).size());
    }

    private String generateJsonFormattedMessage(String[] message, String[] parameters) {
        if (message.length <= parameters.length) {
            StringBuilder builder = new StringBuilder();
            builder.append("{");
            for (int i = 0; i < message.length; i++) {
                builder.append("\"");
                builder.append(parameters[i]);
                builder.append("\"");
                builder.append(":");
                switch (parameterType[i]) {
                    case 's':
                        builder.append("\"");
                        builder.append(message[i]);
                        builder.append("\"");
                        break;
                    case 'd':
                        builder.append(Double.parseDouble(message[i]));
                        break;
                    case 'i':
                        builder.append(Integer.parseInt(message[i]));
                        break;
                }

                if (i != message.length - 1) {
                    builder.append(",");
                }
            }
            builder.append("}");

            return builder.toString();
        } else {
            return null;
        }
    }
}
