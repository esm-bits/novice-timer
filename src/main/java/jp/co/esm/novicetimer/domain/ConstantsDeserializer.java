package jp.co.esm.novicetimer.domain;

import java.io.IOException;

import org.springframework.boot.jackson.JsonComponent;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

@JsonComponent
public class ConstantsDeserializer {
    public static class TimerStateCodeDeserializer extends JsonDeserializer<Object> {
        @Override
        public TimerStateCode deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
            TimerStateCode code = TimerStateCode.fromValue(jp.getText());
            if (code == null) {
                throw new IOException();
            }
            return code;
        }
    }
}
