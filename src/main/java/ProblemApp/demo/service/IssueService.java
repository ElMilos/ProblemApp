package ProblemApp.demo.service;

import ProblemApp.demo.Entieties.Issue;
import ProblemApp.demo.Entieties.User;
import ProblemApp.demo.Enums.IssueStatus;
import ProblemApp.demo.Enums.Priority;
import ProblemApp.demo.Repositories.IssueRepository;
import ProblemApp.demo.Repositories.UserRepository;
import ProblemApp.demo.dto.CreateIssueRequest;
import ProblemApp.demo.dto.UpdateIssueRequest;
import ProblemApp.demo.security.CurrentUser;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Objects;

public class IssueService {

    private final IssueRepository issues;
    private final UserRepository users;

    public IssueService(IssueRepository issues, UserRepository users) {
        this.issues = issues;
        this.users = users;
    }

    @Transactional
    public Issue createIssue(CreateIssueRequest req, CurrentUser cu) {
        User creator = users.findById(cu.id()).orElseThrow();
        User assignee = null;
        if (req.assigneeId() != null) assignee = users.findById(req.assigneeId())
                .orElseThrow(() -> new EntityNotFoundException("Assignee not found"));
        Issue issue = new Issue(req.title(), req.description(), req.priority(), creator);
        if (assignee != null) issue.setAssignee(assignee);
        return issues.save(issue);
    }


    @Transactional(readOnly = true)
    public Issue getIssue(long id) {
        return issues.findById(id).orElseThrow(() -> new EntityNotFoundException("Issue not founded"));
    }

    @Transactional
    public Issue updateIssue(long id, UpdateIssueRequest req, CurrentUser cu) {
        var issue = getIssue(id);
        boolean canBeEdited = Objects.equals
                (issue.getAssignee() != null ? issue.getAssignee().getId() : null, cu.id())
                || "ADMIN".equals(cu.role());
        if (!canBeEdited) throw new SecurityException("Perminision denied to edit issue");

        issue.setTitle(req.title());
        issue.setDescription(req.description());
        issue.setPriority(req.priority());
        if (req.assigneeId() != null) {
            var assignee = users.findById(req.assigneeId())
                    .orElseThrow(() -> new EntityNotFoundException("Assignee not found"));
            issue.setAssignee(assignee);
        } else {
            issue.setAssignee(null);
        }
        return issue;
    }

    @Transactional(readOnly = true)
    public Page<Issue> getIssuesList(
            IssueStatus status, Priority priority, Long assigneeId,
            Instant from, Instant to, int page, int size, Sort sort) {

        Specification<Issue> spec = Specification.allOf();

        if (status != null)
            spec = spec.and((root, q, cb) -> cb.equal(root.get("status"), status));
        if (priority != null)
            spec = spec.and((root, q, cb) -> cb.equal(root.get("priority"), priority));
        if (assigneeId != null)
            spec = spec.and((root, q, cb) -> cb.equal(root.get("assignee").get("id"), assigneeId));
        if (from != null)
            spec = spec.and((root, q, cb) -> cb.greaterThanOrEqualTo(root.get("createdAt"), from));
        if (to != null)
            spec = spec.and((root, q, cb) -> cb.lessThanOrEqualTo(root.get("createdAt"), to));

        Pageable pageable = PageRequest.of(page, size, sort);
        return issues.findAll(spec, pageable);
    }

    public <T> Page<Issue> list(IssueStatus status, Priority priority, Long assigneeId, Instant fromI, Instant toI, int page, int size, Sort s) {
        return null;
    }
}
