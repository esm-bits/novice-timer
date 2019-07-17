package jp.co.esm.novicetimer.web;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.validation.BindingResult;
import org.springframework.validation.MapBindingResult;

import jp.co.esm.novicetimer.web.AgendaForm.SubjectForm;

/**
 * アジェンダ新規登録フォームのバリデータクラスのテスト
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AgendaCreateValidatorTest {

    @Autowired
    private AgendaCreateValidator validator;

    /**
     * AgendaCreateValidatorを実行し、結果を返す
     * @param agendaId アジェンダID
     * @param subjectList サブジェクトリスト
     * @return BindingResult
     */
    private BindingResult doValidate(String agendaId, List<SubjectForm> subjectList) {
        BindingResult result = createBindingResult();
        validator.validate(new AgendaForm(agendaId, subjectList) ,result);
        return result;
    }

    /**
     * BindingResultを生成する。
     * @return BindingResult
     */
    private BindingResult createBindingResult() {
        return new MapBindingResult(new HashMap<>(), "");
    }

    @Test
    public void アジェンダIDがnullでないときエラーとなる() {

        // エラーパターン

        BindingResult result = doValidate("", Arrays.asList(new SubjectForm("タイトル１", "1", "ユーザ１")));
        assertThat(result.getFieldErrors(), hasSize(1));
        assertThat(result.getFieldErrors().get(0).getDefaultMessage(), equalTo("不正なリクエストです。"));

        result = doValidate("1", null);
        assertThat(result.getFieldErrors(), hasSize(1));
        assertThat(result.getFieldErrors().get(0).getDefaultMessage(), equalTo("不正なリクエストです。"));

        // アジェンダIDがnullでないとき、それ以外のエラーは発生しない
        result = doValidate("abc", Collections.singletonList(
            new SubjectForm(
                "12345678901234567890123456789012345678901234567890" +
                "123456789012345678901234567890123456789012345678901",
                "101",
                "12345678901234567890123456789012345678901234567890" +
                "123456789012345678901234567890123456789012345678901")));
        assertThat(result.getFieldErrors(), hasSize(1));
        assertThat(result.getFieldErrors().get(0).getDefaultMessage(), equalTo("不正なリクエストです。"));

        // 正常パターン

        result = doValidate(null, Arrays.asList(new SubjectForm("タイトル１", "1", "ユーザ１")));
        assertThat(result.getFieldErrors(), hasSize(0));
    }

    @Test
    public void サブジェクトリストがnullまたは空のときエラーとなる() {

        // エラーパターン

        BindingResult result = doValidate(null, Collections.emptyList());
        assertThat(result.getFieldErrors(), hasSize(1));
        assertThat(result.getFieldErrors().get(0).getDefaultMessage(), equalTo("入力がありません。"));

        result = doValidate(null, null);
        assertThat(result.getFieldErrors(), hasSize(1));
        assertThat(result.getFieldErrors().get(0).getDefaultMessage(), equalTo("入力がありません。"));

        // 全項目が値なしのサブジェクトのみの場合は、エラーとなる

        result = doValidate(null, Collections.singletonList(new SubjectForm("", "", "")));
        assertThat(result.getFieldErrors(), hasSize(1));
        assertThat(result.getFieldErrors().get(0).getDefaultMessage(), equalTo("入力がありません。"));

        result = doValidate(null, Arrays.asList(
            new SubjectForm("", "", ""),
            new SubjectForm(null, null, null),
            new SubjectForm("", null, ""),
            new SubjectForm(null, "", null)));
        assertThat(result.getFieldErrors(), hasSize(1));
        assertThat(result.getFieldErrors().get(0).getDefaultMessage(), equalTo("入力がありません。"));

        // 正常パターン

        result = doValidate(null, Collections.singletonList(new SubjectForm("タイトル１", "1", "ユーザ１")));
        assertThat(result.getFieldErrors(), hasSize(0));
    }

    @Test
    public void タイトルが100文字以内でないときエラーとなる() {

        // エラーパターン

        // タイトルが101文字
        BindingResult result = doValidate(null, Collections.singletonList(
            new SubjectForm(
                "12345678901234567890123456789012345678901234567890" +
                "123456789012345678901234567890123456789012345678901",
                "1", "ユーザ１")));
        assertThat(result.getFieldErrors(), hasSize(1));
        assertThat(result.getFieldErrors().get(0).getDefaultMessage(), equalTo("タイトルは100文字以内にしてください。"));

        // 複数のタイトルがエラー
        result = doValidate(null, Arrays.asList(
            new SubjectForm(
                "1", "1", "ユーザ１"),
            new SubjectForm(
                "12345678901234567890123456789012345678901234567890" +
                "123456789012345678901234567890123456789012345678901",
                "1", "ユーザ２"),
            new SubjectForm(
                "12345678901234567890123456789012345678901234567890" +
                "123456789012345678901234567890123456789012345678901",
                "1", "ユーザ３")
            ));
        assertThat(result.getFieldErrors(), hasSize(1)); // エラーメッセージは１つ
        assertThat(result.getFieldErrors().get(0).getDefaultMessage(), equalTo("タイトルは100文字以内にしてください。"));

        // 正常パターン

        // タイトルが100文字
        result = doValidate(null, Collections.singletonList(
            new SubjectForm(
                "12345678901234567890123456789012345678901234567890" +
                "12345678901234567890123456789012345678901234567890",
                "1", "ユーザ１")));
        assertThat(result.getFieldErrors(), hasSize(0));
    }

    @Test
    public void ユーザ名が100文字以内でないときエラーとなる() {

        // エラーパターン

        // ユーザ名101文字
        BindingResult result = doValidate(null, Collections.singletonList(
            new SubjectForm(
                "タイトル１", "1",
                "12345678901234567890123456789012345678901234567890" +
                "123456789012345678901234567890123456789012345678901")));
        assertThat(result.getFieldErrors(), hasSize(1));
        assertThat(result.getFieldErrors().get(0).getDefaultMessage(), equalTo("ユーザ名は100文字以内にしてください。"));

        // 複数のユーザ名がエラー
        result = doValidate(null, Arrays.asList(
            new SubjectForm(
                "タイトル１", "1", "ユーザ１"),
            new SubjectForm(
                "タイトル２", "1",
                "12345678901234567890123456789012345678901234567890" +
                "123456789012345678901234567890123456789012345678901"),
            new SubjectForm(
                "タイトル３", "1",
                "12345678901234567890123456789012345678901234567890" +
                "123456789012345678901234567890123456789012345678901")
            ));
        assertThat(result.getFieldErrors(), hasSize(1)); // エラーメッセージは１つ
        assertThat(result.getFieldErrors().get(0).getDefaultMessage(), equalTo("ユーザ名は100文字以内にしてください。"));

        // 正常パターン

        // ユーザ名が100文字
        result = doValidate(null, Collections.singletonList(
            new SubjectForm(
                "タイトル１", "1",
                "12345678901234567890123456789012345678901234567890" +
                "12345678901234567890123456789012345678901234567890")
            ));
        assertThat(result.getFieldErrors(), hasSize(0));
    }

    @Test
    public void 時間分が1から100でないときエラーとなる() {

        // エラーパターン

        // 全角の０
        BindingResult result = doValidate(null, Collections.singletonList(
            new SubjectForm("タイトル１", "０", "ユーザ１")));
        assertThat(result.getFieldErrors(), hasSize(1));
        assertThat(result.getFieldErrors().get(0).getDefaultMessage(), equalTo("時間(分)は1～100の値を入力してください。"));

        // 全角の１０１
        result = doValidate(null, Collections.singletonList(
            new SubjectForm("タイトル１", "１０１", "ユーザ１")));
        assertThat(result.getFieldErrors(), hasSize(1));
        assertThat(result.getFieldErrors().get(0).getDefaultMessage(), equalTo("時間(分)は1～100の値を入力してください。"));

        // 半角の-1
        result = doValidate(null, Collections.singletonList(
            new SubjectForm("タイトル１", "-1", "ユーザ１")));
        assertThat(result.getFieldErrors(), hasSize(1));
        assertThat(result.getFieldErrors().get(0).getDefaultMessage(), equalTo("時間(分)は1～100の値を入力してください。"));

        // 半角の0
        result = doValidate(null, Collections.singletonList(
            new SubjectForm("タイトル１", "0", "ユーザ１")));
        assertThat(result.getFieldErrors(), hasSize(1));
        assertThat(result.getFieldErrors().get(0).getDefaultMessage(), equalTo("時間(分)は1～100の値を入力してください。"));

        // 半角の101
        result = doValidate(null, Collections.singletonList(
            new SubjectForm("タイトル１", "101", "ユーザ１")));
        assertThat(result.getFieldErrors(), hasSize(1));
        assertThat(result.getFieldErrors().get(0).getDefaultMessage(), equalTo("時間(分)は1～100の値を入力してください。"));

        // 半角の2147483648
        result = doValidate(null, Collections.singletonList(
                new SubjectForm("タイトル１", "2147483648", "ユーザ１")));
        assertThat(result.getFieldErrors(), hasSize(1));
        assertThat(result.getFieldErrors().get(0).getDefaultMessage(), equalTo("時間(分)は1～100の値を入力してください。"));

        // 半角の-2147483649
        result = doValidate(null, Collections.singletonList(
                new SubjectForm("タイトル１", "-2147483649", "ユーザ１")));
        assertThat(result.getFieldErrors(), hasSize(1));
        assertThat(result.getFieldErrors().get(0).getDefaultMessage(), equalTo("時間(分)は1～100の値を入力してください。"));

        // 複数の時間分がエラー
        result = doValidate(null, Arrays.asList(
            new SubjectForm("タイトル１", "100", "ユーザ１"),
            new SubjectForm("タイトル２", "a", "ユーザ２"),
            new SubjectForm("タイトル３", "一", "ユーザ３"),
            new SubjectForm("タイトル４", "-1", "ユーザ４")));
        assertThat(result.getFieldErrors(), hasSize(1)); // エラーメッセージは１つ
        assertThat(result.getFieldErrors().get(0).getDefaultMessage(), equalTo("時間(分)は1～100の値を入力してください。"));

        // その他
        result = doValidate(null, Collections.singletonList(
            new SubjectForm("タイトル１", "1a0", "ユーザ１")));
        assertThat(result.getFieldErrors(), hasSize(1));
        assertThat(result.getFieldErrors().get(0).getDefaultMessage(), equalTo("時間(分)は1～100の値を入力してください。"));

        result = doValidate(null, Collections.singletonList(
            new SubjectForm("タイトル１", "1a", "ユーザ１")));
        assertThat(result.getFieldErrors(), hasSize(1));
        assertThat(result.getFieldErrors().get(0).getDefaultMessage(), equalTo("時間(分)は1～100の値を入力してください。"));

        result = doValidate(null, Collections.singletonList(
            new SubjectForm("タイトル１", "a9", "ユーザ１")));
        assertThat(result.getFieldErrors(), hasSize(1));
        assertThat(result.getFieldErrors().get(0).getDefaultMessage(), equalTo("時間(分)は1～100の値を入力してください。"));

        // 正常パターン

        result = doValidate(null, Arrays.asList(
            new SubjectForm("タイトル１", "1", "ユーザ１"),
            new SubjectForm("タイトル２", "100", "ユーザ２"),
            new SubjectForm("タイトル３", "21", "ユーザ３"),
            new SubjectForm("タイトル４", "32", "ユーザ４"),
            new SubjectForm("タイトル５", "43", "ユーザ５"),
            new SubjectForm("タイトル６", "54", "ユーザ６"),
            new SubjectForm("タイトル７", "65", "ユーザ７"),
            new SubjectForm("タイトル８", "76", "ユーザ８"),
            new SubjectForm("タイトル９", "87", "ユーザ９"),
            new SubjectForm("タイトルあ", "98", "ユーザあ"),
            new SubjectForm("タイトルい", "99", "ユーザい"),
            new SubjectForm("タイトルう", "１", "ユーザう"),
            new SubjectForm("タイトルえ", "９９", "ユーザえ"),
            new SubjectForm("タイトルお", "１００", "ユーザお"),
            new SubjectForm("タイトルか", "0100", "ユーザか")
        ));
        assertThat(result.getFieldErrors(), hasSize(0));
    }

    @Test
    public void 全項目が値なしのサブジェクトはOKとなる() {
        // 全サブジェクトで全項目値なしとなった場合は、サブジェクトが空と扱われ入力なしエラーとなる

        BindingResult result = doValidate(null, Arrays.asList(
            new SubjectForm("タイトル１", "1", "ユーザ１"),
            new SubjectForm("", "", "")));
        assertThat(result.getFieldErrors(), hasSize(0));

        result = doValidate(null, Arrays.asList(
            new SubjectForm(null, null, ""),
            new SubjectForm("", "", null),
            new SubjectForm("", "", ""),
            new SubjectForm("タイトル１", "1", "ユーザ１"),
            new SubjectForm(null, null, null)));
        assertThat(result.getFieldErrors(), hasSize(0));
    }

    @Test
    public void 一部の項目が値なしのサブジェクトは入力途中エラーとなる() {

        BindingResult result = doValidate(null, Collections.singletonList(
            new SubjectForm(null, "1", "ユーザ１")));
        assertThat(result.getFieldErrors(), hasSize(1));
        assertThat(result.getFieldErrors().get(0).getDefaultMessage(), equalTo("入力途中の行があります。すべて入力するかすべて空にしてください。"));

        result = doValidate(null, Arrays.asList(
            new SubjectForm("タイトル１", "1", "ユーザ１"),
            new SubjectForm("タイトル２", "", "ユーザ２")));
        assertThat(result.getFieldErrors(), hasSize(1));
        assertThat(result.getFieldErrors().get(0).getDefaultMessage(), equalTo("入力途中の行があります。すべて入力するかすべて空にしてください。"));

        result = doValidate(null, Arrays.asList(
            new SubjectForm("タイトル１", "1", "ユーザ１"),
            new SubjectForm("タイトル２", "1", ""),
            new SubjectForm("タイトル３", "1", null)));
        assertThat(result.getFieldErrors(), hasSize(1));
        assertThat(result.getFieldErrors().get(0).getDefaultMessage(), equalTo("入力途中の行があります。すべて入力するかすべて空にしてください。"));

        result = doValidate(null, Arrays.asList(
            new SubjectForm("タイトル１", "1", "ユーザ１"),
            new SubjectForm(null, null, "ユーザ２"),
            new SubjectForm("タイトル３", "1", "ユーザ３")));
        assertThat(result.getFieldErrors(), hasSize(1));
        assertThat(result.getFieldErrors().get(0).getDefaultMessage(), equalTo("入力途中の行があります。すべて入力するかすべて空にしてください。"));
    }

    @Test
    public void 同時に発生しうるエラー() {

        BindingResult result = doValidate(null, Arrays.asList(
            new SubjectForm(
                "12345678901234567890123456789012345678901234567890" +
                "123456789012345678901234567890123456789012345678901",
                "100",
                "12345678901234567890123456789012345678901234567890" +
                "123456789012345678901234567890123456789012345678901"),
            new SubjectForm("", "101", ""),
            new SubjectForm("タイトル１", "100", "ユーザ１")));
        assertThat(result.getFieldErrors(), hasSize(4));
        assertThat(result.getFieldErrors().get(0).getDefaultMessage(), equalTo("タイトルは100文字以内にしてください。"));
        assertThat(result.getFieldErrors().get(1).getDefaultMessage(), equalTo("ユーザ名は100文字以内にしてください。"));
        assertThat(result.getFieldErrors().get(2).getDefaultMessage(), equalTo("時間(分)は1～100の値を入力してください。"));
        assertThat(result.getFieldErrors().get(3).getDefaultMessage(), equalTo("入力途中の行があります。すべて入力するかすべて空にしてください。"));
    }

}
