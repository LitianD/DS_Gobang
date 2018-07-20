package Gobang.Thread;

import Gobang.Entity.GobangMap;
import Gobang.Entity.Player;
import Gobang.Main;
import Gobang.Net.Network;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.scene.control.Alert;

import java.util.Arrays;

public class MatchThread extends Thread{
    int tag;
    public Main main;
    private GobangMap gobangMap;
    boolean start=false;
    public GobangMap getGobangMap() {
        return gobangMap;
    }
    public MatchThread(int tag){
        this.tag=tag;
    }
    public void setMap(Main main){
        this.main=main;
    }

    public void run(){
        try {
                //Thread.sleep(500);
                Gson gson = new Gson();
                String data = Network.getNetwork().getStrFromUri(Network.base_URI + "player/" + tag);
                JsonParser parser = new JsonParser();
                JsonObject temp = parser.parse(data).getAsJsonObject();
                gobangMap = gson.fromJson(temp, GobangMap.class);
                main.setCurrentGobangMap(gobangMap);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
