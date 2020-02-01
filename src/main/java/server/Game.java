package server;

import api.Bomb;
import api.GameObserver;
import api.Player;
import api.data.Data;
import api.data.DataInteger;
import api.data.DataString;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class Game extends Thread {

    // Propertys
    private Player[] players;
    private List<GameObserver> observers = new ArrayList<>();
    private List<Bomb> bombs = new ArrayList<>();
    private DataInteger fieldObject = new DataInteger();
    private DataString messageObject = new DataString();
    private Boolean needToUpdateObservers = false;
    private Integer playersAlive;

    public Game(Player[] players, Integer[][] field) {
        this.players = players;
        this.playersAlive = players.length;
        this.fieldObject.setData(field);

        // Set player-positions
        this.setPositions();

        // Add players to observers
        for (Player player : players) {
            this.addObserver(player);
        }
    }

    /**
     * Set all player-positions.
     */
    private void setPositions() {

        Integer[][] field = this.fieldObject.getData();
        Integer[][] positions = {{0, 0}, {field.length - 1, 0}, {0, field.length - 1}, {field.length - 1, field.length - 1}};

        for (int x = 0; x < this.players.length; x++) {
            field[positions[x][0]][positions[x][1]] = 61 + x;
            this.players[x].setPosX(positions[x][0]);
            this.players[x].setPosY(positions[x][1]);
            this.messageObject.setMessage("Du bist Spieler: " + (x + 1));
            this.players[x].update(1, this.messageObject);
        }

        this.fieldObject.setData(field);
    }

    /**
     * Game-loop.
     * Get player-inputs, calculate them and inform every player about the changes.
     */
    public void run() {

        System.out.println("Field initialised. Sending everyone the field...");
        this.updateObservers(2, this.fieldObject);

        System.out.println("Check if players are ready...");
        this.checkIfPlayersAreReady();

        this.messageObject.setMessage("Das Spiel beginnt in 3 Sekunden.");
        this.updateObservers(1, this.messageObject);

        // Constants
        Integer amountSurvivors = 1;
        while (this.playersAlive > amountSurvivors) {

            this.needToUpdateObservers = false;

            // Get input and calculated it for every player
            for (int x = 0; x < this.players.length; x++) {

                if (this.players[x].getDead()) {
                    continue;
                }

                Data action = null;
                action = this.players[x].getInput();

                if (action instanceof DataString) {
                    String input = ((DataString) action).getData();
                    System.out.println("Input from " + this.players[x].getName() + ": " + input);
                    this.calculatePlayerAction(x, input);
                }
            }

            this.calculateBomb();

            if (this.needToUpdateObservers) {
                this.updateObservers(2, this.fieldObject);
            }

            this.checkDisconnects();
            this.checkIfPlayerIsAlive();
        }

        this.endGame();
        System.out.println("Game is over. Game-Tread terminated.");
    }

    /**
     * Check connection to players.
     */
    private void checkDisconnects() {

        for (Player player : this.players) {
            if (!player.isConnected()) {
                player.setReady(true);
                player.setDead(true);
            }
        }
    }

    /**
     * Check if players are dead or alive.
     */
    private void checkIfPlayerIsAlive() {
        Integer counter = 0;
        for (Player player : this.players) {
            if (!player.getDead()) {
                counter++;
            }
        }
        this.playersAlive = counter;
    }

    /**
     * Wait till every player is ready.
     * Disconnects count as ready.
     */
    private void checkIfPlayersAreReady() {

        for (Player player : this.players) {
            this.messageObject.setMessage("Are you ready? Send 'Ready!' to continue.");
            player.update(1, this.messageObject);
        }

        int readyPlayers = 0;

        while (readyPlayers < this.players.length) {

            readyPlayers = 0;

            for (Player player : this.players) {

                if (player.getReady()) {
                    readyPlayers++;
                    continue;
                }

                if (!player.isConnected()) {
                    player.setReady(true);
                    continue;
                }

                Data action = player.getInput();

                if (action instanceof DataString) {
                    String input = ((DataString) action).getData();
                    System.out.println("Input from " + player.getName() + ": " + input);
                    if (input.equals("Ready!")) {
                        player.setReady(true);
                        this.messageObject.setMessage(player.getName() + " is ready.");
                        this.updateObservers(1, this.messageObject);
                    }
                }
            }
        }
        this.messageObject.setMessage("Everyone is ready!");
        this.updateObservers(1, this.messageObject);
    }

    /**
     * Ending game for everyone.
     * Inform players about lose or win.
     */
    private void endGame() {

        for (Player player : this.players) {
            if (player.getDead()) {
                this.messageObject.setMessage("You lose!");
                player.update(1, this.messageObject);
            } else {
                this.messageObject.setMessage("You win!");
                player.update(1, this.messageObject);
            }
        }
    }

    /**
     * Calculate player-input.
     * If input is accepted every player gets updated.
     *
     * @param playerNumber Number of player in player-array.
     * @param action       Input from player.
     */
    private void calculatePlayerAction(Integer playerNumber, String action) {

        Integer posX = this.players[playerNumber].getPosX();
        Integer posY = this.players[playerNumber].getPosY();
        Integer[][] field = this.fieldObject.getData();

        switch (action) {
            case "left":
                if (posY > 0) {
                    if (field[posX][posY - 1] == 50 || field[posX][posY - 1] == 53) {
                        if (field[posX][posY] == 54) {
                            field[posX][posY] = 53;
                        } else {
                            field[posX][posY] = 50;
                        }
                        field[posX][posY - 1] = 60 + (playerNumber + 1);
                        this.players[playerNumber].setPosY(posY - 1);
                    }
                }
                this.needToUpdateObservers = true;
                break;
            case "right":
                if (posY + 1 < field.length) {
                    if (field[posX][posY + 1] == 50 || field[posX][posY + 1] == 53) {
                        if (field[posX][posY] == 54) {
                            field[posX][posY] = 53;
                        } else {
                            field[posX][posY] = 50;
                        }
                        field[posX][posY + 1] = 60 + (playerNumber + 1);
                        this.players[playerNumber].setPosY(posY + 1);
                    }
                }
                this.needToUpdateObservers = true;
                break;
            case "up":
                if (posX > 0) {
                    if (field[posX - 1][posY] == 50 || field[posX - 1][posY] == 53) {

                        if (field[posX][posY] == 54) {
                            field[posX][posY] = 53;
                        } else {
                            field[posX][posY] = 50;
                        }
                        field[posX - 1][posY] = 60 + (playerNumber + 1);
                        this.players[playerNumber].setPosX(posX - 1);
                    }
                }
                this.needToUpdateObservers = true;
                break;
            case "down":
                if (posX + 1 < field.length) {
                    if (field[posX + 1][posY] == 50 || field[posX + 1][posY] == 53) {
                        if (field[posX][posY] == 54) {
                            field[posX][posY] = 53;
                        } else {
                            field[posX][posY] = 50;
                        }
                        field[posX + 1][posY] = 60 + (playerNumber + 1);
                        this.players[playerNumber].setPosX(posX + 1);
                    }
                }
                this.needToUpdateObservers = true;
                break;
            case "bomb":
                Bomb bomb = new Bomb(posX, posY);
                this.bombs.add(bomb);
                field[posX][posY] = 54;
                this.needToUpdateObservers = true;
                break;
            case "disconnect":
                this.removeObserver(this.players[playerNumber]);
                this.players[playerNumber].setDead(true);
                this.players[playerNumber].disconnect();
                break;
            default:
                break;
        }

        this.fieldObject.setData(field);
    }

    /**
     * Calculate existing bombs.
     * Whitch players die and whitch blocks get destroyed.
     */
    private void calculateBomb() {
        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
        Integer[][] field = this.fieldObject.getData();

        List<Bomb> bombsToRemove = new ArrayList<>();

        if (!this.bombs.isEmpty()) {
            for (Bomb bomb : this.bombs) {
                if ((currentTimestamp.getTime() - bomb.getPlacedTimeStamp().getTime()) > 3000) {
                    this.needToUpdateObservers = true;
                    Integer posX = bomb.getPosX();
                    Integer posY = bomb.getPosY();
                    for (int x = -1; x <= 1; x++) {
                        for (int y = -1; y <= 1; y++) {
                            if ((posX + x) >= 0 && (posX + x) <= (field.length - 1) && (posY + y) >= 0 && (posY + y) <= (field.length - 1)) {
                                if ((x == -1 && y == -1) || (x == +1 && y == -1) || (x == -1 && y == +1) || (x == +1 && y == +1)) {
                                    continue;
                                }
                                if (field[posX + x][posY + y] == 51) {
                                    field[posX + x][posY + y] = 50;
                                } else if (field[posX + x][posY + y] == 54) {
                                    for (Player player : this.players) {
                                        if (player.getPosX() == posX + x && player.getPosY() == posY + y) {
                                            player.setDead(true);
                                            this.messageObject.setMessage(player.getName() + " ist tot!");
                                            this.updateObservers(1, this.messageObject);
                                        }
                                    }
                                } else if (field[posX + x][posY + y] == 61 || field[posX + x][posY + y] == 62 || field[posX + x][posY + y] == 63 || field[posX + x][posY + y] == 64) {
                                    int currentPlayer = field[posX + x][posY + y] - 61;
                                    field[posX + x][posY + y] = 50;
                                    this.players[currentPlayer].setDead(true);
                                    this.messageObject.setMessage(this.players[currentPlayer].getName() + " ist tot!");
                                    this.updateObservers(1, this.messageObject);
                                }
                            }
                        }
                    }
                    field[posX][posY] = 50;
                    bombsToRemove.add(bomb);
                }
            }
            this.bombs.removeAll(bombsToRemove);
        }
        this.fieldObject.setData(field);
    }

    /**
     * Add observer to observerlist.
     *
     * @param observer Observer to add.
     */
    private void addObserver(GameObserver observer) {
        this.observers.add(observer);
    }

    /**
     * Remove observer from observerlist.
     *
     * @param observer Observer to remove.
     */
    private void removeObserver(GameObserver observer) {
        this.observers.remove(observer);
    }

    /**
     * Update Observers.
     *
     * @param key  Update-Method. 1 = String, 2 = Field.
     * @param data Update-Data. DataString or DataInteger.
     */
    private void updateObservers(Integer key, Data data) {
        for (GameObserver gameObserver : this.observers) {
            gameObserver.update(key, data);
        }
    }
}
