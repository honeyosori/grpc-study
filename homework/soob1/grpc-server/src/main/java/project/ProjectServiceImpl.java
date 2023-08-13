package project;

import com.google.protobuf.Empty;
import com.google.protobuf.Int64Value;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ProjectServiceImpl extends ProjectServiceGrpc.ProjectServiceImplBase {

    private final ProjectRepository projectRepository;

	public ProjectServiceImpl() {
		projectRepository = new ProjectRepository();
	}

	@Override
	public void getProject(ProjectInfo.ProjectId request, StreamObserver<ProjectInfo.ProjectResponse> responseObserver) {
		long projectId = request.getValue();
		Optional<Project> project = projectRepository.findById(projectId);

		if (project.isPresent()) {
			ProjectInfo.ProjectResponse projectResponse = project.get().toResponse();
			responseObserver.onNext(projectResponse);
			responseObserver.onCompleted();
		} else {
			responseObserver.onError(Status.NOT_FOUND.withDescription("Project with ID " + projectId + " cannot be found.").asException());
		}
	}

	@Override
	public void getProjectByMemberId(Int64Value request, StreamObserver<ProjectInfo.ProjectListResponse> responseObserver) {
		long memberId = request.getValue();
		List<Project> projects = projectRepository.findByMemberId(memberId);

		List<ProjectInfo.ProjectResponse> projectResponses = projects.stream()
				.map(Project::toResponse)
				.collect(Collectors.toList());
		ProjectInfo.ProjectListResponse projectListResponse = ProjectInfo.ProjectListResponse
				.newBuilder()
				.addAllProjects(projectResponses)
				.build();

		responseObserver.onNext(projectListResponse);
		responseObserver.onCompleted();
	}

	@Override
	public void createProject(ProjectInfo.ProjectRequest request, StreamObserver<ProjectInfo.ProjectResponse> responseObserver) {
		try {
			Project project = new Project(request.getName(), request.getMemberId());
			projectRepository.save(project);
			ProjectInfo.ProjectResponse projectResponse = project.toResponse();

			responseObserver.onNext(projectResponse);
			responseObserver.onCompleted();
		} catch (IllegalArgumentException e) {
			responseObserver.onError(Status.INVALID_ARGUMENT.withDescription(e.getMessage()).asException());
		}
	}

	@Override
	public void deleteProject(ProjectInfo.ProjectId request, StreamObserver<Empty> responseObserver) {
		long projectId = request.getValue();
		projectRepository.deleteById(projectId);
		responseObserver.onCompleted();
	}

	@Override
	public StreamObserver<ProjectInfo.ProjectRequest> createProjects(StreamObserver<ProjectInfo.ProjectResponse> responseObserver) {
		return new StreamObserver<>() {
			@Override
			public void onNext(ProjectInfo.ProjectRequest value) {
				try {
					Project project = new Project(value.getName(), value.getMemberId());
					projectRepository.save(project);
					ProjectInfo.ProjectResponse projectResponse = project.toResponse();
					responseObserver.onNext(projectResponse);
				} catch (IllegalArgumentException e) {
					responseObserver.onError(Status.INVALID_ARGUMENT.withDescription(e.getMessage()).asException());
				}
			}

			@Override
			public void onError(Throwable t) {
			}

			@Override
			public void onCompleted() {
				responseObserver.onCompleted();
			}
		};
	}

	@Override
	public StreamObserver<ProjectInfo.ProjectId> deleteProjects(StreamObserver<Empty> responseObserver) {
		return new StreamObserver<>() {
			@Override
			public void onNext(ProjectInfo.ProjectId value) {
				long projectId = value.getValue();
				projectRepository.deleteById(projectId);
			}

			@Override
			public void onError(Throwable t) {

			}

			@Override
			public void onCompleted() {
				responseObserver.onCompleted();
			}
		};
	}
}
