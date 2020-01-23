package server;

import api.Player;
import api.data.Data;
import api.data.DataInteger;
import api.data.DataString;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Game extends Thread {

    private Player[] players;
    private Boolean gameEnd = false;
    private List<GameObserver> observers = new ArrayList<GameObserver>();
    private DataInteger field = new DataInteger();
    private DataString message = new DataString();
    private Boolean needToUpdateObservers = false;
    private Integer playersAlive = 4;

    public Game(Player[] players) {
        this.players = players;

        // Create field
        Integer[][] testField = new Integer[10][10];
        for (Integer[] row : testField) {
            Arrays.fill(row, 50);
        }
        testField[0][0] = 61;
        testField[0][9] = 62;
        testField[9][0] = 63;
        testField[9][9] = 64;
        this.field.setData(testField);

        // Set player-position
        players[0].setPosX(0);
        players[0].setPosY(0);
        players[1].setPosX(0);
        players[1].setPosY(9);
        players[2].setPosX(9);
        players[2].setPosY(0);
        players[3].setPosX(9);
        players[3].setPosY(9);

        // Add players to observers
        for (Player player : players) {
            this.addObserver(player);
        }
    }

    public void run() {

        this.message.setMessage("Das Spiel beginnt in 3 Sekunden.");
        this.updateObservers(1, this.message);

        while (this.playersAlive > 1) {

            this.needToUpdateObservers = false;

            for (int x = 0; x < this.players.length; x++) {

                Data action = null;
                try {
                    action = this.players[x].getInput();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (action instanceof DataString && !this.players[x].getDead()) {
                    this.calculatePlayerAction(x, ((DataString) action).getData());
                }
            }

            if (this.needToUpdateObservers) {
                this.updateObservers(2, this.field);
            }
        }

        this.endGame();
    }

    private void endGame() {
        // inform every player if loser or winner
    }

    private void calculatePlayerAction(Integer playerNumber, String action) {

        switch (action) {
            case "up":
                this.needToUpdateObservers = true;
                break;
            case "down":
                this.needToUpdateObservers = true;
                break;
            case "left":
                this.needToUpdateObservers = true;
                break;
            case "right":
                this.needToUpdateObservers = true;
                break;
            case "bomb":
                this.needToUpdateObservers = true;
                break;
            case "disconnect":
                this.removeObserver(this.players[playerNumber]);
                break;
        }
    }

    private void calculateBomb() {
        // set bomb-timeout and calculate deaths
    }

    private void addObserver(GameObserver observer) {
        this.observers.add(observer);
    }

    private void removeObserver(GameObserver observer) {
        this.observers.remove(observer);
    }

    private void updateObservers(Integer key, Data data) {
        for (GameObserver gameObserver : this.observers) {
            gameObserver.update(key, data);
        }
    }


}
