package ProblemApp.demo.Policies;

import ProblemApp.demo.Enums.IssueStatus;

import java.util.Map;
import java.util.Set;

public class ChangeStatePolicy{
    private static final Map<IssueStatus, Set<IssueStatus>> ALLOWED = Map.of(
            IssueStatus.OPEN,        Set.of(IssueStatus.IN_PROGRESS),
            IssueStatus.IN_PROGRESS, Set.of(IssueStatus.RESOLVED),
            IssueStatus.RESOLVED,    Set.of(IssueStatus.CLOSED, IssueStatus.IN_PROGRESS),
            IssueStatus.CLOSED,      Set.of() // no transitions by default
    );

    public boolean isAllowed(IssueStatus from, IssueStatus to) {
        return ALLOWED.getOrDefault(from, Set.of()).contains(to);
    }
}
