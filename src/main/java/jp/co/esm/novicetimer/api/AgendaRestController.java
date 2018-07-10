package jp.co.esm.novicetimer.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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
import jp.co.esm.novicetimer.domain.Subject;
import jp.co.esm.novicetimer.domain.TimerState;
import jp.co.esm.novicetimer.service.AgendaService;

@RestController
@RequestMapping("api/agendas")
public class AgendaRestController {
    @Autowired
    private AgendaService agendaService;

    /**
     * GETリクエストを受けて登録されているアジェンダを全て取得する。
     * <p>
     * 登録されているアジェンダを全て取得する。
     * @return List型で全アジェンダを返す
     * @throws Exception
     */
    @GetMapping
    public List<Agenda> getAgendas() throws Exception {
        return agendaService.findAll();
    }

    /**
     * GETリクエストを受けて登録されているアジェンダを取得する。
     * <p>
     * パス変数から受け取ったidのアジェンダを取得する。
     * @param id 探すアジェンダのid
     * @return アジェンダがあった場合はアジェンダを
     * 無かった場合はnullを返す
     */
    @GetMapping("{id}")
    public Agenda getAgenda(@PathVariable Integer id) throws Exception {
        return agendaService.findOne(id);
    }

    /**
     * POSTリクエストを受けてアジェンダを登録する。
     * <p>
     * POSTリクエストを受けてリクエストボディに渡されたデータを登録する。<br>
     * HTTPステータスは、登録された場合は201を返す。
     * @param agenda 登録するアジェンダのデータ
     * @return idを割り振られたアジェンダ
     * @throws Exception
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Agenda creatAgenda(@RequestBody Agenda agenda) throws Exception {
        return agendaService.create(agenda);
    }

    /**
     * PUTリクエストを受けてアジェンダを更新する。
     * <p>
     * idに対応するアジェンダを、リクエストボディに渡されたアジェンダに変更する。<br>
     * HTTPステータスは、更新された場合は201を返す。idが不正の場合は404を返す。
     * @param agenda 更新するアジェンダの内容
     * @param id 更新されるアジェンダのid
     * @return 更新後のアジェンダ
     * @throws Exception
     */
    @PutMapping("{id}")
    public Agenda editAgenda(@PathVariable Integer id, @RequestBody Agenda agenda) throws Exception {
        agenda.setId(id);
        return agendaService.update(agenda);
    }

    /**
     * PUTリクエストを受けてアジェンダのサブジェクトを1つ更新する。
     * <p>
     * idに対応するアジェンダのnumber番目のサブジェクトを、リクエストボディに渡されたサブジェクトに変更する。<br>
     * HTTPステータスは、更新された場合は201を返す。idが不正の場合と、numberが不正の場合は404を返す。
     * @param subject 更新するサブジェクトの内容
     * @param id 更新されるアジェンダのid
     * @param number 更新されるサブジェクトの番目
     * @return 更新後のアジェンダ
     */
    @PutMapping("{id}/subjects/{number}")
    public Agenda editSubject(@PathVariable Integer id,
        @PathVariable Integer number,
        @RequestBody Subject subject) throws Exception{

        return agendaService.updateSubject(id, number, subject);
    }

    /**
     * タイマーの操作。
     * <p>
     * アジェンダに登録したデータを使ってタイマーを開始・停止する
     * @param id 使用するアジェンダのid
     * @param number 使用するsubjectの番号
     * @param timerState 遷移させたいタイマーの状態
     * @return タイマーの状態を正しく変更できた場合はHTTPステータスの200を返す。
     * アジェンダの情報が無かったりtimerStateの内容が不正だった場合は400を返す。
     * subjectの値が登録されている範囲外の場合は404を返す。
     */
    @PutMapping("{id}/subjects/{number}/timers")
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
     * アジェンダを1つ削除する。
     * <p>
     * パス変数から受け取ったidのアジェンダを削除する。
     * HTTPステータスは削除できた場合に200、削除できなかった場合に404が返される
     * @param id 削除するアジェンダのid
     * @return HTTPステータス
     */
    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteAgenda(@PathVariable Integer id) {
        return agendaService.deleteAgendaProcess(id)
            ? new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /**
     * アジェンダを全削除する。
     * <p>
     * 登録しているアジェンダを全て削除する。HTTPステータスは200が返される
     */
    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public void deleteAgendas() {
        agendaService.deleteAgendasProcess();
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({ IllegalArgumentException.class })
    @ResponseBody
    public void handleIllegalArgumentException() {
    }
}
