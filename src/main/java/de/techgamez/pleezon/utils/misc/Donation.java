package de.techgamez.pleezon.utils.misc;

import com.sun.org.apache.xpath.internal.operations.Bool;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class Donation {
    private double amountEUR;
    private String donoName;
    private String donoMessage;
    private String playerNameFrom;
    private double rawAmount;
    private int delayInTicks;
    Consumer<Boolean> callback;

    public Donation(double amountEUR, String donoName, String donoMessage, String playerNameFrom, double rawAmount, int delayInTicks, Consumer<Boolean> callback){
        this.amountEUR = Math.round(amountEUR*100)/100D;
        this.donoName = donoName;
        this.donoMessage = donoMessage;
        this.playerNameFrom = playerNameFrom;
        this.rawAmount = rawAmount;
        this.delayInTicks = delayInTicks;
        this.callback = callback;
    }

    public void onDonated(Boolean success){
        if(this.callback!=null){
            callback.accept(success);
        }
    }

    public int getDelayInTicks() {
        return delayInTicks;
    }

    public double getAmountEUR() {
        return amountEUR;
    }

    public String getDonoName() {
        return donoName;
    }

    public String getDonoMessage() {
        return donoMessage;
    }

    public String getPlayerNameFrom() {
        return playerNameFrom;
    }

    @Override
    public String toString() {
        return "Donation{" +
                "amountEUR=" + amountEUR +
                ", donoName='" + donoName + '\'' +
                ", donoMessage='" + donoMessage + '\'' +
                ", playerNameFrom='" + playerNameFrom + '\'' +
                ", rawAmount=" + rawAmount +
                ", delayInTicks=" + delayInTicks +
                ", callback=" + (callback!=null) +
                '}';
    }

    public double getRawAmount() {
        return rawAmount;
    }
}
