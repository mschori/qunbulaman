package api.fields;

public class FieldEasy implements Field {

    private Integer[][] field;

    public FieldEasy() {
        this.field = new Integer[10][10];

    }

    public Integer[][] getField() {
        return field;
    }

}
