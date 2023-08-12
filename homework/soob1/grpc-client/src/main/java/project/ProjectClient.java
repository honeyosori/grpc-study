package project;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.logging.Logger;

public class ProjectClient {

    private static final Logger logger = Logger.getLogger(ProjectClient.class.getName());

    public static void main(String[] args) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50051)
                .usePlaintext()
                .build();

        ProjectServiceGrpc.ProjectServiceBlockingStub stub = ProjectServiceGrpc.newBlockingStub(channel);

        ProjectInfo.ProjectResponse projectResponse = stub.createProject(ProjectInfo.ProjectRequest
                .newBuilder()
                .setName("new project")
                .setMemberId(1L)
                .build());

        logger.info("Project ID: " + projectResponse.getId() + " created successfully.");
        channel.shutdown();
    }
}
