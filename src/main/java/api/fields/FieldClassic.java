package api.fields;

public class FieldClassic implements Field {

    private Integer[][] field;

    public FieldClassic() {
        this.field = new Integer[10][10];

    }

    public Integer[][] getField() {
        return field;
    }
}
