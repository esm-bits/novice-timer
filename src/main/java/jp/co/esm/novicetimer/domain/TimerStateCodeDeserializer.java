package jp.co.esm.novicetimer.domain;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class TimerStateCodeDeserializer extends JsonDeserializer<Object> {
    public TimerStateCode deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        return TimerStateCode.fromValue(jp.getText());
    }
}
