package ProblemApp.demo.Entieties;

import ProblemApp.demo.Enums.IssueStatus;
import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "issue_state_change")
public class IssueChangeState {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "issue_id")
    private Issue issue;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
    private IssueStatus fromStatus;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private IssueStatus toStatus;

    @Column(nullable = false)
    private String noteType;  // RESOLUTION|CLOSURE|REOPEN|GENERIC

    @Column
    private String message;

    @ManyToOne(optional = false)
    @JoinColumn(name = "changed_by")
    private User changedBy;

    @Column(nullable = false)
    private Instant changedNext = Instant.now();

    public IssueChangeState() {
    }

    public IssueChangeState(Issue issue, IssueStatus fromS, IssueStatus toS, String noteType, String message, User by) {
        this.issue = issue;
        this.fromStatus = fromS;
        this.toStatus = toS;
        this.noteType = noteType;
        this.message = message;
        this.changedBy = by;
    }
}
