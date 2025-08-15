package ProblemApp.demo.config;

import ProblemApp.demo.Policies.ChangeStatePolicy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StateFlowConfig {
    @Bean
    public ChangeStatePolicy changeStatePolicy(){return new ChangeStatePolicy();}
}
