import project.ProjectGrpcServer;

import java.io.IOException;

public class GrpcServerApplication {

    public static void main(String[] args) throws IOException, InterruptedException {
        final ProjectGrpcServer server = new ProjectGrpcServer();
        server.start();
        server.blockUntilShutdown();
    }
}
