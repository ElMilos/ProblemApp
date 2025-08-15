package ProblemApp.demo.Entieties;

import jakarta.persistence.*;

@Entity
@Table(name="comments")
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String createdTime;

    @ManyToOne
    @JoinColumn(name="issue_id")
    private Issue issue;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    public Comment(){}

    public Comment(String description, User user, Issue issue) {
        this.description = description;
        this.user = user;
        this.issue = issue;
    }

    public Long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public Issue getIssue() {
        return issue;
    }

    public User getUser() {
        return user;
    }
}
