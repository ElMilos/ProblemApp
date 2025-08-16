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
    private Instant changedAtTime = Instant.now();

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Issue getIssue() {
        return issue;
    }

    public void setIssue(Issue issue) {
        this.issue = issue;
    }

    public IssueStatus getToStatus() {
        return toStatus;
    }

    public void setToStatus(IssueStatus toStatus) {
        this.toStatus = toStatus;
    }

    public IssueStatus getFromStatus() {
        return fromStatus;
    }

    public void setFromStatus(IssueStatus fromStatus) {
        this.fromStatus = fromStatus;
    }

    public String getNoteType() {
        return noteType;
    }

    public void setNoteType(String noteType) {
        this.noteType = noteType;
    }

    public User getChangedBy() {
        return changedBy;
    }

    public void setChangedBy(User changedBy) {
        this.changedBy = changedBy;
    }

    public Instant getChangedAtTime() {
        return changedAtTime;
    }

    public void setChangedAtTime(Instant changedNext) {
        this.changedAtTime = changedNext;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
