package jp.co.esm.novicetimer.domain;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "configs")
public class Configs {
    private String hookUrl;

    public void setHookUrl(String hookUrl) {
        this.hookUrl = hookUrl;
    }

    public String getHookUrl() {
        return hookUrl;
    }
}
