package ProblemApp.demo.controller;

import ProblemApp.demo.Entieties.Issue;
import ProblemApp.demo.Repositories.IssueStateChangeRepository;
import ProblemApp.demo.dto.ChangeStateRequestDTO;
import ProblemApp.demo.exceptions.PerminissionExceptions;
import ProblemApp.demo.security.CurrentUser;
import ProblemApp.demo.service.ChangeStateService;
import ProblemApp.demo.service.IssueMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
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

}
