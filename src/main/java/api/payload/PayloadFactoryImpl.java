package api.payload;

public class PayloadFactoryImpl implements PayloadFactory {

    @Override
    public Payload createPayload(Integer key, Object[][] data) {
        if (key == 1) {
            return new PayloadString((String[][]) data);
        } else if (key == 2) {
            return new PayloadInteger((Integer[][]) data);
        }
        return null;
    }
}
