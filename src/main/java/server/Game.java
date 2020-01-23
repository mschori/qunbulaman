package server;

import api.Player;
import api.data.Data;
import api.data.DataInteger;
import api.data.DataString;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Game extends Thread {

    private Player[] players;
    private boolean gameEnd = false;
    private List<GameObserver> observers = new ArrayList<GameObserver>();
    private DataInteger field = new DataInteger();
    private DataString message = new DataString();

    public Game(Player[] players) {
        this.players = players;
        Integer[][] testField = {{1, 2}, {3, 4}};
        this.field.setData(testField);
    }

    public void run() {

        this.message.setMessage("Das Spiel beginnt in 3 Sekunden.");
        this.updateObservers(1, this.message);

        while (true) {

            for (int x = 0; x < this.players.length; x++) {

                if (this.gameEnd) {
                    continue;
                }

                Data action = null;
                try {
                    action = this.players[x].getInput();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (action instanceof DataString) {
                    String[][] input = (String[][]) action.getData();
                    this.calculateGame(x + 1, input[0][0]);
                }
            }

            this.updateObservers(2, this.field);

            if (this.gameEnd) {
                this.endGame();
                break;
            }
        }
    }

    private void endGame() {

    }

    private void calculateGame(Integer playerNumber, String action) {
        // Magic happens with this.field ect...
        this.gameEnd = false;
    }

    public void addObserver(GameObserver observer) {
        this.observers.add(observer);
    }

    public void removeObserver(GameObserver observer) {
        this.observers.remove(observer);
    }

    private void updateObservers(Integer key, Data data) {
        for (GameObserver gameObserver : this.observers) {
            gameObserver.update(key, data);
        }
    }


}
