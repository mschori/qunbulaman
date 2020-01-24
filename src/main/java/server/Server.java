package server;

import api.Player;
import api.SocketHandler;
import api.data.Data;
import api.data.DataString;
import api.fields.Field;
import api.fields.FieldFactoryImpl;

import java.io.*;
import java.net.ServerSocket;

public class Server {
    private final Integer amountOfPlayersPerGame = 1;
    private final Integer port = 3141;
    private final Integer difficulty = 1;
    private ServerSocket serverSocket;
    private Integer catchedPlayers = 0;
    private Player[] players = new Player[this.amountOfPlayersPerGame];
    private DataString message = new DataString();

    public static void main(String[] args) {
        Server server = new Server();
        server.start();
    }

    private void start() {
        try {
            serverSocket = new ServerSocket(this.port);

            System.out.println("Server gestartet und einsatzbereit.");

            while (true) {

                SocketHandler tmp_socket = new SocketHandler(serverSocket.accept());
                Data firstInputFromClient = tmp_socket.receiveForce();

                String input = (String) firstInputFromClient.getData();

                System.out.println("Input: " + input);

                if (input.matches("^Name: .*")) {
                    Player player = new Player(tmp_socket);
                    String playerName = input.substring(6);
                    player.setName(playerName);
                    this.players[this.catchedPlayers] = player;
                    this.catchedPlayers++;
                    this.informPlayers(playerName + " joined game!");
                } else {
                    this.message.setMessage("Melde dich bitte richtig an...");
                    tmp_socket.send(1, this.message.getData());
                    tmp_socket.closeConnections();
                }

                System.out.println("Present Players: " + this.catchedPlayers);

                if (this.catchedPlayers.equals(this.amountOfPlayersPerGame)) {
                    System.out.println(this.amountOfPlayersPerGame + " Players present. Starting the game...");
                    Game game = new Game(this.players, this.generateField(this.difficulty));
                    this.informPlayers("Game starting...");
                    game.start();
                    this.catchedPlayers = 0;
                    this.players = new Player[this.amountOfPlayersPerGame];
                }
            }
        } catch (IOException e) {
            System.out.println("Server failed...");
        }

    }

    private void stop() {
        try {
            serverSocket.close();
        } catch (IOException e) {
            System.out.println("Stoping Server failed...");
        }
    }

    private void informPlayers(String message) {
        this.message.setMessage(message);
        for (int x = 0; x < this.catchedPlayers; x++) {
            this.players[x].update(1, this.message);
        }
    }

    private Integer[][] generateField(Integer difficulty) {

        FieldFactoryImpl fieldFactory = new FieldFactoryImpl();
        Field field = fieldFactory.createField(difficulty);

        return field.getField();
    }
}

