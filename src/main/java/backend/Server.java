package backend;

import api.Player;

import java.io.*;
import java.net.ServerSocket;

public class Server {
    private Integer amountOfPlayers = 1;
    private final Integer port = 3141;
    private ServerSocket serverSocket;
    private Integer catchedPlayers = 0;
    private Player[] players = new Player[this.amountOfPlayers];

    public static void main(String[] args) {
        Server server = new Server();
        server.start();
    }

    private void start() {
        try {
            serverSocket = new ServerSocket(this.port);

            while (true) {

                SocketHandler tmp_socket = new SocketHandler(serverSocket.accept());
                String firstInputFromClient = tmp_socket.receiveTimeout();

                if (firstInputFromClient.matches("^Name: .*")){
                    Player player = new Player(tmp_socket);
                    String playerName = this.extractPlayerName(firstInputFromClient);
                    player.setName(playerName);
                    this.players[this.catchedPlayers] = player;
                    this.catchedPlayers ++;
                    this.informPlayers(playerName + " joined game!");
                }else{
                    tmp_socket.send("You are a hacker, right?");
                }

                if (this.catchedPlayers.equals(this.amountOfPlayers)){
                    Game game = new Game(this.players);
                    this.informPlayers("Game starting...");
                    game.start();
                    this.catchedPlayers = 0;
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

    private String extractPlayerName(String inputFromClient){
        return inputFromClient.substring(6);
    }

    private void informPlayers(String message){
        for (int x = 0; x < this.catchedPlayers; x++){
            this.players[x].socket.send(message);
        }
    }
}

