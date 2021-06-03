package de.techgamez.pleezon.config;



import java.io.*;
import java.util.Properties;


public class ConfigElement {
    /**
     * base class for properties wrapper
     * too lazy for generics, go blame someone else.
     */
    String entryname;
    private static String configFile = "./tipeeeForGG.pleezon";
    ConfigElement(String entryname){
        this.entryname = entryname;
    }

    public static void init() {
        try {
            new File(configFile).createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void save(String value) throws IOException {
        Properties prop = new Properties();
        prop.load(new BufferedInputStream(new FileInputStream(configFile)));
        prop.setProperty(entryname,value);
        prop.store(new FileOutputStream(configFile),"");
    }

    public String load() throws IOException {
        Properties prop = new Properties();
        prop.load(new BufferedInputStream(new FileInputStream(configFile)));
        return prop.getProperty(entryname);
    }
    boolean isRegisteredInConfig() {
        try{
            Properties prop = new Properties();
            prop.load(new BufferedInputStream(new FileInputStream(configFile)));
            return prop.containsKey(entryname);
        }catch (Exception e){return false;}
    }
}
