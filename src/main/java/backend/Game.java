package backend;

import api.Player;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class Game extends Thread {
    private Integer amountOfPlayers = 1;
    private Integer gameFieldDimension = 13;
    private Player[] players;
    private boolean gameEnd = false;

    public Game(Player[] players) {
        this.players = players;
    }

    public void run() {

        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        while (true) {

            for (int x = 0; x < this.amountOfPlayers; x++) {
                if (this.gameEnd) {
                    continue;
                }

                String action = null;
                try {
                    action = this.getMessage(this.players[x]);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (action != null && !action.equals("empty")){
                    this.calculateGame(x + 1, action);
                }
            }

            this.sendMessageToAllPlayers("Actual game as json...");

            if (this.gameEnd) {
                this.endGame();
                break;
            }
        }
    }

    private String getMessage(Player player) throws IOException {
        return player.socket.receive();
    }

    private void sendMessageToAllPlayers(String json) {
        for (int x = 0; x < this.amountOfPlayers; x++) {
            this.players[x].socket.send(json);
        }
    }

    private void endGame() {
        this.sendMessageToAllPlayers("Stopping game...");

        // Stopping clients after highscore
        for (int x = 0; x < this.amountOfPlayers; x++) {
            this.players[x].socket.closeConnections();
        }
    }

    private void calculateGame(Integer playerNumber, String action) {
        // Magic happens
        this.gameEnd = false;
    }
}
