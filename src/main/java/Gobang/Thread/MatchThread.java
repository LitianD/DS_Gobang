package Gobang.Thread;

import Gobang.Entity.GobangMap;
import Gobang.Entity.Player;
import Gobang.Net.Network;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.Arrays;

public class MatchThread extends Thread{
    int tag;
    private GobangMap gobangMap;
    boolean start=false;
    public GobangMap getGobangMap() {
        return gobangMap;
    }

    public MatchThread(int tag){
        this.tag=tag;
    }

    public void run(){
        try {
            while(!start) {
                Gson gson = new Gson();
                String data = Network.getNetwork().getStrFromUri(Network.base_URI + "players/" + tag);
                JsonParser parser = new JsonParser();
                JsonObject temp = parser.parse(data).getAsJsonObject();
                gobangMap = gson.fromJson(temp, GobangMap.class);
                Thread.sleep(1000);
                if(gobangMap.getOverFlag()==1) {
                    start = true;

                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
