package novicetimer.co.jp.novicetimer.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import novicetimer.co.jp.novicetimer.domain.TimerDomain;
import novicetimer.co.jp.novicetimer.service.TimerService;

@RestController
@RequestMapping("api/timers")
public class TimerRestController {
    @Autowired
    TimerService timerService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String postTimers(@RequestBody TimerDomain timerDomain) {
        return timerService.create(timerDomain);
    }
}
