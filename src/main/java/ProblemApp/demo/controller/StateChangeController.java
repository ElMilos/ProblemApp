package ProblemApp.demo.controller;

import ProblemApp.demo.Entieties.Issue;
import ProblemApp.demo.Entieties.IssueChangeState;
import ProblemApp.demo.Repositories.IssueStateChangeRepository;
import ProblemApp.demo.dto.ChangeStateDto;
import ProblemApp.demo.dto.ChangeStateRequestDTO;
import ProblemApp.demo.dto.IssueDto;
import ProblemApp.demo.exceptions.PerminissionExceptions;
import ProblemApp.demo.security.CurrentUser;
import ProblemApp.demo.service.ChangeStateService;
import ProblemApp.demo.service.IssueMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Function;

@RestController
@RequestMapping("/problem/issues/{id}")
public class StateChangeController {
    private final ChangeStateService service;
    private final IssueStateChangeRepository repo;
    private final Function<HttpServletRequest, CurrentUser> cu;
    private final static DateTimeFormatter time = DateTimeFormatter.ISO_INSTANT;

    public StateChangeController(ChangeStateService s, IssueStateChangeRepository r,
                                 Function<HttpServletRequest, CurrentUser> f) {
        this.service = s;
        this.repo = r;
        this.cu = f;
    }

    @PostMapping("/changeState")
    public Object changeState(@PathVariable long id, @RequestBody ChangeStateRequestDTO req, HttpServletRequest http) {
        var me = cu.apply(http);
        Issue updated = null;
        try {
            updated = service.ChangeState(id, req, me);
            return IssueMapper.toDto(updated);
        } catch (PerminissionExceptions e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/transitions")
    public List<ChangeStateDto> history(@PathVariable long id) {
        return repo.findAll().stream()
                .filter(i -> i.getIssue().getId().equals(id))
                .sorted((a, b) -> a.getChangedAtTime().compareTo(b.getChangedAtTime()))
                .map(this::toDto)
                .toList();
    }

    private ChangeStateDto toDto(IssueChangeState ics) {
        return new ChangeStateDto(
                ics.getId(), ics.getIssue().getId(), ics.getFromStatus(), ics.getToStatus(),
                ics.getNoteType(), ics.getMessage(), ics.getChangedBy().getId(),
                time.format(ics.getChangedAtTime())
        );

    }
}
