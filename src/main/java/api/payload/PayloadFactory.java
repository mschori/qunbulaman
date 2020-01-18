package api.payload;

public interface PayloadFactory {

    Payload createPayload(Integer key, Object[][] data);
}
