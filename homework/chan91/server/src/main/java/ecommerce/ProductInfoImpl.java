package ecommerce;

import io.grpc.Status;
import io.grpc.StatusException;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

public class ProductInfoImpl extends ProductInfoGrpc.ProductInfoImplBase {

    private static final Logger logger = Logger.getLogger(ProductInfoImpl.class.getName());


    private Map productMap = new HashMap<String, ProductInfoOuterClass.Product>();

    @Override
    public void addProduct(ProductInfoOuterClass.Product request,
        io.grpc.stub.StreamObserver<ProductInfoOuterClass.ProductID> responseObserver) {

        UUID uuid = UUID.randomUUID();
        logger.info("addProduct start");
        String randomUUIDString = uuid.toString();
        request = request.toBuilder().setId(randomUUIDString).build();
        productMap.put(randomUUIDString, request);
        ProductInfoOuterClass.ProductID id
            = ProductInfoOuterClass.ProductID.newBuilder().setValue(randomUUIDString).build();
        responseObserver.onNext(id);
        responseObserver.onCompleted();
    }

    @Override
    public void getProduct(ProductInfoOuterClass.ProductID request,
    io.grpc.stub.StreamObserver<ProductInfoOuterClass.Product> responseObserver) {

        logger.info("getProduct start");
        String id = request.getValue();
        if (productMap.containsKey(id)) {
            responseObserver.onNext((ProductInfoOuterClass.Product) productMap.get(id));
            responseObserver.onCompleted();
        } else {
            responseObserver.onError(new StatusException(Status.NOT_FOUND));
        }
    }
}
