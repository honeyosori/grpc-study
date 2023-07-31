package ecommerce;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import java.util.logging.Logger;
import java.io.IOException;

public class ProductInfoClient {

    private static final Logger logger = Logger.getLogger(ProductInfoClient.class.getName());

    public static void main(String[] args) throws IOException, InterruptedException {
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress("localhost", 50051)
                .usePlaintext()
                .build();

        ProductInfoGrpc.ProductInfoBlockingStub stub = ProductInfoGrpc.newBlockingStub(channel);

        // Add product
        ProductInfoOuterClass.ProductID productID = stub.addProduct(
                ProductInfoOuterClass.Product.newBuilder()
                        .setName("pencil")
                        .setDescription("This pen is the world's most valuable pen")
                        .setPrice(7777777.0f)
                        .build()
        );
        logger.info("Product ID: " + productID.getValue() + " added successfully.");

        // Get product
        ProductInfoOuterClass.Product product = stub.getProduct(productID);
        logger.info("Product: " + product.toString());

        // Get non-existent product
        ProductInfoOuterClass.ProductID wrongId = ProductInfoOuterClass.ProductID.newBuilder().setValue("2").build();
        ProductInfoOuterClass.Product product1 = stub.getProduct(wrongId);

        channel.shutdown();

    }
}
