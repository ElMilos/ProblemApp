package ProblemApp.demo.dto;

import java.util.List;

public record StatusCountResponse(String from, String to, String basedOn, List<StatusCountItem> items) {}

