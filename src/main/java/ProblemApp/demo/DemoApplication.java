package ProblemApp.demo;

import ProblemApp.demo.Enums.IssueStatus;
import ProblemApp.demo.Enums.Priority;
import ProblemApp.demo.dto.IssueDto;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);

		IssueDto id = new IssueDto(1L,"a","a",
				Priority.MEDIUM, IssueStatus.OPEN, 2L,2L);

	}

}
