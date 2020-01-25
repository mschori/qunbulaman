package api.fields;

public class FieldFactoryImpl implements FieldFactory {

    @Override
    public Field createField(Integer difficulty) {

        switch (difficulty) {
            case 2:
                return new FieldEasy();
            case 3:
                return new FieldHard();
            default:
                return new FieldClassic();
        }
    }
}
