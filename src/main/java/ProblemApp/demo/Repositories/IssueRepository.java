package ProblemApp.demo.Repositories;

import ProblemApp.demo.Entieties.Issue;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IssueRepository extends JpaRepository<Issue,Long> {
}
