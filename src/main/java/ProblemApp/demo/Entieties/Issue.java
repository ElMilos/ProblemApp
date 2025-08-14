package ProblemApp.demo.Entieties;

import ProblemApp.demo.Enums.IssueStatus;
import ProblemApp.demo.Enums.Priority;
import jakarta.persistence.*;

@Entity
@Table(name = "issues")
public class Issue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(nullable = false, length = 4000)
    private String description;

    @ManyToOne
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    @ManyToOne
    @JoinColumn(name = "assignee_id")
    private User assignee;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Priority priority = Priority.MEDIUM;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private IssueStatus status = IssueStatus.OPEN;

    public Issue() {
    }

    public Issue(String title, String description, Priority priority, IssueStatus status) {
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.status = status;
    }
}
