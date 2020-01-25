package api.fields;

public class FieldHard implements Field {

    private Integer[][] field;

    public FieldHard() {
        this.field = new Integer[10][10];

        for (int x = 0; x < field.length; x++) {
            for (int y = 0; y < field[0].length; y++) {
                this.field[x][y] = 50;
            }
        }
    }

    public Integer[][] getField() {
        return field;
    }
}
