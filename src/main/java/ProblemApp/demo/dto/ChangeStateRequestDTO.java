package ProblemApp.demo.dto;

import ProblemApp.demo.Enums.IssueStatus;
import org.antlr.v4.runtime.misc.NotNull;

public record ChangeStateRequestDTO(
    @NotNull
    IssueStatus nextState,
    Long assigneeId,     // required for -> IN_PROGRESS (if not already set)
    String description,         // required for -> RESOLVED or -> CLOSED
    Boolean verified,    // required true for -> CLOSED
    String reason        // required for RESOLVED -> IN_PROGRESS (reopen)
) {}

