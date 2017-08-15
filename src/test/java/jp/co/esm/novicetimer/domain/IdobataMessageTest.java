package jp.co.esm.novicetimer.domain;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

import jp.co.esm.novicetimer.domain.IdobataMessage;
import jp.co.esm.novicetimer.domain.IdobataMessage.Builder;

public class IdobataMessageTest {

    @Test
    public void Builderのコンストラクタのみを使った生成ではsourceはメッセージのみになること() {
        IdobataMessage message = new IdobataMessage.Builder("message").build();
        assertThat(message.getSource(), is("message"));
    }

    @Test
    public void Builderのコンストラクタとusersメソッドを使った生成ではsourceはアカウントとメッセージになること() {
        IdobataMessage message = new IdobataMessage.Builder("message").users("user").build();
        assertThat(message.getSource(), is("@user message"));
    }

    @Test
    public void Builderのusersメソッドに引数を2つ与えたらアカウントが2つ出力されること() {
        IdobataMessage message = new IdobataMessage.Builder("message").users("user1", "user2").build();
        assertThat(message.getSource(), is("@user1 @user2 message"));
    }

    @Test
    public void Builderのusersメソッドを2つ呼び出したらアカウントが2つの引数分出力されること() {
        IdobataMessage message = new IdobataMessage.Builder("message").users("user1").users("user2").build();
        assertThat(message.getSource(), is("@user1 @user2 message"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void Builderのコンストラクタに空文字を渡すとIllegalArgmentExceptionが発生すること() {
        new IdobataMessage.Builder("").build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void Builderのコンストラクタにnullを渡すとIllegalArgmentExceptionが発生すること() {
        new IdobataMessage.Builder(null).build();
    }

    @Test
    public void Builderのusersにnullや空文字を渡してもアカウントとして追加されないこと() {
        IdobataMessage message1 = new IdobataMessage.Builder("message").users(null).build();
        IdobataMessage message2 = new IdobataMessage.Builder("message").users("").build();

        assertThat(message1.getSource(), is("message"));
        assertThat(message2.getSource(), is("message"));
    }
}
