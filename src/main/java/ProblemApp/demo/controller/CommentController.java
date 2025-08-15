package ProblemApp.demo.controller;

import ProblemApp.demo.dto.CommentDto;
import ProblemApp.demo.dto.CreateComment;
import ProblemApp.demo.security.CurrentUser;
import ProblemApp.demo.service.CommentMapper;
import ProblemApp.demo.service.CommentService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/problem/issues/{issueId}/comments")
public class CommentController {
    private final CommentService service;
    private final java.util.function.Function<HttpServletRequest, CurrentUser> current;

    public CommentController(CommentService s, java.util.function.Function<HttpServletRequest, CurrentUser> c){
        this.service=s; this.current=c;
    }

    @PostMapping
    public CommentDto add(@PathVariable long issueId, @RequestBody CreateComment req, HttpServletRequest http){
        var me = current.apply(http);
        return CommentMapper.toDto(service.addComment(issueId, req, me));
    }

    @GetMapping
    public List<CommentDto> list(@PathVariable long issueId){
        return service.listComments(issueId).stream().map(CommentMapper::toDto).toList();
    }
}
