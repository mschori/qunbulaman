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

    private final Integer maxAlivePlayers = 0;
    private Player[] players;
    private List<GameObserver> observers = new ArrayList<>();
    private List<Bomb> bombs = new ArrayList<>();
    private DataInteger field = new DataInteger();
    private DataString message = new DataString();
    private Boolean needToUpdateObservers = false;
    private Integer playersAlive;
    private Integer activePlayers;

    public Game(Player[] players, Integer[][] field) {
        this.players = players;
        this.playersAlive = players.length;
        this.activePlayers = players.length;
        this.field.setData(field);

        // Set player-positions
        this.setPositions();

        // Add players to observers
        for (Player player : players) {
            this.addObserver(player);
        }
    }

    private void setPositions() {

        Integer[][] field = this.field.getData();

        switch (this.players.length) {
            case 1:
                field[0][0] = 61;
                this.players[0].setPosX(0);
                this.players[0].setPosY(0);
                break;
            case 2:
                field[0][0] = 61;
                field[0][field.length] = 62;
                this.players[0].setPosX(0);
                this.players[0].setPosY(0);
                this.players[1].setPosX(0);
                this.players[1].setPosY(field.length);
                break;
            case 3:
                field[0][0] = 61;
                field[0][field.length] = 62;
                field[field.length][0] = 63;
                this.players[0].setPosX(0);
                this.players[0].setPosY(0);
                this.players[1].setPosX(0);
                this.players[1].setPosY(field.length);
                this.players[2].setPosX(field.length);
                this.players[2].setPosY(0);
                break;
            case 4:
                field[0][0] = 61;
                field[0][field.length] = 62;
                field[field.length][0] = 63;
                field[field.length][field.length] = 64;
                this.players[0].setPosX(0);
                this.players[0].setPosY(0);
                this.players[1].setPosX(0);
                this.players[1].setPosY(field.length);
                this.players[2].setPosX(field.length);
                this.players[2].setPosY(0);
                this.players[3].setPosX(field.length);
                this.players[3].setPosY(field.length);
                break;
            default:
                break;
        }

        this.field.setData(field);
    }

    public void run() {

        System.out.println("Field initialised. Sending everyone the field...");
        this.updateObservers(2, this.field);

        System.out.println("Check if players are ready...");
        this.checkIfPlayersReady();

        this.message.setMessage("Das Spiel beginnt in 3 Sekunden.");
        this.updateObservers(1, this.message);

        while (this.playersAlive > this.maxAlivePlayers) {

            this.needToUpdateObservers = false;

            for (int x = 0; x < this.players.length; x++) {

                if (this.players[x].getDead()) {
                    continue;
                }

                Data action = null;

                action = this.players[x].getInput();


                if (action instanceof DataString) {
                    this.calculatePlayerAction(x, ((DataString) action).getData());
                    this.calculateBomb();
                }
            }

            if (this.needToUpdateObservers) {
                this.updateObservers(2, this.field);
            }
        }

        this.endGame();
    }

    private void checkDisconnects() {
        for (Player player : this.players) {
            if (!player.isConnected()) {
                player.setDead(true);
                player.setReady(true);
                player.disconnect();
            }
        }
    }

    private void checkIfPlayersReady() {

        for (Player player : this.players) {
            this.message.setMessage("Are you ready?");
            player.update(1, this.message);
        }

        int readyPlayers = 0;

        while (readyPlayers < this.players.length) {

            this.checkDisconnects();

            readyPlayers = 0;

            for (Player player : this.players) {
                if (player.getReady()) {
                    readyPlayers++;
                    continue;
                }

                Data action = null;
                action = player.getInput();

                if (action instanceof DataString) {
                    String input = ((DataString) action).getData();
                    System.out.println("From " + player.getName() + ": " + input);
                    if (input.equals("Ready!")) {
                        player.setReady(true);
                        this.message.setMessage(player.getName() + " is ready.");
                        this.updateObservers(1, this.message);
                    } else {
                        System.out.println("Ready erwartet aber folgendes erhalten: " + input);
                    }
                }
            }
        }
        this.message.setMessage("Everyone is ready!");
        this.updateObservers(1, this.message);
    }

    private void endGame() {

        for (Player player : this.players) {
            if (player.getDead()) {
                this.message.setMessage("You lose!");
                player.update(1, this.message);
            } else {
                this.message.setMessage("You win!");
                player.update(1, this.message);
            }
        }
    }

    private void calculatePlayerAction(Integer playerNumber, String action) {

        Integer posX = this.players[playerNumber].getPosX();
        Integer posY = this.players[playerNumber].getPosY();
        Integer[][] field = this.field.getData();

        switch (action) {
            case "up":
                if (posY > 0) {
                    if (field[posX][posY - 1] == 50 || field[posX][posY - 1] == 53) {
                        if (field[posX][posY] != 53) {
                            field[posX][posY] = 50;
                        }
                        field[posX][posY - 1] = 60 + (playerNumber + 1);
                        this.players[playerNumber].setPosY(posY - 1);
                    }
                }
                this.needToUpdateObservers = true;
                break;
            case "down":
                if (posY + 1 < field.length) {
                    if (field[posX][posY + 1] == 50 || field[posX][posY - 1] == 53) {
                        if (field[posX][posY] != 53) {
                            field[posX][posY] = 50;
                        }
                        field[posX][posY + 1] = 60 + (playerNumber + 1);
                        this.players[playerNumber].setPosY(posY + 1);
                    }
                }
                this.needToUpdateObservers = true;
                break;
            case "left":
                if (posX > 0) {
                    if (field[posX - 1][posY] == 50 || field[posX - 1][posY] == 53) {
                        if (field[posX][posY] != 53) {
                            field[posX][posY] = 50;
                        }
                        field[posX - 1][posY] = 60 + (playerNumber + 1);
                        this.players[playerNumber].setPosX(posX - 1);
                    }
                }
                this.needToUpdateObservers = true;
                break;
            case "right":
                if (posX + 1 < field.length) {
                    if (field[posX + 1][posY] == 50 || field[posX + 1][posY] == 53) {
                        if (field[posX][posY] != 53) {
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
                field[posX][posY] = 53;
                this.needToUpdateObservers = true;
                break;
            case "disconnect":
                this.removeObserver(this.players[playerNumber]);
                break;
            default:
                break;
        }

        this.field.setData(field);
    }

    private void calculateBomb() {
        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
        Integer[][] field = this.field.getData();

        if (!this.bombs.isEmpty()) {
            for (Bomb bomb : this.bombs) {
                if ((currentTimestamp.getTime() - bomb.getPlacedTimeStamp().getTime()) > 3000) {
                    Integer posX = bomb.getPosX();
                    Integer posY = bomb.getPosY();
                    for (int x = -1; x < posX + 1; x++) {
                        for (int y = -1; y < posY + 1; y++) {
                            if ((posX + x) >= 0 && (posX + x) <= field.length && (posY + y) >= 0 && (posY + y) <= field.length) {
                                if (field[posX + x][posY + y] == 51) {
                                    field[posX + x][posY + y] = 50;
                                } else if (field[posX + x][posY + y] == 61) {
                                    field[posX + x][posY + y] = 50;
                                    this.players[0].setDead(true);
                                    this.playersAlive--;
                                } else if (field[posX + x][posY + y] == 62) {
                                    field[posX + x][posY + y] = 50;
                                    this.players[1].setDead(true);
                                    this.playersAlive--;
                                } else if (field[posX + x][posY + y] == 63) {
                                    field[posX + x][posY + y] = 50;
                                    this.players[2].setDead(true);
                                    this.playersAlive--;
                                } else if (field[posX + x][posY + y] == 64) {
                                    field[posX + x][posY + y] = 50;
                                    this.players[3].setDead(true);
                                    this.playersAlive--;
                                }
                            }
                        }
                    }
                }
            }

            this.needToUpdateObservers = true;
        }

        this.field.setData(field);
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
