package de.techgamez.pleezon.utils.handler;

import de.techgamez.pleezon.TipeeeGG;
import de.techgamez.pleezon.utils.misc.Donation;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.concurrent.LinkedBlockingDeque;

public class DonationQueueHandler {
    private int current_count = 0;
    private LinkedBlockingDeque<Donation> queue = new LinkedBlockingDeque<>();
    public DonationQueueHandler(){

    }

    public void add(Donation d){
        if(d.getDelayInTicks()==0){
            APIHandler.sendDonation(TipeeeGG.apiName.getValue(),TipeeeGG.apiKey.getValue(),d);
        }else{
            this.queue.add(d);
        }
    }

    @SubscribeEvent
    public synchronized void onTick( TickEvent.ClientTickEvent event ) {
        if(!queue.isEmpty() && queue.getFirst().getDelayInTicks()<=current_count){
            Donation d = queue.removeFirst();
            APIHandler.sendDonation(TipeeeGG.apiName.getValue(),TipeeeGG.apiKey.getValue(),d);
            current_count=0;
        }else{
            current_count++;
        }
    }
}
