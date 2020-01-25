package client;

public class Activity {

    private boolean loadAsync, loaded;

    public final void load(){
        loaded = false;
        if(loadAsync){
            onLoad();
            loaded = true;
        }
    }

    private void onLoad() {

    }

    private boolean isLoaded(){
        return loaded;
    }

}

