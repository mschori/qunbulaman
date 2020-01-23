package api.data;

public class DataFactoryImpl implements DataFactory {

    @Override
    public Data createData(Integer key, String payload) {
        if (key == 1) {
            return new DataString(payload);
        } else if (key == 2) {
            return new DataInteger(payload);
        }
        return null;
    }
}
