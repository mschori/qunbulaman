package api.data;

public class DataInteger implements Data {

    private Integer[][] data;

    public DataInteger() {
    }

    public DataInteger(String payload) {
        this.decryptPayload(payload);
    }

    public void setData(Integer[][] data) {
        this.data = data;
    }

    public Integer[][] getData() {
        return data;
    }

    @Override
    public void decryptPayload(String payload) {

        if (!payload.contains("Field: ")) {
            this.data = null;
            return;
        }

        payload = payload.substring(7);

        String outerSplit = "%";
        String[] firstSplit = payload.split(outerSplit);
        String innerSplit1 = "-";
        Integer[][] gamefield = new Integer[firstSplit.length][firstSplit[0].split(innerSplit1).length];

        for (int x = 0; x < firstSplit.length; x++) {
            String[] innerSplit = firstSplit[x].split(innerSplit1);
            for (int y = 0; y < innerSplit.length; y++) {
                gamefield[x][y] = Integer.parseInt(innerSplit[y].trim());
            }
        }
        this.data = gamefield;
    }
}
