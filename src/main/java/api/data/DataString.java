package api.data;

public class DataString implements Data {

    private String[][] data;

    public DataString(String payload) {
        this.decryptPayload(payload);
    }

    public String[][] getData() {
        return data;
    }

    @Override
    public void decryptPayload(String payload) {
        this.data = new String[][]{{"Hallo"}, {"Hey"}};
    }
}
