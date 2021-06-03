package de.techgamez.pleezon;

import de.techgamez.pleezon.config.*;
import de.techgamez.pleezon.utils.gui.PasswordElement;
import de.techgamez.pleezon.utils.handler.*;
import net.labymod.api.LabyModAddon;
import net.labymod.api.events.MessageReceiveEvent;
import net.labymod.settings.elements.*;
import net.labymod.utils.Material;
import net.minecraft.network.Packet;

import java.util.HashMap;
import java.util.List;

public class TipeeeGG extends LabyModAddon {


    public static ChatQueueHandler chatQueueHandler = new ChatQueueHandler(3.0);
    public static DonationQueueHandler donationQueueHandler = new DonationQueueHandler();
    public static boolean isEnabled = false;
    public static ConfigHashMap blockedUsers = new ConfigHashMap("blockedUsers",new HashMap<>()); //key: UUID, value: name

    public static ConfigInt conversionRate = new ConfigInt("conversionRate",100);

    public static ConfigString errorMessage = new ConfigString("errorMessage","");
    public static ConfigString thanksMessage = new ConfigString("thanksMessage","");
    public static ConfigString blockedMessage = new ConfigString("blockedMessage","");


    public static ConfigString donoName = new ConfigString("donoName","");
    public static ConfigString donoMessage = new ConfigString("donoMessage","");

    public static ConfigString apiName = new ConfigString("apiName","");
    public static ConfigString apiKey = new ConfigString("apiKey","");



    @Override
    public void onEnable() {
        ConfigElement.init();
        this.getApi().registerForgeListener(chatQueueHandler);
        this.getApi().registerForgeListener(donationQueueHandler);
        this.getApi().getEventManager().registerOnIncomingPacket(new RealNameHandler());
        this.getApi().getEventManager().register(new ChatReceiveHandler());
        this.getApi().getEventManager().register(new CommandsHandler());
    }

    @Override
    public void loadConfig() {

    }

    @Override
    protected void fillSettings(List<SettingsElement> list) {
        list.add(new HeaderElement("§a§lTipeeeGG by Pleezon || AntiBannSystem"));
        list.add(new HeaderElement("§3§lChat-Commands:"));
        list.add(new HeaderElement("§a§l!block NAME -> blocks said user"));
        list.add(new HeaderElement("§a§l!list -> lists all blocked users"));
        list.add(new HeaderElement("§a§l!remove NAME/UUID -> removes blocked user from the list"));
        list.add(new HeaderElement("§3§lPlaceholders:"));
        list.add(new HeaderElement("§a§l%n% -> name"));
        list.add(new HeaderElement("§a§l%a% -> $ amount"));
        list.add(new HeaderElement("§3§lleave field blank to disable the field's functionality completely."));

        list.add(new BooleanElement("Enabled", new ControlElement.IconData(Material.LEVER),(a)->{isEnabled=a;},isEnabled));
        NumberElement conversionRateElement = new NumberElement("?$ = 1EUR", new ControlElement.IconData(Material.GOLD_NUGGET), conversionRate.getValue());
        conversionRateElement.setMinValue(1);
        conversionRateElement.addCallback((v) -> {
            conversionRate.setValue(v);
        });
        list.add(conversionRateElement);
        list.add(new StringElement("Thanks Message", new ControlElement.IconData(Material.EMERALD), thanksMessage.getValue(), (v) -> {
            thanksMessage.setValue(v);
        }));
        list.add(new StringElement("Error Message", new ControlElement.IconData(Material.REDSTONE), errorMessage.getValue(), (v) -> {
            errorMessage.setValue(v);
        }));
        list.add(new StringElement("Blocked Message", new ControlElement.IconData(Material.PAPER), blockedMessage.getValue(), (v) -> {
            blockedMessage.setValue(v);
        }));
        list.add(new StringElement("Donation Name", new ControlElement.IconData(Material.PAPER), donoName.getValue(), (v) -> {
            donoName.setValue(v);
        }));
        list.add(new StringElement("Donation Message", new ControlElement.IconData(Material.PAPER), donoMessage.getValue(), (v) -> {
            donoMessage.setValue(v);
        }));

        list.add(new HeaderElement("§4§lAPI-Settings"));
        list.add(new StringElement("ApiName",new ControlElement.IconData(Material.REDSTONE_TORCH_ON),apiName.getValue(),(v)->{
            apiName.setValue(v);
        }));
        list.add(new PasswordElement("ApiKey",new ControlElement.IconData(Material.REDSTONE_TORCH_ON),apiKey.getValue(),(v)->{
            apiKey.setValue(v);
        }));
    }



}
