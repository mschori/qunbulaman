package server;

import api.Player;
import api.data.Data;

import java.util.ArrayList;
import java.util.List;

public class GameNew {

    private Integer amountOfPlayers = 1;
    private Integer gameFieldDimension = 13;
    private Player[] players;
    private boolean gameEnd = false;
    private List<GameObserver> observers = new ArrayList<GameObserver>();



    public void addObserver(GameObserver observer) {
        this.observers.add(observer);
    }

    public void removeObserver(GameObserver observer) {
        this.observers.remove(observer);
    }

    public void updateObservers(Data data) {
        for (GameObserver gameObserver : this.observers) {
            gameObserver.update(data);
        }
    }


}
