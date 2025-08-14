package ProblemApp.demo.Repositories;

import ProblemApp.demo.Entieties.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface  UserRepository extends JpaRepository<User, Long> {
}
