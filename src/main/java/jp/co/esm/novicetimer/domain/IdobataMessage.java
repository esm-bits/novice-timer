package jp.co.esm.novicetimer.domain;

public class IdobataMessage {
    public static IdobataMessage getInstance(String source) {
        return new IdobataMessage(source);
    }

    private String source;

    private IdobataMessage(String source) {
        this.source = source;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
