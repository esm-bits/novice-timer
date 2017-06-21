package novicetimer.co.jp.novicetimer.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

<<<<<<< HEAD
import novicetimer.co.jp.novicetimer.domain.TimeLimit;
=======
import novicetimer.co.jp.novicetimer.domain.TimerDomain;
>>>>>>> POSTした値を計るタイマー作成
import novicetimer.co.jp.novicetimer.service.TimerService;

@RestController
@RequestMapping("api/timers")
public class TimerRestController {
    @Autowired
    TimerService timerService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
<<<<<<< HEAD
    public String postTimers(@RequestBody TimeLimit timeLimit) {
        return timerService.startTimer(timeLimit);
=======
    public String postTimers(@RequestBody TimerDomain timerDomain) {
<<<<<<< HEAD
        return timerService.create(timerDomain);
>>>>>>> POSTした値を計るタイマー作成
=======
        return timerService.timerStart(timerDomain);
>>>>>>> 時間計測処理の場所と内容を修正、time変数の名前を変更、不要なコードを削除
    }
}
