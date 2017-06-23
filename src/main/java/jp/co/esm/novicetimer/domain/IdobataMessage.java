package jp.co.esm.novicetimer.domain;

public class IdobataMessage {
    private static final IdobataMessage IDOBATA_MESSAGE = new IdobataMessage();

    public static IdobataMessage getInstance(String source) {
        IDOBATA_MESSAGE.setSource(source);;
        return IDOBATA_MESSAGE;
    }

    private String source = "null";

    private IdobataMessage() {
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
