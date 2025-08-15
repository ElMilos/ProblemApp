package ProblemApp.demo.service;

import ProblemApp.demo.Entieties.Comment;
import ProblemApp.demo.dto.CommentDto;

import java.time.format.DateTimeFormatter;

public class CommentMapper {

    private static final DateTimeFormatter time = DateTimeFormatter.ISO_INSTANT;

    public static CommentDto toDto(Comment c)
    {
        return new CommentDto(c.getId(), c.getUser().getId(), c.getDescription(), time.format(c.getCreatedTime()));
    }
}
