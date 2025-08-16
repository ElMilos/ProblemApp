package ProblemApp.demo.Repositories;

import ProblemApp.demo.Entieties.Issue;
import org.springframework.data.repository.Repository;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;
import java.util.List;


public interface IssueReportRepository  extends Repository<Issue, Long> {

    @Query("""
    select i.status as status, count(i) as cnt
    from Issue i
    where i.createdAt between :from and :to
    group by i.status
  """)
    List<Object[]> countByStatusCreated(Instant from, Instant to);

    @Query("""
    select i.status as status, count(i) as cnt
    from Issue i
    where i.updatedAt between :from and :to
    group by i.status
  """)
    List<Object[]> countByStatusUpdated(Instant from, Instant to);
}
