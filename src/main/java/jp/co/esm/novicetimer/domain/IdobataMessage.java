package jp.co.esm.novicetimer.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lombok.Data;

/**
 * idobataに送るメッセージを構成するクラス<br>
 * 生成時、最初にBuilderメソッドを、最後にbuildメソッドを呼び出さなければならない<br>
 * 使用例:{@code IdobataMessage message = new IdobataMessage.Builder("message").users("user").build();}
 */
@Data
public class IdobataMessage implements MessageSerializable {
    private String source;

    private IdobataMessage(Builder builder) {
        this.source = Stream.concat(
            builder.userList.stream().distinct().map(user -> "@" + user),
            Stream.of(builder.message)
        ).collect(Collectors.joining(" "));
    }

    public static class Builder {
        private String message;
        private List<String> userList;

        /**
         * idobataに送信するメッセージの本文を登録する。
         * <p>
         * IdobataMessageのインスタンス生成時に最初に呼び出さなければならない
         * @param message idobataに送信するメッセージの本文
         */
        public Builder(String message) {
            if (message == null || message.isEmpty()) {
                throw new IllegalArgumentException();
            }
            this.message = message;
            this.userList = new ArrayList<>();
        }

        /**
         * idobataでメンションするユーザ名を登録するメソッド。
         * <p>
         * 引数が可変長変数であり、1度の呼び出しで複数人追加できる
         * @param users idobataでメンションするユーザ名
         * @return Builder
         */
        public Builder users(String... users) {
            if (users == null) {
                return this;
            }
            Stream.of(users).filter(user -> !(user != null && user.isEmpty())).forEach(user -> this.userList.add(user));
            return this;
        }

        /**
         * 登録した情報をIdobataMessageクラスに変換するメソッド。
         * <p>
         * 必須の終端操作
         * @return IdobataMesageクラスに変換した情報
         */
        public IdobataMessage build() {
            return new IdobataMessage(this);
        }
    }

    @Override
    public String serialize() {
        return source;
    }
}
