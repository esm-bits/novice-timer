package jp.co.esm.novicetimer.web;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jp.co.esm.novicetimer.domain.Agenda;
import jp.co.esm.novicetimer.domain.Subject;
import jp.co.esm.novicetimer.service.AgendaService;
import jp.co.esm.novicetimer.web.AgendaForm.SubjectForm;

@Controller
@RequestMapping
public class AgendaController {

    /**
     * アジェンダ登録フォームのバリデータ
     */
    @Autowired
    private AgendaCreateValidator createValidator;

    /**
     * アジェンダ編集フォームのバリデータ
     */
    @Autowired
    private AgendaEditValidator editValidator;

    /**
     * アジェンダの操作を行うサービス
     */
    @Autowired
    private AgendaService agendaService;

    @GetMapping
    public String getRoot() {
        return "redirect:/agendas";
    }

    /**
     * アジェンダ一覧画面を表示する。
     *
     * @param model Model
     * @return アジェンダ一覧ページ
     */
    @GetMapping("/agendas")
    public String getList(Model model) throws Exception {

        List<Agenda> agendas = agendaService.findAll();
        model.addAttribute("agendas", agendas);
        return "agendas";
    }

    /**
     * アジェンダ詳細画面を表示する
     *
     * @param id アジェンダID
     * @param model Model
     * @return アジェンダ詳細ページ
     */
    @GetMapping("/agendas/detail")
    public String getDetail(@RequestParam Integer id, Model model) throws Exception {

        Agenda agenda = agendaService.findOne(id);
        model.addAttribute("agenda", agenda);
        return "agenda";
    }

    /**
     * アジェンダ作成画面を表示する
     *
     * @param model Model
     * @return アジェンダ作成画面
     */
    @GetMapping("/agendas/create")
    public String getCreate(Model model) {

        List<AgendaForm.SubjectForm> subjectForms = new ArrayList<>();
        subjectForms.add(new AgendaForm.SubjectForm());
        subjectForms.add(new AgendaForm.SubjectForm());
        subjectForms.add(new AgendaForm.SubjectForm());
        subjectForms.add(new AgendaForm.SubjectForm());
        subjectForms.add(new AgendaForm.SubjectForm());
        AgendaForm form = new AgendaForm();
        form.setSubjectForms(subjectForms);

        model.addAttribute("agendaForm", form);
        return "agendaCreate";
    }

    /**
     * アジェンダ作成画面を再表示する
     *
     * @param form フォーム
     * @param result BindingResult
     * @param model Model
     * @return アジェンダ編集画面
     */
    @GetMapping("/agendas/create/show")
    public String getCreate(@Validated AgendaForm form, BindingResult result, Model model) throws Exception {

        model.addAttribute("agendaForm", form);
        return "agendaCreate";
    }

    /**
     * アジェンダを作成し、アジェンダ一覧画面へリダイレクトする
     *
     * @param form フォーム
     * @return アジェンダ一覧画面
     */
    @PostMapping("/agendas/create")
    public String postCreate(@Validated AgendaForm form, BindingResult result, Model model) throws Exception {

        ValidationUtils.invokeValidator(createValidator, form, result);
        if (result.hasErrors()) {
            return getCreate(form, result, model);
        }

        Agenda agenda = convertAgendaFrom(form);
        agendaService.create(agenda);
        return "redirect:/agendas";
    }

    /**
     * アジェンダ編集画面を表示する
     *
     * @param id アジェンダID
     * @param model Model
     * @return アジェンダ編集画面
     */
    @GetMapping("/agendas/edit")
    public String getEdit(@RequestParam Integer id, Model model) throws Exception {

        model.addAttribute("agendaForm", convertAgenda(agendaService.findOne(id)));
        return "agendaEdit";
    }

    /**
     * アジェンダ編集画面を再表示する
     *
     * @param form フォーム
     * @param result BindingResult
     * @param model Model
     * @return アジェンダ編集画面
     */
    @GetMapping("/agendas/edit/show")
    public String getEdit(@Validated AgendaForm form, BindingResult result, Model model) throws Exception {

        model.addAttribute("agendaForm", form);
        return "agendaEdit";
    }

    /**
     * アジェンダを編集し、アジェンダ一覧画面へリダイレクトする。<br>
     * 入力内容にエラーがある場合はエラーメッセージを表示する。
     *
     * @param form 入力フォーム
     * @param result BindingResult
     * @param model Model
     * @return アジェンダ一覧画面
     */
    @PostMapping("/agendas/edit")
    public String postEdit(@Validated AgendaForm form, BindingResult result, Model model) throws Exception {

        ValidationUtils.invokeValidator(editValidator, form, result);
        if (result.hasErrors()) {
            return getEdit(form, result, model);
        }

        Agenda agenda = convertAgendaFrom(form);
        agendaService.update(agenda);
        return "redirect:/agendas";
    }

    /**
     * アジェンダを削除し、アジェンダ一覧画面にリダイレクトする。
     * @param selectAgenda 削除するアジェンダのID
     * @return アジェンダ一覧画面
     */
    @PostMapping("/agendas/delete")
    public String postDelete(String selectAgenda) throws Exception {

        // 削除の成功有無は無視して一覧画面にリダイレクトする
        agendaService.deleteAgendaProcess(Integer.valueOf(selectAgenda));
        return "redirect:/agendas";
    }

    private Agenda convertAgendaFrom(AgendaForm form) {

        final Agenda agenda = new Agenda();
        agenda.setSubjects(new ArrayList<>());

        if (form.getId() == null) {
            agenda.setId(0);
        } else {
            agenda.setId(Integer.valueOf(form.getId()));
        }

        form.getSubjectForms().forEach(formSubject -> {

            final String title = formSubject.getTitle() == null ? "" : formSubject.getTitle();
            final String idobataUser = formSubject.getIdobataUser() == null ? "" : formSubject.getIdobataUser();
            final String minutes = formSubject.getMinutes() == null ? "" : formSubject.getMinutes();
            if (title.isEmpty() && idobataUser.isEmpty() && minutes.isEmpty()) {
                return;
            }

            final Subject subject = new Subject();
            subject.setTitle(title);
            subject.setIdobataUser(idobataUser);
            subject.setMinutes(Integer.valueOf(minutes));
            agenda.getSubjects().add(subject);
        });

        return agenda;
    }

    private AgendaForm convertAgenda(Agenda agenda) {

        AgendaForm form = new AgendaForm();
        form.setId(String.valueOf(agenda.getId()));
        List<SubjectForm> subjectForms = new ArrayList<>();
        for (int i = 0; i < agenda.getSubjects().size(); i++) {
            Subject subject = agenda.getSubjects().get(i);
            subjectForms.add(new SubjectForm(subject.getTitle(), String.valueOf(subject.getMinutes()), subject.getIdobataUser()));
        }
        form.setSubjectForms(subjectForms);
        return form;
    }

}
