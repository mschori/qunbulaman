package api.payload;

public class PayloadInteger implements Payload {

    private String encryptedPayload;

    public PayloadInteger(Integer[][] data) {
        this.encryptData(data);
    }

    public String getPayload() {
        return encryptedPayload;
    }

    @Override
    public void encryptData(Object data) {

        Integer[][] integerData = (Integer[][]) data;

        StringBuilder payload = new StringBuilder();
        for (Integer[] x : integerData) {
            for (Integer y : x) {
                payload.append(y);
                String innerSplit = "-";
                payload.append(innerSplit);
            }
            payload.setLength(payload.length() - 1);
            String outerSplit = "%";
            payload.append(outerSplit);
        }
        payload.setLength(payload.length() - 1);
        this.encryptedPayload = "Field: " + payload.toString();
    }
}
