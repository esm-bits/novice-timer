package jp.co.esm.novicetimer.domain;

public class IdobataMessage {
    private String source;

    private IdobataMessage(Builder builder) {
        StringBuilder str = new StringBuilder();

        for (String user : builder.users) {
            str.append("@" + user + " ");
        }

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
            this.users = new String[0];
        }

        public Builder users(String... users) {
            this.users = users;
            return this;
        }

        public IdobataMessage build() {
            if (message.isEmpty()) {
                throw new IllegalArgumentException();
            }
            return new IdobataMessage(this);
        }
    }
}
