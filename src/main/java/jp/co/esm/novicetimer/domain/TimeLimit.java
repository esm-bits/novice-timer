package jp.co.esm.novicetimer.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TimeLimit {
    private int minutes; // 計測する分数

    @JsonProperty("idobata_user")
    private String idobataUser;//idobataのアカウント名

}
