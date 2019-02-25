package jp.co.esm.novicetimer.web;


import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * アジェンダの入力フォームを表すクラス
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AgendaForm {

    private String id;

    private List<SubjectForm> subjectForms;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SubjectForm {

        private String title;

        private String minutes;

        private String idobataUser;

    }

}
