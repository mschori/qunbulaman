package server;

import api.Player;
import api.SocketHandler;
import api.data.Data;
import api.data.DataString;
import api.fields.FieldFactoryImpl;

import java.io.*;
import java.net.ServerSocket;

public class Server {

    // Constants
    private final Integer amountOfPlayersPerGame = 1;
    private final Integer port = 3141;
    private final Integer difficulty = 1;

    // Propertys
    private ServerSocket serverSocket;
    private Integer catchedPlayers = 0;
    private Player[] players = new Player[this.amountOfPlayersPerGame];
    private DataString message = new DataString();
    private FieldFactoryImpl fieldFactory = new FieldFactoryImpl();

    public static void main(String[] args) {
        Server server = new Server();
        server.start();
    }

    /**
     * Server-loop.
     * Accepts players and start new game for these players.
     */
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
                    player.setName(input.substring(6));
                    this.players[this.catchedPlayers] = player;
                    this.catchedPlayers++;
                    this.updatePlayers(input.substring(6) + " joined game!");
                } else {
                    this.message.setMessage("Melde dich bitte richtig an...");
                    tmp_socket.send(1, this.message.getData());
                    tmp_socket.closeConnections();
                }

                if (this.catchedPlayers.equals(this.amountOfPlayersPerGame)) {
                    System.out.println(this.amountOfPlayersPerGame + " Players present. Starting the game...");
                    Game game = new Game(this.players, this.generateField(this.difficulty));
                    this.updatePlayers("Game starting...");
                    game.start();
                    this.catchedPlayers = 0;
                    this.players = new Player[this.amountOfPlayersPerGame];
                }
            }
        } catch (IOException e) {
            System.out.println("Server failed...");
        }

    }

    /**
     * Stopping server
     */
    private void stop() {
        try {
            serverSocket.close();
        } catch (IOException e) {
            System.out.println("Stoping Server failed...");
        }
    }

    /**
     * Update players with message.
     *
     * @param message Message for every player.
     */
    private void updatePlayers(String message) {
        this.message.setMessage(message);
        for (int x = 0; x < this.catchedPlayers; x++) {
            this.players[x].update(1, this.message);
        }
    }

    /**
     * Generate new game-field base on difficulty.
     *
     * @param difficulty 1 = classic, 2 = easy, 3 = hard
     * @return Game-field as integer[][]
     */
    private Integer[][] generateField(Integer difficulty) {

        return this.fieldFactory.createField(difficulty).getField();
    }
}

