package api;

import api.data.Data;

public class Player implements GameObserver {

    private String name;
    private SocketHandler socket;
    private Integer posX;
    private Integer posY;
    private String color;
    private Boolean isDead = false;
    private Boolean isReady = false;

    public Player(SocketHandler socket) {
        this.socket = socket;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPosX() {
        return posX;
    }

    public void setPosX(Integer posX) {
        this.posX = posX;
    }

    public Integer getPosY() {
        return posY;
    }

    public void setPosY(Integer posY) {
        this.posY = posY;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Boolean getDead() {
        return isDead;
    }

    public void setDead(Boolean dead) {
        isDead = dead;
    }

    public Boolean getReady() {
        return isReady;
    }

    public void setReady(Boolean ready) {
        isReady = ready;
    }

    public Data getInput() {
        try {
            return this.socket.receive();
        } catch (Exception e) {
            this.disconnect();
            return null;
        }
    }

    public Data getInputForce() {
        try {
            return this.socket.receiveForce();
        } catch (Exception e) {
            this.disconnect();
            return null;
        }
    }

    public void disconnect() {
        this.socket.closeConnections();
    }

    public boolean isConnected() {
        return this.socket.isConnected();
    }

    @Override
    public void update(Integer key, Data data) {
        if (this.isConnected()) {
            this.socket.send(key, data.getData());
        }
    }
}
