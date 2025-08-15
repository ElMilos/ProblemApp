package ProblemApp.demo.Repositories;

import ProblemApp.demo.Entieties.IssueChangeState;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IssueStateChangeRepository extends JpaRepository<IssueChangeState, Long> {
}
