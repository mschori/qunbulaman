package api.data;

public interface Data {

    void decryptPayload(String payload);

    Object[][] getData();
}
