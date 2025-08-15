package ProblemApp.demo.service;

import ProblemApp.demo.Entieties.Issue;
import ProblemApp.demo.dto.IssueDto;

import java.time.format.DateTimeFormatter;

public class IssueMapper {

    private static final DateTimeFormatter time = DateTimeFormatter.ISO_INSTANT;

    public static IssueDto toDto(Issue i)
    {
        return new IssueDto(
                i.getId(), i.getTitle(), i.getDescription(), i.getPriority(), i.getStatus(),
                i.getAssignee()!=null ? i.getAssignee().getId() : null,
                i.getCreatedBy()!=null ? i.getCreatedBy().getId() : null,
                time.format(i.getCreatedTime()),
                time.format(i.getUpdatedTime())

        );
    }
}
