package client;

import api.Player;
import api.SocketHandler;
import api.data.Data;
import api.data.DataInteger;
import api.data.DataString;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class TestClient {

    private Player player;

    public static void main(String[] args) throws IOException {

        TestClient client = new TestClient("localhost", 3141);
        Scanner scanner = new Scanner(System.in);

        while (true) {

            System.out.print("Your input>  ");
            String input = scanner.nextLine();

            if (input.equals("abbruch")) {
                break;
            }

            client.sendMessage(input);

            Data response = client.getMessage();

            if (response instanceof DataString) {
                System.out.println(((DataString) response).getData());
            } else if (response instanceof DataInteger) {
                Integer[][] field = ((DataInteger) response).getData();
                for (Integer[] line : field) {
                    for (Integer column : line) {
                        if (column.equals(50)) {
                            System.out.print("O");
                        } else if (column.equals(53)) {
                            System.out.print("B");
                        } else if (column.equals(61)) {
                            System.out.print("X");
                        } else if (column.equals(54)) {
                            System.out.print("Q");
                        }
                    }
                    System.out.println();
                }
            } else {
                System.out.println("Keine Antwort erhalten");
            }
        }

        client.stopConnection();
        System.out.println("Verbindung abgebrochen...");
    }

    public TestClient(String ip, int port) throws IOException {
        Socket socket = new Socket(ip, port);
        SocketHandler socketHandler = new SocketHandler(socket);
        this.player = new Player(socketHandler);
    }

    public void sendMessage(String message) {
        DataString dataString = new DataString();
        dataString.setMessage(message);
        this.player.update(1, dataString);
    }

    public Data getMessage() {
        return this.player.getInput();
    }

    public Data getMessageForce() {
        return this.player.getInputForce();
    }

    public void stopConnection() {
        this.player.disconnect();
    }
}
