package api;

import java.sql.Timestamp;

public class Bomb {

    private Integer posX;
    private Integer posY;
    private Timestamp placedTimeStamp;

    public Bomb(Integer posX, Integer posY) {
        this.posX = posX;
        this.posY = posY;
        this.placedTimeStamp = new Timestamp(System.currentTimeMillis());
    }

    public Integer getPosX() {
        return posX;
    }

    public Integer getPosY() {
        return posY;
    }

    public Timestamp getPlacedTimeStamp() {
        return placedTimeStamp;
    }
}
