package api.payload;

public interface Payload {

    void encryptData(Object data);

    String getPayload();
}
