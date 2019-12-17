package api;

public class PayloadHandler {

    private String outerSplit = "%";
    private String innerSplit = "-";

    public String encodePayload(Integer[][] gamefield){
        StringBuilder payload = new StringBuilder();
        for (Integer[] x : gamefield) {
            for (Integer y : x) {
                payload.append(y);
                payload.append(this.innerSplit);
            }
            payload.setLength(payload.length() -1);
            payload.append(this.outerSplit);
        }
        payload.setLength(payload.length() -1);
        return payload.toString();
    }

    public Integer[][] decodePayload(String payload){
        String[] firstSplit = payload.split(this.outerSplit);
        Integer[][] gamefield = new Integer[firstSplit.length][firstSplit[0].split(this.innerSplit).length];

        for (int x = 0; x < firstSplit.length; x++){
            String[] innerSplit = firstSplit[x].split(this.innerSplit);
            for (int y = 0; y < innerSplit.length; y++){
                gamefield[x][y] = Integer.parseInt(innerSplit[y].trim());
            }
        }

        return gamefield;
    }
}
