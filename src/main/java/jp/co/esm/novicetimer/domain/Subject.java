package jp.co.esm.novicetimer.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * アジェンダ内のLTの情報を保持するクラス
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Subject {
    private String title;

    private int minutes; // 計測する分数

    @JsonProperty("idobata_user")
    private String idobataUser; // idobataのアカウント名
}
