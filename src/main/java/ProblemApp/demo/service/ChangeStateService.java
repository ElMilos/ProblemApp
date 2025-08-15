package ProblemApp.demo.service;


import ProblemApp.demo.Entieties.Issue;
import ProblemApp.demo.Entieties.IssueChangeState;
import ProblemApp.demo.Enums.IssueStatus;
import ProblemApp.demo.Policies.ChangeStatePolicy;
import ProblemApp.demo.Repositories.IssueRepository;
import ProblemApp.demo.Repositories.IssueStateChangeRepository;
import ProblemApp.demo.Repositories.UserRepository;
import ProblemApp.demo.dto.ChangeStateRequestDTO;
import ProblemApp.demo.exceptions.ConflictException;
import ProblemApp.demo.exceptions.PerminissionExceptions;
import ProblemApp.demo.exceptions.ValidationException;
import ProblemApp.demo.security.CurrentUser;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import static ProblemApp.demo.Enums.IssueStatus.*;

@Service
public class ChangeStateService {
    private final IssueRepository issues;
    private final UserRepository users;
    private final IssueStateChangeRepository transitions;
    private final ChangeStatePolicy policy;
    private final ChangeStatePerminisions perms;

    public ChangeStateService(IssueRepository i, UserRepository u,
                              IssueStateChangeRepository t, ChangeStatePolicy p, ChangeStatePerminisions perms){
        this.issues=i; this.users=u; this.transitions=t; this.policy=p; this.perms=perms;
    }

    @Transactional
    public Issue ChangeState(long id, ChangeStateRequestDTO req, CurrentUser me) throws PerminissionExceptions {
        var issue = issues.findById(id).orElseThrow(() -> new EntityNotFoundException("Issue not found"));
        var from = issue.getStatus(); var to = req.nextState();

        if (from == to) throw new ConflictException("Issue already in state " + to);
        if (!policy.isAllowed(from, to)) throw new ConflictException("Illegal transition " + from + " → " + to);

        switch (to) {
            case IN_PROGRESS -> {
                if (issue.getAssignee()==null && req.assigneeId()==null)
                    throw new ValidationException("assigneeId is required to move to IN_PROGRESS");
            }
            case RESOLVED -> {
                if (req.description()==null || req.description().isBlank())
                    throw new ValidationException("description is required to move to RESOLVED");
            }
            case CLOSED -> {
                if (req.description()==null || req.description().isBlank())
                    throw new ValidationException("description is required to move to CLOSED");
                if (req.verified()==null || !req.verified())
                    throw new ValidationException("verified=true is required to move to CLOSED");
            }
            default -> {}
        }
        if (from==IssueStatus.RESOLVED && to==IssueStatus.IN_PROGRESS) {
            if (req.reason()==null || req.reason().isBlank())
                throw new ValidationException("reason is required to reopen");
        }

        perms.ensureCanTransition(issue, to, me);

        if (to==IssueStatus.IN_PROGRESS && issue.getAssignee()==null && req.assigneeId()!=null) {
            var assignee = users.findById(req.assigneeId())
                    .orElseThrow(() -> new EntityNotFoundException("Assignee not found"));
            issue.setAssignee(assignee);
        }

        issue.setStatus(to);

        String noteType = switch (to) {
            case RESOLVED -> "RESOLUTION";
            case CLOSED -> "CLOSURE";
            case IN_PROGRESS -> (from==IssueStatus.RESOLVED ? "REOPEN" : "GENERIC");
            default -> "GENERIC";
        };
        String message = switch (to) {
            case RESOLVED -> req.description();
            case CLOSED -> req.description();
            case IN_PROGRESS -> (from== IssueStatus.RESOLVED ? req.reason() : (req.description()!=null?req.description():null));
            default -> null;
        };

        transitions.save(new IssueChangeState(issue, from, to, noteType, message,
                users.findById(me.id()).orElseThrow()));

        return issue;
    }
}
