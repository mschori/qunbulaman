package server;

import api.data.Data;

public interface GameObserver {

    void update(Integer key, Data payload);
}
