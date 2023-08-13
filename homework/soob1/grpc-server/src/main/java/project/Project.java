package project;

import javax.persistence.*;

import static project.ProjectInfo.ProjectResponse;

@Entity
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    protected Project() {}

    public Project(String name, Long memberId) {
        verify(name, memberId);
        this.name = name;
        this.memberId = memberId;
    }

    private void verify(String name, Long memberId) {
        if (name.isBlank()) {
            throw new IllegalArgumentException("Project name cannot be blank.");
        }
        if (memberId < 1) {
            throw new IllegalArgumentException("Invalid member ID.");
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Long getMemberId() {
        return memberId;
    }

    public ProjectResponse toResponse() {
        return ProjectResponse.newBuilder()
                .setId(id)
                .setName(name)
                .setMemberId(memberId)
                .build();
    }
}
