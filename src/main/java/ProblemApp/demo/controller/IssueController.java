package ProblemApp.demo.controller;


import ProblemApp.demo.Entieties.Issue;
import ProblemApp.demo.Enums.IssueStatus;
import ProblemApp.demo.Enums.Priority;
import ProblemApp.demo.dto.CreateIssueRequest;
import ProblemApp.demo.dto.IssueDto;
import ProblemApp.demo.dto.UpdateIssueRequest;
import ProblemApp.demo.security.CurrentUser;
import ProblemApp.demo.service.IssueMapper;
import ProblemApp.demo.service.IssueService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.*;
import org.springframework.web.bind.annotation.*;
import java.time.*;

import static ProblemApp.demo.service.IssueMapper.toDto;


@RestController
@RequestMapping("/problem/issues")
public class IssueController {
    private final IssueService service;
    private final java.util.function.Function<HttpServletRequest, CurrentUser> current;

    public IssueController(IssueService s, java.util.function.Function<HttpServletRequest, CurrentUser> c){
        this.service=s; this.current=c;
    }

    @PostMapping
    public IssueDto create(@RequestBody CreateIssueRequest req, HttpServletRequest http){
        var me = current.apply(http);
        return toDto(service.createIssue(req, me));
    }

    @GetMapping("/{id}")
    public IssueDto get(@PathVariable long id){
        return toDto(service.getIssue(id));
    }

    @PutMapping("/{id}")
    public IssueDto update(@PathVariable long id, @RequestBody UpdateIssueRequest req, HttpServletRequest http){
        var me = current.apply(http);
        return toDto(service.updateIssue(id, req, me));
    }

    @GetMapping
    public Page<IssueDto> list(
            @RequestParam(required=false) IssueStatus status,
            @RequestParam(required=false) Priority priority,
            @RequestParam(required=false) Long assigneeId,
            @RequestParam(required=false) String from,
            @RequestParam(required=false) String to,
            @RequestParam(defaultValue="0") int page,
            @RequestParam(defaultValue="20") int size,
            @RequestParam(defaultValue="createdAt,DESC") String sort) {

        // sort parser
        String[] parts = sort.split(",");
        Sort s = parts.length==2 ? Sort.by(Sort.Direction.fromString(parts[1]), parts[0]) : Sort.by(Sort.Direction.DESC, "createdAt");

        Instant fromI = (from!=null)? Instant.parse(from): null;
        Instant toI   = (to!=null)? Instant.parse(to): null;

        Page<Issue> issues = service.list(status, priority, assigneeId, fromI, toI, page, size, s);

        return issues.map(issue -> IssueMapper.toDto(issue));

    }
}
