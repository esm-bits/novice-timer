package jp.co.esm.novicetimer.domain;

public class TimeLimit {
    private int seconds; // 計測する秒数

    private String idobata_user;//idobataのアカウント名

    public int getSeconds() {
        return seconds;
    }

    public String getIdobata_user() {
        return idobata_user;
    }
}
