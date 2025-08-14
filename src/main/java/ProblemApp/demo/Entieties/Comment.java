package ProblemApp.demo.Entieties;

import jakarta.persistence.*;

@Entity
@Table(name="comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String description;

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
}
