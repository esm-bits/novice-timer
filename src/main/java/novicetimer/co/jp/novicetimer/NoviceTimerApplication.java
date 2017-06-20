package novicetimer.co.jp.novicetimer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class NoviceTimerApplication {
    public static void main(String[] args) {
		SpringApplication.run(NoviceTimerApplication.class, args);
	}
}
