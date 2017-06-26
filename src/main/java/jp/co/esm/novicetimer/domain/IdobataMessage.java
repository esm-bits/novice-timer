package jp.co.esm.novicetimer.domain;

public class IdobataMessage {
    private String source;

    public IdobataMessage(String source) {
        this.source = source;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
