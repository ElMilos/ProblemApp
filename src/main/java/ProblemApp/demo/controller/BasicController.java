package ProblemApp.demo.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ProblemApp.demo.security.CurrentUser;

import java.util.function.Function;

@RestController
@RequestMapping("/problem")
public class BasicController {

    private final java.util.function.Function<HttpServletRequest, CurrentUser> provider;

    public BasicController(Function<HttpServletRequest, CurrentUser> provider) {
        this.provider = provider;
    }


    @GetMapping("/user")
    public CurrentUser getUser(HttpServletRequest req) {
        return provider.apply(req);
    }

    @GetMapping("/ping")
    public String ping(){ return "ok"; }
}
