package com.bjtu.gobang.gobang.Service;

import com.bjtu.gobang.gobang.Enities.GobangMap;
import com.bjtu.gobang.gobang.Enities.Player;
import com.google.gson.JsonObject;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PlayerService {
    public GobangMap addUser(Player player);
    public List<Player> getUsers();
    public GobangMap down(int x, int y,int color,int tag,int count);
    public GobangMap getMap(int indx);
    public GobangMap getMapAfterDown(int indx);
    public void quit(int tag,int isQiutSelf);
}
