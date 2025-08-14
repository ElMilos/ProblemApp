package ProblemApp.demo.config;

import ProblemApp.demo.Entieties.User;
import ProblemApp.demo.Repositories.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ProblemApp.demo.security.CurrentUser;

import java.util.function.Function;

@Configuration
public class DevController {

    @Bean
    public Function<HttpServletRequest, CurrentUser> currentUserFunction(UserRepository userRepository)
    {
        return req ->
        {
            String id = req.getHeader("X-User-Id");
            Long uid= (id == null || id.isBlank()) ? 1L : Long.parseLong(id);
            User u = userRepository.findById(uid).orElseThrow();
            return new CurrentUser(u.getId(), u.getEmail(), u.getLogin(), u.getRole().name());
        };
    }
}
