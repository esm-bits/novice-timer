package jp.co.esm.novicetimer.domain;

import org.springframework.stereotype.Component;

@Component
public class Message {
    private String source = "null";

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
