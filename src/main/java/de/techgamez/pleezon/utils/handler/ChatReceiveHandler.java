package de.techgamez.pleezon.utils.handler;

import de.techgamez.pleezon.TipeeeGG;
import net.labymod.api.events.MessageReceiveEvent;

import java.util.regex.Pattern;

public class ChatReceiveHandler implements MessageReceiveEvent {
    public final Pattern paymentValid = Pattern.compile(".+┃ §r[§0-9a-zA-Z!~]+§r §r§ahat dir \\$[0-9.,]+ gegeben.§r");
    @Override
    public boolean onReceive(String raw, String text) {
        if(!TipeeeGG.isEnabled)return false;
        if (paymentValid.matcher(raw).find() && !text.contains("mir")) {
            String name = text.split(" ")[2];
            double amount = Double.parseDouble(text.split(" ")[5].replace("$", "").replaceAll(",", ""));
            MoneyReceivedHandler.onMoneyReceived(amount,name);
        }
        return false;
    }
}
