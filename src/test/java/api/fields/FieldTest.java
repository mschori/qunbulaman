package api.fields;

import api.payload.Payload;
import api.payload.PayloadFactoryImpl;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FieldTest {

    @Test
    void outputClassicField() {

        FieldClassic classicField = new FieldClassic();
        Integer[][] field = classicField.getField();

        for (Integer[] line : field) {
            for (Integer column : line) {
                if (column.equals(50)) {
                    System.out.print("-");
                } else if (column.equals(51)) {
                    System.out.print("o");
                } else if (column.equals(52)) {
                    System.out.print("X");
                } else if (column.equals(53)) {
                    System.out.print("B");
                } else if (column.equals(54)) {
                    System.out.print("Q");
                } else if (column.equals(61)) {
                    System.out.print("P");
                }
            }
            System.out.println();
        }
    }

}