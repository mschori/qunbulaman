package server;

import api.data.Data;
import api.data.DataInteger;
import api.data.DataString;
import client.TestClient;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TestClientTest {

    @Test
    void clientLoginFail() throws IOException {
        TestClient client = new TestClient("localhost", 3141);
        client.sendMessage("Test");

        Data response = null;

        while (response == null) {
            response = client.getMessage();
        }

        if (response instanceof DataString) {
            System.out.println(((DataString) response).getData());
        } else {
            System.out.println("Das ist kein DataString...");
        }

        client.stopConnection();

        assertEquals("Melde dich bitte richtig an...", response.getData());
    }

    @Test
    void clientLogin() throws IOException {
        TestClient client = new TestClient("localhost", 3141);
        client.sendMessage("Name: Larchfomos");

        Data response = null;

        while (response == null) {
            response = client.getMessage();
        }

        if (response instanceof DataString) {
            System.out.println(((DataString) response).getData());
        } else {
            System.out.println("Das ist kein DataString...");
        }

        client.stopConnection();

        assertEquals("Larchfomos joined game!", response.getData());
    }

    @Test
    void loginAndReady() throws IOException, InterruptedException {
        TestClient client = new TestClient("localhost", 3141);
        client.sendMessage("Name: Larchfomos");

        while (true) {
            Data response = null;

            while (response == null) {
                response = client.getMessage();
            }

            if (response instanceof DataString) {
                String message = ((DataString) response).getData();
                System.out.println(message);
                if (message.equals("Das Spiel beginnt in 3 Sekunden.")) {
                    break;
                }
                if (message.equals("Are you ready?")) {
                    client.sendMessage("Ready!");
                    System.out.println("Sending: Ready!");
                }
            } else if (response instanceof DataInteger) {
                Integer[][] field = ((DataInteger) response).getData();
                for (Integer[] line : field) {
                    for (Integer column : line) {
                        if (column.equals(50)) {
                            System.out.print("O");
                        } else if (column.equals(61)) {
                            System.out.print("X");
                        }
                    }
                    System.out.println();
                }
            } else {
                System.out.println("Die Antwort ist keine Message und kein Field.");
                break;
            }
        }

        client.stopConnection();
    }
}