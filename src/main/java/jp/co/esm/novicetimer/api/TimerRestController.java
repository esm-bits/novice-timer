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

import jp.co.esm.novicetimer.domain.Subject;
import jp.co.esm.novicetimer.service.TimerService;

/**
 *
 *
 */
@RestController
@RequestMapping("api/timers")
public class TimerRestController {
    @Autowired
    TimerService timerService;

    /**
     * タイマーを動作させる
     * <p>
     * Subjectのデータを受け取って指定の時間後に通知を送るタイマーを動作させる。<br>
     * 既にタイマーが動作していた場合は、タイマーの上書きはせず、
     * このメソッドの呼び出しによる状態の変化は無い。<br>
     * @param subject 指定したい時間を保持したsubject
     * @return タイマーを動作させる時間。
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String postTimers(@RequestBody Subject subject) {
        return timerService.startTimer(subject);
    }

    /**
     * 動作しているタイマーを止める。
     * <p>
     * 動作しているタイマーを止める。idやsubjectの指定は不要。
     * @return タイマーを止められたら200。
     * 動いていなかった場合は404。
     */
    @DeleteMapping
    public ResponseEntity<String> stopTimer() {
        HttpStatus status = timerService.stopTimer() ? HttpStatus.OK : HttpStatus.NOT_FOUND;
        return new ResponseEntity<>(status);
    }

}
