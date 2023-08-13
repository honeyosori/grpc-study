package project;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import static project.ProjectInfo.*;

@Entity
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Long memberId;

    protected Project() {}

    public Project(String name, Long memberId) {
        this.name = name;
        this.memberId = memberId;
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
