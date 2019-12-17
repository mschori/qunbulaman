package backend;

import frontend.TestClient;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

class TestClientTest {

    @Test
    void clientLogin() throws IOException, InterruptedException {
        String response;
        TestClient client = new TestClient();
        client.startConnection("localhost", 3141);
        response = client.sendMessage("Name: Michael");
        System.out.println(response);

        for (int x = 0; x < 5; x++){
            TimeUnit.SECONDS.sleep(1);
            System.out.println(client.getMesage());
        }

        client.stopConnection();
    }

}