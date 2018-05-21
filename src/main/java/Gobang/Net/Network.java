package Gobang.Net;

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

    private static final String base_URI = "http://localhost:8080/";
    public  static  Network network=new Network();

    public static  Network getNetwork(){
        if(network==null)
            return  new Network();
        else
            return network;
    }

    public int joinSolo(Player player){
        int res=0;
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
                return 100;
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

             res=uc.getResponseCode();
            //控制台显示，debug或者加日志
            System.out.println("do Json Post(Sign up): getResponseCode " + uc.getResponseCode());


        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
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


    private static String getStrFromUri (String uri) throws IOException {
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

    public String down(int i,int j,int color) throws IOException{
        String result=null;
        try {
            String uri = base_URI + "player/down";
            URL url = new URL(uri);
            HttpURLConnection uc = (HttpURLConnection) url.openConnection();
            uc.setDoInput(true);
            uc.setDoOutput(true);
            uc.setRequestProperty("Charset", "UTF-8");
            uc.setRequestProperty("Content-Type", "application/json");
            uc.setRequestProperty("Accept", "application/json");
            uc.setRequestMethod("POST");
            //参数
            String body = "i="+i+"&j="+j+"&color="+color;


            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(uc.getOutputStream(), "UTF-8"));
            writer.write(body);
            writer.close();

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
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

}
