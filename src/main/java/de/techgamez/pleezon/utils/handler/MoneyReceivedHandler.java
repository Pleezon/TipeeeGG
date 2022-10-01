package de.techgamez.pleezon.utils.handler;

import de.techgamez.pleezon.TipeeeGG;
import de.techgamez.pleezon.utils.misc.Donation;
import net.labymod.main.LabyMod;

public class MoneyReceivedHandler {
    public static void onMoneyReceived(double amount, String playername){
        RealNameHandler.getRealName(playername,(name, isBlocked)->{

            if(name == null){
                TipeeeGG.chatQueueHandler.addMessage(playername,parseMessage(TipeeeGG.errorMessage.getValue(),playername,amount));
                TipeeeGG.chatQueueHandler.addPayment(playername,amount);
                return;
            }
            if(isBlocked) {
                TipeeeGG.chatQueueHandler.addMessage(name,parseMessage(TipeeeGG.blockedMessage.getValue(),name,amount));
                TipeeeGG.chatQueueHandler.addPayment(name,amount);
                return;
            }

            double amountEUR = Math.round((amount /TipeeeGG.conversionRate.getValue()) * 100) / 100.0;
            String donoName = parseMessage(TipeeeGG.donoName.getValue(),name,amount);
            String donoMessage = parseMessage(TipeeeGG.donoMessage.getValue(),name,amount);
            TipeeeGG.donationQueueHandler.add(new Donation(amountEUR,donoName,donoMessage,name,amount,amountEUR<1?20:0,(success)->{
                if(success){
                    TipeeeGG.chatQueueHandler.addMessage(playername,parseMessage(TipeeeGG.thanksMessage.getValue(),name,amount));
                }else {
                    TipeeeGG.chatQueueHandler.addMessage(playername,parseMessage(TipeeeGG.errorMessage.getValue(),name,amount));
                    TipeeeGG.chatQueueHandler.addPayment(playername,amount);
                }
            }));
        });
    }

    private static String parseMessage(String message, String name, double amount){
        if(message==null||message.equals(""))return "";
        return message.replaceAll("%n%",name).replaceAll("%a%",Integer.toString((int)amount));
    }



}