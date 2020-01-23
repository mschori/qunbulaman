package api.payload;

public class PayloadFactoryImpl implements PayloadFactory {

    @Override
    public Payload createPayload(Integer key, Object data) {
        if (key == 1 && data instanceof String) {
            return new PayloadString((String) data);
        } else if (key == 2 && data instanceof Integer[][]) {
            return new PayloadInteger((Integer[][]) data);
        }
        return null;
    }
}
