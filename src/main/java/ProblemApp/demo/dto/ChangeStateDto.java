package ProblemApp.demo.dto;

import ProblemApp.demo.Enums.IssueStatus;

public record ChangeStateDto(
        Long id,
        Long issueId,
        IssueStatus fromStatus,
        IssueStatus toStatus,
        String noteType,
        String message,
        Long changedBy,
        String changedAt
) {}
