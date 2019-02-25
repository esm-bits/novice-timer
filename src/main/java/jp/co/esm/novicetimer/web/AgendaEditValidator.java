package jp.co.esm.novicetimer.web;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.SmartValidator;

/**
 * アジェンダ編集フォームのバリデータクラス
 */
@Component
public class AgendaEditValidator implements SmartValidator {

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean supports(Class<?> clazz) {
        return AgendaForm.class.isAssignableFrom(clazz);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void validate(Object target, Errors errors) {
        validate(target, errors, new Object[] {});
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void validate(Object target, Errors errors, Object... validationHints) {

        AgendaForm form = AgendaForm.class.cast(target);

        // アジェンダIDが半角数字であること
        try {
            if (form.getId() == null || form.getId().length() > 9) {
                throw new NumberFormatException();
            }
            Integer.parseInt(form.getId());
        } catch (NumberFormatException e) {
            errors.rejectValue("id", null, new Object[] {}, "不正なリクエストです。");
            return;
        }

        List<AgendaForm.SubjectForm> subjects = form.getSubjectForms();

        // サブジェクトリストが空でないこと
        boolean subjectsEmptyError = false;
        if (subjects == null) {
            subjectsEmptyError = true;
        } else {
            // すべての項目がnullまたは空文字のサブジェクトは検証対象外とするため、リストから削除する
            // FYI：AgendaControllerでAgendaクラスに変換する際に削除される
            subjects = subjects.stream()
                .filter(s -> !(isNullOrEmpty(s.getTitle()) && isNullOrEmpty(s.getIdobataUser()) && isNullOrEmpty(s.getMinutes()) ) )
                .collect(Collectors.toList());
            if (subjects.isEmpty()) {
                subjectsEmptyError = true;
            }
        }
        if (subjectsEmptyError) {
            errors.rejectValue("subjectForms", null, new Object[] {}, "入力がありません。");
            return;
        }

        // サブジェクトの内容が適切であること
        boolean titleError = false;
        boolean idobataUserError = false;
        boolean minutesError = false;
        boolean subjectFormsError = false;
        for (int i = 0; i < subjects.size(); i++) {

            final String title = subjects.get(i).getTitle();
            final String idobataUser = subjects.get(i).getIdobataUser();
            final String minutes = subjects.get(i).getMinutes();

            if (isNullOrEmpty(title)) {
                subjectFormsError = true;
            } else {
                // タイトルは100文字以内であること
                if (title.length() > 100) {
                    titleError = true;
                }
            }

            if (isNullOrEmpty(idobataUser)) {
                subjectFormsError = true;
            } else {
                // ユーザ名は100文字以内であること
                if (idobataUser.length() > 100) {
                    idobataUserError = true;
                }
            }

            if (isNullOrEmpty(minutes)) {
                subjectFormsError = true;
            } else {
                // 時間(分)は1～100の半角数字であること
                if (3 < minutes.length() ||
                    !Pattern.compile("^[1-9][0-9]*$").matcher(minutes).find() ||
                    Integer.parseInt(minutes) < 1||
                    100 < Integer.parseInt(minutes)) {

                    minutesError = true;
                }
            }
        }

        // サブジェクトのエラーメッセージを登録する
        // FYI：エラーとなったサブジェクトのindex情報は不要のため、エラーを登録するフィールドは0番目のサブジェクトとしておく
        if (titleError) {
            errors.rejectValue("subjectForms[0].title", null, new Object[] {}, "タイトルは100文字以内にしてください。");
        }
        if (idobataUserError) {
            errors.rejectValue("subjectForms[0].idobataUser", null, new Object[] {}, "ユーザ名は100文字以内にしてください。");
        }
        if (minutesError) {
            errors.rejectValue("subjectForms[0].minutes", null, new Object[] {}, "時間(分)は半角数字で1～100の値を入力してください。");
        }
        if (subjectFormsError) {
            errors.rejectValue("subjectForms", null, new Object[] {}, "入力途中の行があります。すべて入力するかすべて空にしてください。");
        }

    }

    private static boolean isNullOrEmpty(String value) {
        if(value == null || value.isEmpty()) {
            return true;
        }
        return false;
    }

}
