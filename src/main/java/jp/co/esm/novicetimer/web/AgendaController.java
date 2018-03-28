package jp.co.esm.novicetimer.web;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jp.co.esm.novicetimer.domain.Agenda;
import jp.co.esm.novicetimer.domain.Subject;
import jp.co.esm.novicetimer.service.AgendaService;

@Controller
@RequestMapping("agendas")
public class AgendaController {
    
    @Autowired
    private AgendaService agendaService;
    
    @GetMapping
    public String getList(Model model) {
        
        List<Agenda> agendas = agendaService.findAll();
        model.addAttribute("agendas", agendas);
        return "agendas";
    }
    
    @GetMapping("/detail")
    public String getDetail(@RequestParam Integer id, Model model) {
        
        Agenda agenda = agendaService.findOne(id);
        model.addAttribute("agenda", agenda);
        return "agenda";
    }
    
    @GetMapping("/create")
    public String getCreate(Model model) {
        
        return "agendaCreate";
    }
    
    @PostMapping("/create")
    public String postCreate(AgendaForm form, Model model) {
        
        Agenda agenda = convertAgendaFrom(form);
        agendaService.create(agenda);
        return "redirect:/agendas";
    }
    
    @GetMapping("/edit")
    public String getEdit(@RequestParam Integer id, Model model) {
        
        model.addAttribute("agenda", agendaService.findOne(id));
        return "agendaEdit";
    }
    
    @PostMapping("/edit")
    public String postEdit(AgendaForm form, Model model) {
        
        Agenda agenda = convertAgendaFrom(form);
        agendaService.update(agenda);
        return "redirect:/agendas";
    }
    
    @PostMapping("/delete")
    public String postDelete(String selectAgenda, Model model) {
        
        boolean result = agendaService.deleteAgendaProcess(Integer.valueOf(selectAgenda));
        return result ? "redirect:/agendas" : "削除に失敗しました";
    }
    
    private Agenda convertAgendaFrom(AgendaForm form) {
        
        Agenda agenda = new Agenda();
        agenda.setSubjects(new ArrayList<>());
        
        if (form.getId() == null) {
            agenda.setId(0);
        } else {
            agenda.setId(Integer.valueOf(form.getId()));
        }
        
        form.getSubjectForms().forEach(formSubject -> {
            
            if (formSubject.getTitle().equals("") && formSubject.getIdobataUser().equals("") && formSubject.getMinutes().equals("")) {
                return;
            }
            
            Subject subject = new Subject();
            subject.setTitle(formSubject.getTitle());
            subject.setIdobataUser(formSubject.getIdobataUser());
            subject.setMinutes(Integer.valueOf(formSubject.getMinutes()));
            agenda.getSubjects().add(subject);
        });
        
        return agenda;
    }
    
}
