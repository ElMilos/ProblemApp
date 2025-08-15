package ProblemApp.demo.dto;

import ProblemApp.demo.Enums.IssueStatus;
import ProblemApp.demo.Enums.Priority;

public record IssueDto(Long id,
                       String title,
                       String description,
                       Priority priority,
                       IssueStatus status,
                       Long assigneeId,
                       Long creatorId)
{ }
