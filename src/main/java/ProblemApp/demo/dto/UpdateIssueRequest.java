package ProblemApp.demo.dto;

import ProblemApp.demo.Enums.Priority;

public record UpdateIssueRequest(String title,
                                 String description,
                                 Priority priority,
                                 Long assigneeId) {
}
