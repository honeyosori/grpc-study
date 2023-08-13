package project;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class ProjectClient {

    private static final Logger logger = Logger.getLogger(ProjectClient.class.getName());

    public static void main(String[] args) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50051)
                .usePlaintext()
                .build();

        ProjectServiceGrpc.ProjectServiceBlockingStub stub = ProjectServiceGrpc.newBlockingStub(channel);
        createProjectByUnaryGrpc(stub);

        ProjectServiceGrpc.ProjectServiceStub asyncStub = ProjectServiceGrpc.newStub(channel);
        createProjectsByStreamingGrpc(asyncStub);

        channel.shutdown();
    }

    private static void createProjectByUnaryGrpc(ProjectServiceGrpc.ProjectServiceBlockingStub stub) {
        ProjectInfo.ProjectResponse projectResponse = stub.createProject(ProjectInfo.ProjectRequest
                .newBuilder()
                .setName("new project")
                .setMemberId(1L)
                .build());

        logger.info("Project ID: " + projectResponse.getId() + " created successfully.");
    }

    private static void createProjectsByStreamingGrpc(ProjectServiceGrpc.ProjectServiceStub asyncStub) {
        final CountDownLatch countDownLatch = new CountDownLatch(1);

        StreamObserver<ProjectInfo.ProjectResponse> responseObserver = new StreamObserver<>() {
            @Override
            public void onNext(ProjectInfo.ProjectResponse value) {
                logger.info("Project ID: " + value.getId() + " created successfully.");
            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onCompleted() {
                logger.info("All projects have been created.");
                countDownLatch.countDown();
            }
        };

        StreamObserver<ProjectInfo.ProjectRequest> requestObserver = asyncStub.createProjects(responseObserver);

        requestObserver.onNext(ProjectInfo.ProjectRequest.newBuilder().setName("project1").setMemberId(1L).build());
        requestObserver.onNext(ProjectInfo.ProjectRequest.newBuilder().setName("project2").setMemberId(1L).build());
        requestObserver.onNext(ProjectInfo.ProjectRequest.newBuilder().setName("project3").setMemberId(1L).build());

        if (countDownLatch.getCount() == 0) {
            logger.warning("RPC completed or errored before we finished sending.");
            return;
        }

        requestObserver.onCompleted();

        try {
            if (!countDownLatch.await(120, TimeUnit.SECONDS)) {
                logger.warning("RPC process cannot finish within 60 seconds.");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
