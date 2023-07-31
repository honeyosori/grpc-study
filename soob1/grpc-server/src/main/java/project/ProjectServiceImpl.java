package project;

import com.google.protobuf.Empty;
import io.grpc.Status;
import io.grpc.StatusException;
import io.grpc.stub.StreamObserver;

import java.util.HashMap;
import java.util.Map;

public class ProjectServiceImpl extends ProjectServiceGrpc.ProjectServiceImplBase {

    private Map<Long, Project> memberProjectMap = new HashMap<>();
    private Long lastProjectId = 0L;

    @Override
    public void createProjectByMember(ProjectOuterClass.Member request, StreamObserver<ProjectOuterClass.Project> responseObserver) {
        long memberId = request.getId();
        String projectName = request.getName() + " (" + request.getLoginId() + ")";
        Project project = new Project(++lastProjectId, projectName, memberId);

        memberProjectMap.put(memberId, project);

        responseObserver.onNext(ProjectOuterClass.Project
                .newBuilder()
                .setId(project.getId())
                .setName(project.getName())
                .setMemberId(project.getMemberId())
                .build());
        responseObserver.onCompleted();
    }

    @Override
    public void deleteProjectByMember(ProjectOuterClass.MemberId request, StreamObserver<Empty> responseObserver) {
        long memberId = request.getValue();

        if (memberProjectMap.containsKey(memberId)) {
            memberProjectMap.remove(memberId);
            responseObserver.onCompleted();
        } else {
            responseObserver.onError(new StatusException(Status.NOT_FOUND));
        }
    }
}
