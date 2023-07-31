package ecommerce;

import com.google.protobuf.StringValue;
import io.grpc.Status;
import io.grpc.StatusException;
import io.grpc.stub.StreamObserver;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class OrderServiceImpl extends OrderManagementGrpc.OrderManagementImplBase {

    private static final Logger logger = Logger.getLogger(OrderServiceImpl.class.getName());

    private OrderManagementOuterClass.Order ord1 = OrderManagementOuterClass.Order.newBuilder()
            .setId("102")
            .addItems("Google Pixel 3A").addItems("Mac Book Pro")
            .setDestination("Mountain View, CA")
            .setPrice(1800)
            .build();
    private OrderManagementOuterClass.Order ord2 = OrderManagementOuterClass.Order.newBuilder()
            .setId("103")
            .addItems("Apple Watch S4")
            .setDestination("San Jose, CA")
            .setPrice(400)
            .build();
    private OrderManagementOuterClass.Order ord3 = OrderManagementOuterClass.Order.newBuilder()
            .setId("104")
            .addItems("Google Home Mini").addItems("Google Nest Hub")
            .setDestination("Mountain View, CA")
            .setPrice(400)
            .build();
    private OrderManagementOuterClass.Order ord4 = OrderManagementOuterClass.Order.newBuilder()
            .setId("105")
            .addItems("Amazon Echo")
            .setDestination("San Jose, CA")
            .setPrice(30)
            .build();
    private OrderManagementOuterClass.Order ord5 = OrderManagementOuterClass.Order.newBuilder()
            .setId("106")
            .addItems("Amazon Echo").addItems("Apple iPhone XS")
            .setDestination("Mountain View, CA")
            .setPrice(300)
            .build();

    private Map<String, OrderManagementOuterClass.Order> orderMap = Stream.of(
                    new AbstractMap.SimpleEntry<>(ord1.getId(), ord1),
                    new AbstractMap.SimpleEntry<>(ord2.getId(), ord2),
                    new AbstractMap.SimpleEntry<>(ord3.getId(), ord3),
                    new AbstractMap.SimpleEntry<>(ord4.getId(), ord4),
                    new AbstractMap.SimpleEntry<>(ord5.getId(), ord5))
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));



    @Override
    public void addOrder(
            OrderManagementOuterClass.Order request,
            StreamObserver<StringValue> responseObserver
    ) {
        logger.info("Order Added - ID: " + request.getId() + ", Destination : " + request.getDestination());
        orderMap.put(request.getId(), request);
        StringValue id = StringValue.newBuilder().setValue(request.getId()).build();
        responseObserver.onNext(id);
        responseObserver.onCompleted();
    }

    @Override
    public void getOrder(
            StringValue request,
            StreamObserver<OrderManagementOuterClass.Order> responseObserver
    ) {
        OrderManagementOuterClass.Order order = orderMap.get(request.getValue());

        if(order != null) {
            responseObserver.onNext(order);
            responseObserver.onCompleted();
        } else {
            responseObserver.onError(new StatusException(Status.NOT_FOUND));
        }
    }

    @Override
    public void searchOrders(
            StringValue request,
            StreamObserver<OrderManagementOuterClass.Order> responseObserver
    ) {
        for (Map.Entry<String, OrderManagementOuterClass.Order> orderEntry : orderMap.entrySet()) {
            OrderManagementOuterClass.Order order = orderEntry.getValue();
            int itemsCount = order.getItemsCount();
            for(int index=0; index < itemsCount; index++) {
                String item = order.getItems(index);
                if(item.contains(request.getValue())) {
                    logger.info("Item found : " + item);
                    responseObserver.onNext(order);
                    break;
                }
            }
        }
    }
}
