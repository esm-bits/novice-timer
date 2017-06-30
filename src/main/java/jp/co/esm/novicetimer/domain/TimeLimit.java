package jp.co.esm.novicetimer.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TimeLimit {
    private int minutes; // 計測する分数

    @JsonProperty("idobata_user")
    private String idobataUser;//idobataのアカウント名

    public int getMinutes() {
        return minutes;
    }

    public String getIdobataUser() {
        return idobataUser;
    }
}
