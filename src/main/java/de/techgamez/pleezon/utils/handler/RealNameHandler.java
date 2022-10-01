package de.techgamez.pleezon.utils.handler;

import de.techgamez.pleezon.TipeeeGG;
import net.labymod.main.LabyMod;
import net.labymod.utils.Consumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.network.play.server.S38PacketPlayerListItem;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiConsumer;

public class RealNameHandler {

    public static String getIdFromDisplayName(String nickedUserName) {
        String uuid = null;
        Collection<NetworkPlayerInfo> infoMap = Minecraft.getMinecraft().getNetHandler().getPlayerInfoMap();
        for (final NetworkPlayerInfo networkplayerinfo : infoMap) {
            if(networkplayerinfo.getDisplayName().getUnformattedText().split(" ")[2].equals(nickedUserName)) {
                uuid = networkplayerinfo.getGameProfile().getId().toString();
            }
        }
        return uuid;
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
}
