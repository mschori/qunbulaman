package api.fields;

public class FieldHard implements Field {

    private Integer[][] field;

    public FieldHard() {
        this.field = new Integer[10][10];

    }

    public Integer[][] getField() {
        return field;
    }
}
