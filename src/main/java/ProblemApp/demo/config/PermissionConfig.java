package ProblemApp.demo.config;

import ProblemApp.demo.service.ChangeStatePerminisions;
import org.springframework.context.annotation.Bean;

public class PermissionConfig
{
    @Bean
    public ChangeStatePerminisions changeStatePerminisions()
    {return new ChangeStatePerminisions();}
}
