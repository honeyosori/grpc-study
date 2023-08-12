package project;

import com.google.protobuf.Empty;
import com.google.protobuf.Int64Value;
import io.grpc.stub.StreamObserver;

public class ProjectServiceImpl extends ProjectServiceGrpc.ProjectServiceImplBase {

    private ProjectRepository projectRepository;

	public ProjectServiceImpl() {
		projectRepository = new ProjectRepository();
	}

	@Override
	public void getProject(ProjectInfo.ProjectId request, StreamObserver<ProjectInfo.ProjectResponse> responseObserver) {
		long projectId = request.getValue();
		Project project = projectRepository.findById(projectId);

		ProjectInfo.ProjectResponse projectResponse = ProjectInfo.ProjectResponse
				.newBuilder()
				.setId(project.getId())
				.setName(project.getName())
				.setMemberId(project.getMemberId())
				.build();

		responseObserver.onNext(projectResponse);
		responseObserver.onCompleted();
	}

	@Override
	public void getProjectByMemberId(Int64Value request, StreamObserver<ProjectInfo.ProjectListResponse> responseObserver) {
		super.getProjectByMemberId(request, responseObserver);
	}

	@Override
	public void createProject(ProjectInfo.ProjectRequest request, StreamObserver<ProjectInfo.ProjectResponse> responseObserver) {
		Project project = new Project(request.getName(), request.getMemberId());
		projectRepository.save(project);

		ProjectInfo.ProjectResponse projectResponse = ProjectInfo.ProjectResponse
				.newBuilder()
				.setId(project.getId())
				.setName(project.getName())
				.setMemberId(project.getMemberId())
				.build();

		responseObserver.onNext(projectResponse);
		responseObserver.onCompleted();
	}

	@Override
	public void deleteProject(ProjectInfo.ProjectId request, StreamObserver<Empty> responseObserver) {
		super.deleteProject(request, responseObserver);
	}

	@Override
	public StreamObserver<ProjectInfo.ProjectRequest> createProjects(StreamObserver<ProjectInfo.ProjectResponse> responseObserver) {
		return super.createProjects(responseObserver);
	}

	@Override
	public StreamObserver<ProjectInfo.ProjectId> deleteProjects(StreamObserver<Empty> responseObserver) {
		return super.deleteProjects(responseObserver);
	}
}
