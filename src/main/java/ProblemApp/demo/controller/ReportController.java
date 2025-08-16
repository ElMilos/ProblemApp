package ProblemApp.demo.controller;


import ProblemApp.demo.Enums.IssueStatus;
import ProblemApp.demo.Enums.Priority;
import ProblemApp.demo.dto.StatusCountResponse;
import ProblemApp.demo.service.ReportService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@RestController
@RequestMapping("/api/v1/reports")
public class ReportController {
    private final ReportService service;
    public ReportController(ReportService s){ this.service=s; }

    @GetMapping("/status-counts")
    public StatusCountResponse counts(
            @RequestParam(required=false) String from,
            @RequestParam(required=false) String to,
            @RequestParam(defaultValue="created") String basedOn
    ){
        Instant fromTime = from!=null? Instant.parse(from): null;
        Instant toTime = to!=null?   Instant.parse(to):   null;
        return service.statusCount(fromTime, toTime, basedOn);
    }

    @GetMapping(value="/export", produces="text/csv")
    public ResponseEntity<byte[]> export(
            @RequestParam(required=false) IssueStatus status,
            @RequestParam(required=false) Priority priority,
            @RequestParam(required=false) Long assigneeId,
            @RequestParam(required=false) String from,
            @RequestParam(required=false) String to
    ){
        Instant f = from!=null? Instant.parse(from): null;
        Instant t = to!=null?   Instant.parse(to):   null;
        byte[] csv = service.exportCsv(status, priority, assigneeId, f, t);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"issues_export.csv\"")
                .contentType(MediaType.valueOf("text/csv"))
                .body(csv);
    }
}