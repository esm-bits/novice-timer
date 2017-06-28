package jp.co.esm.novicetimer.domain;

import java.util.stream.Stream;

public class IdobataMessage {
    private String source;

    private IdobataMessage(Builder builder) {
        StringBuilder str = new StringBuilder();
        Stream<String> users = Stream.of(builder.users);

        users.forEach(user -> str.append("@" + user + " "));

        str.append(builder.message);

        this.source = str.toString();
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public static class Builder {
        private String message;
        private String[] users;

        public Builder(String message) {
            this.message = message;
            if (message.isEmpty()) {
                throw new IllegalArgumentException();
            }

            this.users = new String[0];
        }

        public Builder users(String... users) {
            if (users[0] == null) {
                return this;
            }
            this.users = users;
            return this;
        }

        public IdobataMessage build() {
            return new IdobataMessage(this);
        }
    }
}
