package document;

import document.global.security.TokenAuthInterceptor;
import document.module.service.DocumentServiceImpl;
import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;
import java.util.logging.Logger;

public class DocumentManagementServer {

    private static final Logger logger = Logger.getLogger(DocumentManagementServer.class.getName());

    public static void main(String[] args) throws IOException, InterruptedException {
        int port = 50051;
        Server server = ServerBuilder.forPort(port)
                .addService(new DocumentServiceImpl())
                .intercept(new TokenAuthInterceptor())
                .build()
                .start();
        logger.info("Server started, listening on " + port);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.info("Shutting down gRPC server sing JVM is shutting down");
            if(server != null) {
                server.shutdown();
            }
            logger.info("Server shut down");
        }));
        server.awaitTermination();
    }
}
