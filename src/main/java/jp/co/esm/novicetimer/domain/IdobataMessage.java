package jp.co.esm.novicetimer.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lombok.Data;

@Data
public class IdobataMessage {
    private String source;

    private IdobataMessage(Builder builder) {
        this.source = String.join(
            " ",
            Stream.concat(
                builder.userList.stream().distinct().map(user -> "@" + user),
                Stream.of(builder.message)
            ).collect(Collectors.toList())
        );
    }

    public static class Builder {
        private String message;
        private List<String> userList;

        public Builder(String message) {
            if (message == null || message.isEmpty()) {
                throw new IllegalArgumentException();
            }
            this.message = message;
            this.userList = new ArrayList<>();
        }

        public Builder users(String... users) {
            if (users == null) {
                return this;
            }
            Stream.of(users).filter(user -> !(user != null && user.isEmpty())).forEach(user -> this.userList.add(user));
            return this;
        }

        public IdobataMessage build() {
            return new IdobataMessage(this);
        }
    }
}
