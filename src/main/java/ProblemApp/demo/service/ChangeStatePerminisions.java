package ProblemApp.demo.service;

import ProblemApp.demo.Entieties.Issue;
import ProblemApp.demo.Enums.IssueStatus;
import ProblemApp.demo.exceptions.PerminissionExceptions;
import ProblemApp.demo.security.CurrentUser;

public class ChangeStatePerminisions {
    public boolean isAdmin(CurrentUser me){ return "ADMIN".equals(me.role()); }
    public boolean isAssignee(Issue issue, CurrentUser me){
        return issue.getAssignee()!=null && issue.getAssignee().getId().equals(me.id());
    }
    public boolean isReporter(Issue issue, CurrentUser me){
        return issue.getCreatedBy()!=null && issue.getCreatedBy().getId().equals(me.id());
    }

    public void ensureCanTransition(Issue issue, IssueStatus to, CurrentUser me) throws PerminissionExceptions {
        switch (to) {
            case IN_PROGRESS -> {
                // OPEN -> IN_PROGRESS lub RESOLVED -> IN_PROGRESS (reopen)
                boolean ok = isAdmin(me) || isAssignee(issue, me) || (issue.getStatus()==IssueStatus.RESOLVED && isReporter(issue, me));
                if (!ok) deny();
            }
            case RESOLVED -> {
                boolean ok = isAdmin(me) || isAssignee(issue, me);
                if (!ok) deny();
            }
            case CLOSED -> {
                boolean ok = isAdmin(me) || isReporter(issue, me); // zgodnie z policy (Reporter może jeśli polityka ON)
                if (!ok) deny();
            }
            default -> deny();
        }
    }

    private void deny() throws PerminissionExceptions { throw new PerminissionExceptions("Permission denied for transition"); }
}

