package Gobang.Net;

import Gobang.Entity.DownTemp;
import Gobang.Entity.GobangMap;
import Gobang.Entity.Player;
import Gobang.Util.JsonChangeStrUtil;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

public class Network {

    public static final String base_URI = "http://localhost:8080/";
    public  static  Network network=new Network();

    public static  Network getNetwork(){
        if(network==null)
            return  new Network();
        else
            return network;
    }

    public GobangMap joinSolo(Player player){
        String result=null;
        Gson gson = new Gson();
        GobangMap gobangMap=null;
        try {
            String uri = base_URI + "player/addPlayer";
            URL url = new URL(uri);
            HttpURLConnection uc = (HttpURLConnection) url.openConnection();
            uc.setDoInput(true);
            uc.setDoOutput(true);
            uc.setRequestProperty("Charset", "UTF-8");
            uc.setRequestProperty("Content-Type", "application/json");
            uc.setRequestProperty("Accept", "application/json");
            uc.setRequestMethod("POST");

            String json = JsonChangeStrUtil.convertJavaBeanToJson(player);

            if (json == null) {
                return null;
            }

            if (json.length() != 0) {
                byte[] writebytes = json.getBytes();
                //设置文件长度
                uc.setRequestProperty("Content-Length", String.valueOf(writebytes.length));
            }
            OutputStream os = uc.getOutputStream();
            os.write(json.getBytes());
            os.flush();
            os.close();

            InputStream inputStream = uc.getInputStream();
            InputStream buffer = new BufferedInputStream(inputStream);
            Reader r = new InputStreamReader(buffer);
            int c;
            StringBuilder bodys = new StringBuilder();
            while ((c = r.read()) != -1) {
                bodys.append((char) c);
            }
            result=bodys.toString();
            JsonParser parser = new JsonParser();
            JsonObject obj=parser.parse(result).getAsJsonObject();
            gobangMap=gson.fromJson(obj,GobangMap.class);

            //控制台显示，debug或者加日志
            System.out.println("do Json Post(Sign up): getResponseCode " + uc.getResponseCode());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return gobangMap;
    }

    public List<Player> getUsers() throws IOException{
        Gson gson = new Gson();
        String data = getStrFromUri(base_URI + "players");
        JsonParser parser = new JsonParser();
        JsonArray player=parser.parse(data).getAsJsonArray();
        System.out.println(player);
        Player[] ui = gson.fromJson(player, Player[].class);
        return Arrays.asList(ui);
    }


    public synchronized static String getStrFromUri (String uri) throws IOException {
        URL url = new URL(uri);
        HttpURLConnection uc = (HttpURLConnection) url.openConnection();
        uc.setDoInput(true);
        uc.setDoOutput(true);
        uc.setRequestProperty("Charset", "UTF-8");
        uc.setRequestProperty("Content-Type", "application/json");
        uc.setRequestProperty("Accept", "application/json");
        InputStream raw = uc.getInputStream();
        InputStream buffer = new BufferedInputStream(raw);
        Reader r = new InputStreamReader(buffer);
        int c;
        StringBuilder body = new StringBuilder();
        while ((c = r.read()) != -1) {
            body.append((char) c);
        }
        return body.toString();
    }

    //下子
    public GobangMap down(int i,int j,int color,int tag,int count) throws IOException{
        String result=null;
        Gson gson = new Gson();
        GobangMap gobangMap=null;
        DownTemp downTemp=new DownTemp(i,j,color,tag,count);
        try {
            String uri = base_URI + "players/down";
            URL url = new URL(uri);
            HttpURLConnection uc = (HttpURLConnection) url.openConnection();
            uc.setDoInput(true);
            uc.setDoOutput(true);
            uc.setRequestProperty("Charset", "UTF-8");
            uc.setRequestProperty("Content-Type", "application/json");
            uc.setRequestProperty("Accept", "application/json");
            uc.setRequestMethod("POST");

            String json = JsonChangeStrUtil.convertJavaBeanToJson(downTemp);

            if (json == null) {
                return null;
            }

            if (json.length() != 0) {
                byte[] writebytes = json.getBytes();
                //设置文件长度
                uc.setRequestProperty("Content-Length", String.valueOf(writebytes.length));
            }
            OutputStream os = uc.getOutputStream();
            os.write(json.getBytes());
            os.flush();
            os.close();

            //获取返回
            InputStream inputStream = uc.getInputStream();
            InputStream buffer = new BufferedInputStream(inputStream);
            Reader r = new InputStreamReader(buffer);
            int c;
            StringBuilder bodys = new StringBuilder();
            while ((c = r.read()) != -1) {
                bodys.append((char) c);
            }
            result=bodys.toString();

            JsonParser parser = new JsonParser();
            JsonObject obj=parser.parse(result).getAsJsonObject();
            gobangMap=gson.fromJson(obj,GobangMap.class);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return gobangMap;
    }

    //网络对战位成功服务端wait--   tag是桌号 ，isQiutSelf为0时表示不是逃跑，1表示是逃跑
    public void  quit(int tag,int isQiutSelf){
        try {
            String uri = base_URI + "player/waitQuit";
            URL url = new URL(uri);
            HttpURLConnection uc = (HttpURLConnection) url.openConnection();
            uc.setDoInput(true);
            uc.setDoOutput(true);
            uc.setRequestProperty("Charset", "UTF-8");
            uc.setRequestProperty("Content-Type", "application/json");
            uc.setRequestProperty("Accept", "application/json");
            uc.setRequestMethod("POST");

            Player player=new Player("quit");
            if(isQiutSelf==1&&tag!=-1) {
                player.setFlag(3);
                System.out.println("自己主动退出游戏");
            }
            if(isQiutSelf==0&&tag==-1)
            {
                System.out.println("匹配时取消匹配");
            }
            if(isQiutSelf==0&&tag!=-1)
            {
                System.out.println("得到对手逃跑消息后发送自己离开消息");
            }
                player.tag=tag;

            System.out.println("发送给服务器的Player信息为: tag: "+player.tag+"isQiutSelf: "+player.getFlag());
            String json = JsonChangeStrUtil.convertJavaBeanToJson(player);

            if (json.length() != 0) {
                byte[] writebytes = json.getBytes();
                //设置文件长度
                uc.setRequestProperty("Content-Length", String.valueOf(writebytes.length));
            }
            OutputStream os = uc.getOutputStream();
            os.write(json.getBytes());
            os.flush();
            os.close();

            //控制台显示，debug或者加日志
            System.out.println("do Json Post(Sign up): getResponseCode " + uc.getResponseCode());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
