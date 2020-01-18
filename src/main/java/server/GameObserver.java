package server;

import api.data.Data;

public interface GameObserver {

    void update(Data payload);
}
