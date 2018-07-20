package Gobang.Thread;

import Gobang.Entity.GobangMap;
import Gobang.Main;
import Gobang.Net.Network;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class PlayThread extends Thread{
    int tag;
    private GobangMap gobangMap;
    public Main main;
    public PlayThread(int tag,Main main){
        this.tag=tag;
        this.main=main;
    }
    public void setMap(Main main){
        this.main=main;
    }
    public void run(){
        try {
            Thread.sleep(1000);
            Gson gson = new Gson();
            String data = Network.getNetwork().getStrFromUri(Network.base_URI + "player/" + tag);
            JsonParser parser = new JsonParser();
            JsonObject temp = parser.parse(data).getAsJsonObject();
            gobangMap = gson.fromJson(temp, GobangMap.class);
            main.setCurrentGobangMap(gobangMap);
            Thread.sleep(500);
            //main.reflushMapUI();
            //fresh
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
