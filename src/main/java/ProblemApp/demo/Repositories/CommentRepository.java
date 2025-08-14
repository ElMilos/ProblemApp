package ProblemApp.demo.Repositories;

import ProblemApp.demo.Entieties.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface  CommentRepository extends JpaRepository<Comment,Long> {
}
