package jp.co.esm.novicetimer.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TimeLimit {
    private int seconds; // 計測する秒数

    @JsonProperty("idobata_user")
    private String idobataUser;

    public int getSeconds() {
        return seconds;
    }

    public String getIdobataUser() {
        return idobataUser;
    }
}
