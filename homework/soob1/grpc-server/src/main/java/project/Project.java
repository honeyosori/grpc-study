package project;

public class Project {

    private Long id;
    private String name;
    private Long memberId;

    public Project(Long id, String name, Long memberId) {
        this.id = id;
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
}
