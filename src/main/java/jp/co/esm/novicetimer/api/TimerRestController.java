package jp.co.esm.novicetimer.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jp.co.esm.novicetimer.domain.MultipleTimeLimit;
import jp.co.esm.novicetimer.domain.TimeLimit;
import jp.co.esm.novicetimer.service.TimerService;

@RestController
@RequestMapping("api/timers")
public class TimerRestController {
    @Autowired
    TimerService timerService;

    @PostMapping(params = "!timeLimits")
    @ResponseStatus(HttpStatus.CREATED)
    public String postTimers(@RequestBody TimeLimit timeLimit) {
        return timerService.startTimer(timeLimit);
    }

    @PostMapping(params = "timeLimits")
    @ResponseStatus(HttpStatus.CREATED)
    public MultipleTimeLimit postTimers(@RequestBody MultipleTimeLimit multipleTimeLimit) {
        return timerService.create(multipleTimeLimit);
    }

    @DeleteMapping
    public ResponseEntity<String> putTimers() {
        HttpStatus status = timerService.stopTimer() ? HttpStatus.OK : HttpStatus.NOT_FOUND;
        return new ResponseEntity<>(status);
    }
}
