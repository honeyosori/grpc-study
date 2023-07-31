package ecommerce;

import com.google.protobuf.StringValue;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.Iterator;
import java.util.logging.Logger;

public class OrderClient {

    private static final Logger logger = Logger.getLogger(OrderClient.class.getName());

    public static void main(String[] args) {
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress("localhost", 50051)
                .usePlaintext()
                .build();

        OrderManagementGrpc.OrderManagementBlockingStub stub = OrderManagementGrpc.newBlockingStub(channel);

        OrderManagementOuterClass.Order order = OrderManagementOuterClass.Order
                .newBuilder()
                .setId("101")
                .addItems("book1")
                .addItems("book2")
                .setDestination("Seoul, South Korea")
                .setPrice(40000)
                .build();

        // Add order
        StringValue result = stub.addOrder(order);
        logger.info("AddOrder Response -> : " + result.getValue());

        // Get order
        OrderManagementOuterClass.Order orderResponse = stub.getOrder(result);
        logger.info("GetOrder Response -> : " + orderResponse.toString());

        // Search orders
        StringValue searchWord = StringValue.newBuilder().setValue("Google").build();
        Iterator<OrderManagementOuterClass.Order> matchingOrdersItr;

        logger.info("Search orders which contains item : " + searchWord);
        matchingOrdersItr = stub.searchOrders(searchWord);

        while(matchingOrdersItr.hasNext()) {
            OrderManagementOuterClass.Order matchingOrder = matchingOrdersItr.next();
            logger.info("Search Order Response -> Matching Order - " + matchingOrder.getId());
            logger.info(" Order : " + order.getId() + "\n " + matchingOrder.toString());
        }

        // Update orders

        channel.shutdown();
    }
}
