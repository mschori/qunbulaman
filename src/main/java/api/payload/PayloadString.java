package api.payload;

public class PayloadString implements Payload {

    private String encryptedPayload = null;

    public PayloadString(String[][] data) {
        this.encryptData(data);
    }

    public String getPayload() {
        return encryptedPayload;
    }

    @Override
    public void encryptData(Object[][] data) {
        this.encryptedPayload = "Tschüss";
    }
}
