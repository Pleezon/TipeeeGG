package de.techgamez.pleezon.utils.handler;

import de.techgamez.pleezon.TipeeeGG;
import net.labymod.utils.Consumer;
import net.minecraft.network.play.server.S38PacketPlayerListItem;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiConsumer;

public class RealNameHandler implements Consumer<Object> {
    private static HashMap<String, String> names = new HashMap<>();
    public static String getIdFromDisplayName(String displayName){
        for(Map.Entry<String,String> entry : names.entrySet()){
            if(entry.getValue().equals(displayName)){
                return entry.getKey();
            }
        }
        return null;
    }
    public static void getRealName(String displayname, BiConsumer<String,Boolean> callback){
        new Thread(()->{
            String uuid = getIdFromDisplayName(displayname);
            if (uuid != null) {
                callback.accept(APIHandler.getName(uuid), TipeeeGG.blockedUsers.containsKey(uuid));
            } else {
                callback.accept(null,false);
            }

        }).start();

    }

    @Override
    public void accept(Object packet) {
        try{
            if(packet instanceof S38PacketPlayerListItem){
                S38PacketPlayerListItem p = (S38PacketPlayerListItem) packet;
                if(p.func_179768_b().equals(S38PacketPlayerListItem.Action.UPDATE_DISPLAY_NAME)){
                    p.func_179767_a().forEach(d->{
                        String name = d.getDisplayName().getUnformattedText().split(" ")[2];
                        names.put(d.getProfile().getId().toString().replaceAll("-",""),name);
                    });
                }else if(p.func_179768_b().equals(S38PacketPlayerListItem.Action.ADD_PLAYER)){
                    p.func_179767_a().forEach((d)->{
                        if(!d.getProfile().getId().toString().endsWith("0000-000000000000")){
                            if(d.getDisplayName()!=null){
                                String name = d.getDisplayName().getUnformattedText().split(" ")[2];
                                names.put(d.getProfile().getId().toString().replaceAll("-",""),name);
                            }
                        }
                    });
                }
            }
        }catch (Exception|Error e){ /* not ignoring this may lead to console spam*/}
    }
}
