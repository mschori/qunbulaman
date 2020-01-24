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

            Data response = null;

            while (response == null) {
                response = client.getMessage();
            }

            if (response instanceof DataString) {
                System.out.println(((DataString) response).getData());
            } else if (response instanceof DataInteger) {
                Integer[][] field = ((DataInteger) response).getData();
                // gib feld aus
                System.out.print("Feld erhalten!");
            } else {
                System.out.println("Die Antwort ist kein Feld und keine Message...");
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
