package de.techgamez.pleezon.utils.handler;

import de.techgamez.pleezon.TipeeeGG;
import de.techgamez.pleezon.utils.misc.Donation;
import net.labymod.api.LabyModAddon;
import net.labymod.api.events.MessageSendEvent;
import net.labymod.main.LabyMod;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.API;

import java.util.Map;

public class CommandsHandler implements MessageSendEvent {


    @Override
    public boolean onSend(String s) {
        if (s.startsWith("!block ")) {
            String name = s.replace("!block ", "");
            String UUID = RealNameHandler.getIdFromDisplayName(name);
            if (UUID != null) {
                displayInChat("blocked " + name + " with UUID " + UUID);
                TipeeeGG.blockedUsers.put(UUID, name);
            }
        } else if (s.equals("!list")) {
            displayInChat("Blocked-List:");
            if (TipeeeGG.blockedUsers.getMap().isEmpty()) {
                displayInChat("empty");
            }
            TipeeeGG.blockedUsers.getMap().forEach((key, value) -> {
                LabyMod.getInstance().displayMessageInChat("§4§l" + value);
            });
        } else if (s.startsWith("!remove ")) {
            String name = s.replace("!remove ", "");
            for (Map.Entry<String, String> entry : TipeeeGG.blockedUsers.getMap().entrySet()) {
                if (entry.getValue().equals(name) || entry.getKey().equals(name)) {
                    TipeeeGG.blockedUsers.remove(entry.getKey());
                    displayInChat("removed " + name);
                }
            }
        }

        return s.startsWith("!");
    }


    private static void displayInChat(String message) {
        LabyMod.getInstance().displayMessageInChat("§a§l[TipeeeGG:]§r§f " + message);

    }
}
