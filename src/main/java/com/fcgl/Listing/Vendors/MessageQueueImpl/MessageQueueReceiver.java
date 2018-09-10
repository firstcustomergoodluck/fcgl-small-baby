package com.fcgl.Listing.Vendors.MessageQueueImpl;

import com.fcgl.Listing.Vendors.ProductInformation;
import com.fcgl.MessageQueue.IMessageQueueReceiver;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.util.StopWatch;

import java.util.List;

//NOTE: I think I can do this without the rabbit listener... But it's a little weird. I think the listener executes the job everytime....
@RabbitListener(queues = "hello")
public class MessageQueueReceiver {

    private final int instance;

    public MessageQueueReceiver(int i) {
        this.instance = i;
    }


//    @Override
//    public List<ProductInformation> getProductInformation(String message) {
//        return null;
//    }

    @RabbitHandler
    public void receive(String in) throws InterruptedException {
        StopWatch watch = new StopWatch();
        watch.start();
        System.out.println("instance " + this.instance +
                " [x] Received '" + in + "'");
        doWork(in);
        watch.stop();
        System.out.println("instance " + this.instance +
                " [x] Done in " + watch.getTotalTimeSeconds() + "s");
    }

    private void doWork(String in) throws InterruptedException {
        for (char ch : in.toCharArray()) {
            if (ch == '.') {
                Thread.sleep(1000);
            }
        }
    }

}
