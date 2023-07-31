import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import project.ProjectOuterClass;
import project.ProjectServiceGrpc;

import java.util.logging.Logger;

public class GrpcClientApplication {

    private static final Logger logger = Logger.getLogger(GrpcClientApplication.class.getName());

    public static void main(String[] args) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50051)
                .usePlaintext()
                .build();

        ProjectServiceGrpc.ProjectServiceBlockingStub stub = ProjectServiceGrpc.newBlockingStub(channel);

        ProjectOuterClass.Project project = stub.createProjectByMember(ProjectOuterClass.Member
                .newBuilder()
                .setId(1L)
                .setLoginId("user1")
                .setName("사용자1")
                .build());

        logger.info("Project [" + project.getName() + "] created.");
        channel.shutdown();
    }
}
