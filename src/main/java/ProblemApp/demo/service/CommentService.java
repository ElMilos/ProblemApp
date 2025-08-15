package ProblemApp.demo.service;

import ProblemApp.demo.Entieties.Comment;
import ProblemApp.demo.Repositories.CommentRepository;
import ProblemApp.demo.Repositories.IssueRepository;
import ProblemApp.demo.Repositories.UserRepository;
import ProblemApp.demo.dto.CreateComment;
import ProblemApp.demo.security.CurrentUser;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {
    private final CommentRepository comments;
    private final IssueRepository issues;
    private final UserRepository users;

    public CommentService(CommentRepository c, IssueRepository i, UserRepository u){
        this.comments = c; this.issues = i; this.users = u;
    }

    @Transactional
    public Comment addComment(long issueId, CreateComment req, CurrentUser me){
        var issue = issues.findById(issueId).orElseThrow(() -> new EntityNotFoundException("Issue not found"));
        var author = users.findById(me.id()).orElseThrow();
        var c = new Comment( req.description(), author ,issue);
        return comments.save(c);
    }

    @Transactional(readOnly = true)
    public List<Comment> listComments(long issueId){
        var issue = issues.findById(issueId).orElseThrow(() -> new EntityNotFoundException("Issue not found"));
        return comments.findAll().stream().filter(c -> c.getIssue().getId().equals(issue.getId()))
                .sorted((a,b)-> a.getCreatedTime().compareTo(b.getCreatedTime()))
                .toList();
    }
}
