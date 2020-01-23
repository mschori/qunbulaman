package api;

import api.data.Data;
import api.data.DataFactoryImpl;
import api.payload.Payload;
import api.payload.PayloadFactoryImpl;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class PayloadAndDataHandlerTest {

    @Test
    void encodePayloadMessage() {

        PayloadFactoryImpl payloadFactory = new PayloadFactoryImpl();

        Payload payload = payloadFactory.createPayload(1, "Hallo Test");

        assertEquals("Message: Hallo Test", payload.getPayload());
    }

    @Test
    void encodePayloadField() {

        Integer[][] testArray = new Integer[3][3];
        for (int x = 0; x < testArray.length; x++) {
            for (int y = 0; y < testArray[x].length; y++) {
                testArray[x][y] = 2;
            }
        }

        PayloadFactoryImpl payloadFactory = new PayloadFactoryImpl();

        Payload payload = payloadFactory.createPayload(2, testArray);

        assertEquals("Field: 2-2-2%2-2-2%2-2-2", payload.getPayload());
    }

    @Test
    void decodePayloadMessage() {

        DataFactoryImpl dataFactory = new DataFactoryImpl();

        Data data = dataFactory.createData(1, "Message: Hallo Test");

        assertEquals("Hallo Test", data.getData());
    }

    @Test
    void decodePayloadField() {

        String testPayload = "Field: 2-2-2%2-2-2%2-2-2";

        Integer[][] expectedArray = new Integer[3][3];
        for (int x = 0; x < expectedArray.length; x++) {
            Arrays.fill(expectedArray[x], 2);
        }

        DataFactoryImpl dataFactory = new DataFactoryImpl();

        Data data = dataFactory.createData(2, testPayload);

        assertArrayEquals(expectedArray, (Object[]) data.getData());
    }
}