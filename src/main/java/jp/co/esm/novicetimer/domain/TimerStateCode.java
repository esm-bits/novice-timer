package jp.co.esm.novicetimer.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(using = TimerStateCodeDeserializer.class)
public enum TimerStateCode {
    START("start"),
    STOP("stop"),
    ;

    private String state;

    private TimerStateCode(String str) {
        this.state = str;
    }

    public String getState() {
        return state;
    }
}
