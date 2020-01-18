package api;

import api.data.DataInteger;
import api.payload.PayloadInteger;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class PayloadHandlerTest {

    @Test
    void encodePayloadField() {

        Integer[][] testArray = new Integer[3][3];
        for (int x = 0; x < testArray.length; x++) {
            for (int y = 0; y < testArray[x].length; y++) {
                testArray[x][y] = 2;
            }
        }

        PayloadInteger payloadInteger = new PayloadInteger(testArray);

        assertEquals("2-2-2%2-2-2%2-2-2", payloadInteger.getPayload());
    }

    @Test
    void decodePayloadField() {

        String testString = "2-2-2%2-2-2%2-2-2";

        Integer[][] testArray = new Integer[3][3];
        for (int x = 0; x < testArray.length; x++) {
            Arrays.fill(testArray[x], 2);
        }

        DataInteger dataInteger = new DataInteger(testString);

        assertArrayEquals(testArray, dataInteger.getData());
    }
}