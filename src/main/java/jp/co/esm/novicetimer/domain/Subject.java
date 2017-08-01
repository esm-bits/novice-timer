package jp.co.esm.novicetimer.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * フィールドにタイトル、制限時間、メンションするアカウント名を持つクラス。
 */
@Data
@AllArgsConstructor
public class Subject {
    private String title;

    private int minutes; // 計測する分数

    @JsonProperty("idobata_user")
    private String idobataUser; // idobataのアカウント名
}
