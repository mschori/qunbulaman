package api;

import backend.SocketHandler;


public class Player {

    private String name;
    public SocketHandler socket;

    public Player(SocketHandler socket) {
        this.socket = socket;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
