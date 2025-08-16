package ProblemApp.demo.service;

import ProblemApp.demo.Entieties.Issue;
import ProblemApp.demo.Enums.IssueStatus;
import ProblemApp.demo.Enums.Priority;
import ProblemApp.demo.Repositories.IssueReportRepository;
import ProblemApp.demo.Repositories.IssueRepository;
import ProblemApp.demo.dto.StatusCountItem;
import ProblemApp.demo.dto.StatusCountResponse;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.stream.Collectors;

public class ReportService {

    private final IssueReportRepository reportRepository;
    private final IssueRepository issueRepository;

    public ReportService(IssueReportRepository reportRepository, IssueRepository issueRepository)
    {
        this.reportRepository=reportRepository;
        this.issueRepository=issueRepository;
    }


    @Transactional(readOnly = true)
    public StatusCountResponse statusCount(Instant fromTime, Instant toTime, String basedOn) {
        if (fromTime== null || toTime == null) {
            // domyślnie ostatnie 30 dni
            toTime = Instant.now();
            fromTime= toTime.minus(Duration.ofDays(30));
        }
        var rows = "updated".equalsIgnoreCase(basedOn)
                ? reportRepository.countByStatusUpdated(fromTime, toTime)
                : reportRepository.countByStatusCreated(fromTime, toTime);

        var items = rows.stream()
                .map(r -> new StatusCountItem((IssueStatus) r[0], (Long) r[1]))
                .sorted(Comparator.comparing((StatusCountItem s) -> s.status().name()))
                .toList();

        return new StatusCountResponse(DateTimeFormatter.ISO_INSTANT.format(fromTime),
                DateTimeFormatter.ISO_INSTANT.format(toTime),
                (basedOn==null?"created":basedOn),
                items);
    }

    @Transactional(readOnly = true)
    public byte[] exportCsv(IssueStatus status, Priority priority, Long assigneeId,
                            Instant fromTime, Instant toTime) {
        Specification<Issue> spec = Specification.allOf();
        if (status!=null)   spec = spec.and((r,q,cb)->cb.equal(r.get("status"), status));
        if (priority!=null) spec = spec.and((r,q,cb)->cb.equal(r.get("priority"), priority));
        if (assigneeId!=null) spec = spec.and((r,q,cb)->cb.equal(r.get("assignee").get("id"), assigneeId));
        if (fromTime!=null)     spec = spec.and((r,q,cb)->cb.greaterThanOrEqualTo(r.get("createdAt"), fromTime));
        if (toTime!=null)       spec = spec.and((r,q,cb)->cb.lessThanOrEqualTo(r.get("createdAt"), toTime));

        var list = issueRepository.findAll(spec, Sort.by(Sort.Direction.DESC, "createdAt"));

        String header = "id,title,priority,status,assigneeEmail,creatorEmail,createdAt,updatedAt\n";
        String body = list.stream().map(i -> String.join(",",
                escape(i.getId()),
                escape(i.getTitle()),
                escape(i.getPriority().name()),
                escape(i.getStatus().name()),
                escape(i.getAssignee()!=null? i.getAssignee().getEmail(): ""),
                escape(i.getCreatedBy()!=null? i.getCreatedBy().getEmail(): ""),
                escape(i.getCreatedTime().toString()),
                escape(i.getUpdatedTime().toString())
        )).collect(Collectors.joining("\n"));

        return (header + body + "\n").getBytes(StandardCharsets.UTF_8);
    }

    private String escape(Object v) {
        String s = v==null? "": v.toString();
        // CSV basic escaping: wrap with quotes if contains comma/quote/newline
        if (s.contains(",") || s.contains("\"") || s.contains("\n")) {
            s = s.replace("\"", "\"\"");
            return "\"" + s + "\"";
        }
        return s;
    }
}