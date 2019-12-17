package api;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class PayloadHandlerTest {

    @Test
    void encodePayload() {
        PayloadHandler payloadHandler = new PayloadHandler();

        Integer[][] testArray = new Integer[3][3];
        for (int x = 0; x < testArray.length; x++){
            for (int y = 0; y < testArray[x].length; y++){
                testArray[x][y] = 2;
            }
        }

        assertEquals("2-2-2%2-2-2%2-2-2", payloadHandler.encodePayload(testArray));
    }

    @Test
    void decodePayload() {
        PayloadHandler payloadHandler = new PayloadHandler();

        String testString = "2-2-2%2-2-2%2-2-2";

        Integer[][] testArray = new Integer[3][3];
        for (int x = 0; x < testArray.length; x++){
            Arrays.fill(testArray[x], 2);
        }

        assertArrayEquals(testArray, payloadHandler.decodePayload(testString));
    }
}