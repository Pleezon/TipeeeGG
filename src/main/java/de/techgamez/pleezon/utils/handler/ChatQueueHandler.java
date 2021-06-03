package de.techgamez.pleezon.utils.handler;

import net.labymod.main.LabyMod;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ChatQueueHandler {
    private int tickDelay;
    private int current_count = 0;
    private LinkedHashMap<String,Thread> queue = new LinkedHashMap<>(); //hashmap so people spamming only get one answer; if they lose their money it's their bad.
    public ChatQueueHandler(double delayInSeconds){
        this.tickDelay = (int) (delayInSeconds * 20);
    }

    public void add(String s){
        this.queue.put(s,null);
    }
    public void add(String s, Thread r){
        queue.put(s,r);
    }

    @SubscribeEvent
    public void onTick( TickEvent.ClientTickEvent event ) {
        if(tickDelay<=current_count){
            if(!queue.isEmpty() && Minecraft.getMinecraft().theWorld!=null){
                Map.Entry<String,Thread> entry = queue.entrySet().iterator().next();
                String key = entry.getKey();
                Thread value = entry.getValue();
                Minecraft.getMinecraft().thePlayer.sendChatMessage(key);
                if(value!=null){
                    value.start();
                }
                queue.remove(key);
                current_count=0;
            }
        }else{
            current_count++;
        }
    }
    public void addPayment(String playerName, double amount){
        if(amount==0)return;
        if(playerName==null)return;
        if(playerName.equals(""))return;
        add("/pay "+playerName+" "+amount);
    }
    public void addMessage(String playerName, String message){
        if(message==null)return;
        if(message.equals(""))return;
        if(playerName==null)return;
        if(playerName.equals(""))return;
        add("/msg " + playerName + " " + message);
    }




}
