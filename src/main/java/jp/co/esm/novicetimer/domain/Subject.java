package jp.co.esm.novicetimer.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Subject {
    private String title;

    @JsonProperty("idobata_user")
    private String idobataUser;

    private int minutes; // 計測する分数

    public String getTitle() {
        return title;
    }

    public String getIdobataUser() {
        return idobataUser;
    }

    public int getMinutes() {
        return minutes;
    }
}
