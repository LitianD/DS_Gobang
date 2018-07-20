package com.bjtu.gobang.gobang.Controllers;


import com.bjtu.gobang.gobang.Enities.DownTemp;
import com.bjtu.gobang.gobang.Enities.GobangMap;
import com.bjtu.gobang.gobang.Enities.Player;
import com.bjtu.gobang.gobang.ServiceImp.PlayerServiceImp;

import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.sound.midi.SysexMessage;
import java.util.List;

@RestController
public class PlayerController {
    @Autowired
    PlayerServiceImp ser;


    @PostMapping("/player/addPlayer")
    public GobangMap addUser(@RequestBody Player player){

        System.out.println(player.getUsername());
        GobangMap res=ser.addUser(player);
       return res;
    }

    @RequestMapping(value = "/players",method = RequestMethod.GET)
    public List<Player> getUsers(){
        System.out.println("in");
        return ser.getUsers();
    }

    @PostMapping("/players/down")
    public GobangMap down(@RequestBody DownTemp downTemp){
        System.out.println("down");
        GobangMap res=ser.down(downTemp.getI(),downTemp.getJ(),downTemp.getColor(),downTemp.getTag(),downTemp.getCount());

        return res;
    }

    @GetMapping("player/{index}")
    public GobangMap getMapByIndex(@PathVariable("index") int index){
        GobangMap temp=ser.getMap(index);
        return temp;
    }

    @GetMapping("afterDown/{index}")
    public GobangMap getMapAfterDown(@PathVariable("index") int index){
        GobangMap temp=ser.getMap(index);
        return temp;
    }

    @PostMapping("player/waitQuit")
    public void quit(@RequestBody Player player){
        if(player.getFlag()==3)
        {
            ser.quit(player.tag,1);
        }
        else {
            ser.quit(player.tag,0);
        }

    }

}
