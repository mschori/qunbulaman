package api;


import api.data.Data;
import server.GameObserver;

public class Player implements GameObserver {

    private String name;
    private SocketHandler socket;
    private Integer posX;
    private Integer posY;
    private String color;

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

    @Override
    public void update(Data data) {
        this.socket.send(1, data.getData());
    }
}
