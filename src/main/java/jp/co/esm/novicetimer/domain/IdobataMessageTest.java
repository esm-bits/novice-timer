package jp.co.esm.novicetimer.domain;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class IdobataMessageTest {

    @Test
    public void Builderのコンストラクタのみを使った生成だとsourceはメッセージのみになること() {
        IdobataMessage message = new IdobataMessage.Builder("message").build();
        assertThat(message.getSource(), is("message"));
    }

    @Test
    public void Builderのコンストラクタとusersメソッドを使った生成だとsourceはアカウントとメッセージになること() {
        IdobataMessage message = new IdobataMessage.Builder("message").users("user").build();
        assertThat(message.getSource(), is("@user message"));
    }

    @Test
    public void Builderのusersメソッドに引数を2つ与えたらアカウントが2つ出力されること() {
        IdobataMessage message = new IdobataMessage.Builder("message").users("user1", "user2").build();
        assertThat(message.getSource(), is("@user1 @user2 message"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void Builderのコンストラクタに空文字を渡すとIllegalArgmentExceptionになること() {
        new IdobataMessage.Builder("").build();
    }

    @Test
    public void Builderのusersにnullや空文字を渡してもスルーされること() {
        IdobataMessage message1 = new IdobataMessage.Builder("message").users(null).build();
        IdobataMessage message2 = new IdobataMessage.Builder("message").users("").build();

        assertThat(message1.getSource(), is("message"));
        assertThat(message2.getSource(), is("message"));
    }
}
