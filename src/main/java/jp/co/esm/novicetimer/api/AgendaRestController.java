package jp.co.esm.novicetimer.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jp.co.esm.novicetimer.domain.Agenda;
import jp.co.esm.novicetimer.domain.TimerState;
import jp.co.esm.novicetimer.service.AgendaService;

/**
 * Agendaに関するControllerクラス。<p>
 * HTTPからリクエストを受けてレスポンスを返すクラス
 */
@RestController
@RequestMapping("api/agendas")
public class AgendaRestController {
    @Autowired
    private AgendaService agendaService;

    /**
     * GETリクエストを受けて登録されてるアジェンダを全て取得する。
     * <p>
     * GETリクエストにパス変数がなければアジェンダの全取得を行う。
     * @return 登録されているアジェンダのList
     */
    @GetMapping
    public List<Agenda> getAgendas() {
        return agendaService.findAll();
    }

    /**
     * GETリクエストを受けて登録されているアジェンダを取得する。
     * <p>
     * GETリクエストにパス変数があればそのidのアジェンダを探す。
     * @param id 探すアジェンダのid
     * @return アジェンダがあった場合はアジェンダを
     * 無かった場合はnullを返す
     */
    @GetMapping("{id}")
    public Agenda getAgenda(@PathVariable Integer id) {
        return agendaService.findOne(id);
    }

    /**
     * POSTリクエストを受けてアジェンダを登録する。
     * <p>
     * POSTリクエストを受けてリクエストボディに渡されたデータを登録する。<br>
     * HTTPステータスは、登録された場合は201を返す。
     * @param agenda 登録するアジェンダのデータ
     * @return idを割り振られたアジェンダ
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Agenda creatAgenda(@RequestBody Agenda agenda) {
        return agendaService.create(agenda);
    }

    /**
     * タイマーの操作。
     * <p>
     * アジェンダに登録したデータを使ってタイマーを動作させたり、
     * タイマーを停止したりすることができます。
     * @param id 使用するアジェンダのid
     * @param number 使用するsubjectの番号
     * @param timerState タイマーを遷移させたい状態
     * @return タイマーの状態を正しく変更できた場合はHTTPステータスの200を返す。
     * アジェンダの情報が無かったりtimerStateの内容が不正だった場合は400を返す。
     * subjectの値が登録されている範囲外の場合は404を返す。
     */
    @PutMapping("{id}/subjects/{number}")
    public ResponseEntity<String> operateTimer(@PathVariable Integer id,
            @PathVariable Integer number,
            @RequestBody TimerState timerState) {

        try {
            agendaService.changeTimerState(id, number, timerState.getState());
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     *
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({ IllegalArgumentException.class })
    @ResponseBody
    public void handleIllegalArgumentException() {
    }
}
