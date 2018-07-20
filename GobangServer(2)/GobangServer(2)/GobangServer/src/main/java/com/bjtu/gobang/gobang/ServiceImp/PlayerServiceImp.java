package com.bjtu.gobang.gobang.ServiceImp;

import com.bjtu.gobang.gobang.Enities.GobangMap;
import com.bjtu.gobang.gobang.Enities.Player;
import com.bjtu.gobang.gobang.Service.PlayerService;
import com.google.gson.JsonObject;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class PlayerServiceImp implements PlayerService {
    public static List<Player> players=new ArrayList<>();
    public static List<GobangMap> gobangMaps=new ArrayList<>();
    public static int tag=1;

    static int wait=0;

    @Override
    public GobangMap addUser(Player player){
//        String name=player.getUsername();
        player.setFlag(0);
//        for(Player player1:players){
//            if(player1.getUsername().equals(name))
//                return false;
//        }
        players.add(player);
        wait++;

        GobangMap gobangMap=null;
        if(wait%2==1){
            gobangMap=new GobangMap();
            gobangMap.setTag(tag);
            gobangMaps.add(gobangMap);
            player.setBlackOrWhite(1);
            gobangMap.setP1(player);
            player.setFlag(1);
        }
        else {
            gobangMap=gobangMaps.get(tag-1);
            player.setBlackOrWhite(0);
            gobangMap.setP2(player);
            player.setFlag(1);
            //第二人进入就开始游戏
            gobangMap.setOverFlag(1);
            tag++;
        }
        if(wait%2==0){
            //传输给客户端
            wait=wait-2;
            System.out.println(wait+"人在等待");
            System.out.println("桌号为"+(tag-1));
            System.out.println("本桌状态为"+gobangMap.getOverFlag());
            return gobangMap;
        }
        System.out.println(wait+"人在等待");
        System.out.println("桌号为"+tag);
        System.out.println("本桌状态为"+gobangMap.getOverFlag());
        return gobangMap;
    }

    @Override
    public GobangMap down(int x, int y,int color,int tag,int count){
        GobangMap gobangMap=gobangMaps.get(tag-1);

        if(gobangMap.getLastPlayer()!=color) {
            gobangMap.map[x][y].setPut_number(count);
            gobangMap.map[x][y].setI(x);
            gobangMap.map[x][y].setJ(y);
            gobangMap.map[x][y].setWhiteOrBlack(color);
            gobangMap.setLastPlayer(color);
            boolean res = gobangMaps.get(tag-1).judge(x, y);

            if (res) {
                gobangMap.setOverFlag(2);
            }
        }

        return gobangMap;
    }

    @Override
    public List<Player> getUsers(){
        return players;
    }

    @Override
    public GobangMap getMap(int indx){
        try{
            Thread.sleep(500);
        }catch(Exception e ){
            e.printStackTrace();
        }
        return gobangMaps.get(indx-1);
    }

    @Override
    public GobangMap getMapAfterDown(int indx){
        return gobangMaps.get(indx-1);
    }

    @Override
    public void quit(int tag,int isQiutSelf){
        if(tag!=-1&&isQiutSelf==1) {
            int index=tag-1;
            gobangMaps.get(index).setOverFlag(3);
            System.out.println("有玩家逃跑,将桌号为"+tag+"设为有人逃跑");
            System.out.println("验证：flag现在为 "+gobangMaps.get(index).getOverFlag());
        }
        else if(tag!=-1&&isQiutSelf==0)
        {
            if(tag>=1)
            {
          //      gobangMaps.remove(tag-1);
                System.out.println("删除了棋盘： "+tag);
            }
        }
        else
        {
            System.out.println("在匹配的时候取消游戏");
        }
        if(wait>0) {
            wait--;
        }
        System.out.println("quit 当前等待人数为"+wait);
    }

}
