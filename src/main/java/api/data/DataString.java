package api.data;

public class DataString implements Data {

    private String message;

    public DataString() {
    }

    public DataString(String payload) {
        this.decryptPayload(payload);
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getData() {
        return message;
    }

    @Override
    public void decryptPayload(String payload) {

        if (!payload.contains("Message: ")) {
            this.message = null;
            return;
        }

        this.message = payload.substring(9);
    }
}
