package ProblemApp.demo.dto;

import ProblemApp.demo.Enums.IssueStatus;

public record StatusCountItem(IssueStatus status, long count) {}
