package ecommerce;

import com.google.protobuf.StringValue;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.util.Iterator;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class OrderClient {

    private static final Logger logger = Logger.getLogger(OrderClient.class.getName());

    public static void main(String[] args) {
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress("localhost", 50051)
                .usePlaintext()
                .build();

        OrderManagementGrpc.OrderManagementBlockingStub stub = OrderManagementGrpc.newBlockingStub(channel);

        // Add & get order
        addAndGetOrder(stub);

        // Search orders
        searchOrders(stub);

        OrderManagementGrpc.OrderManagementStub asyncStub = OrderManagementGrpc.newStub(channel);

        // Update orders
        updateOrders(asyncStub);

        // Process Order


        channel.shutdown();
    }

    private static void addAndGetOrder(OrderManagementGrpc.OrderManagementBlockingStub stub) {
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
    }

    private static void searchOrders(OrderManagementGrpc.OrderManagementBlockingStub stub) {
        StringValue searchWord = StringValue.newBuilder().setValue("Google").build();
        Iterator<OrderManagementOuterClass.Order> matchingOrdersItr;

        matchingOrdersItr = stub.searchOrders(searchWord);
        while(matchingOrdersItr.hasNext()) {
            OrderManagementOuterClass.Order matchingOrder = matchingOrdersItr.next();
            logger.info("Search Order Response -> Matching Order - " + matchingOrder.getId());
            logger.info(" Order : \n " + matchingOrder.toString());
        }
    }

    private static void updateOrders(OrderManagementGrpc.OrderManagementStub stub) {
        OrderManagementOuterClass.Order updOrder1 = OrderManagementOuterClass.Order.newBuilder()
                .setId("102")
                .addItems("Google Pixel 3A").addItems("Google Pixel Book")
                .setDestination("Mountain View, CA")
                .setPrice(1100)
                .build();
        OrderManagementOuterClass.Order updOrder2 = OrderManagementOuterClass.Order.newBuilder()
                .setId("103")
                .addItems("Apple Watch S4").addItems("Mac Book Pro").addItems("iPad Pro")
                .setDestination("San Jose, CA")
                .setPrice(2800)
                .build();
        OrderManagementOuterClass.Order updOrder3 = OrderManagementOuterClass.Order.newBuilder()
                .setId("104")
                .addItems("Google Home Mini").addItems("Google Nest Hub").addItems("iPad Mini")
                .setDestination("Mountain View, CA")
                .setPrice(2200)
                .build();

        CountDownLatch finishLatch = new CountDownLatch(1);

        StreamObserver<StringValue> updateOrderResponseObserver = new StreamObserver<StringValue>() {
            @Override
            public void onNext(StringValue value) {
                logger.info("Update Orders Res : " + value.getValue());
            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onCompleted() {
                logger.info("Update orders response  completed!");
                finishLatch.countDown();
            }
        };

        StreamObserver<OrderManagementOuterClass.Order> updateOrderRequestObserver = stub.updateOrders(updateOrderResponseObserver);
        updateOrderRequestObserver.onNext(updOrder1);
        updateOrderRequestObserver.onNext(updOrder2);
        updateOrderRequestObserver.onNext(updOrder3);

        if (finishLatch.getCount() == 0) {
            logger.warning("RPC completed or errored before we finished sending.");
            return;
        }

        updateOrderRequestObserver.onCompleted();

        try {
            if (!finishLatch.await(10, TimeUnit.SECONDS)) {
                logger.warning("FAILED : Process orders cannot finish within 10 seconds");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
