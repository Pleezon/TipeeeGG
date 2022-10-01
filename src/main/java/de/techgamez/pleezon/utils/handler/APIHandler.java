package de.techgamez.pleezon.utils.handler;

import de.techgamez.pleezon.utils.misc.Donation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.stream.Collectors;

public class APIHandler {
    public static void sendDonation(String apiName, String apiKey, Donation donation){
        new Thread(()->{
            try {
                StringBuilder urlBuilder = new StringBuilder("https://api.tipeeestream.com/v1.0/users/"+apiName.toLowerCase()+"/events?apiKey="+apiKey+"&type=donation&params[currency]=EUR&params[amount]="+ donation.getAmountEUR());
                if(!donation.getDonoName().equals("")){
                    urlBuilder.append("&params[username]=").append(URLEncoder.encode(donation.getDonoName(),"UTF-8").replaceAll("\\+","%20"));
                }
                if(!donation.getDonoMessage().equals("")){
                    urlBuilder.append("&params[message]=").append(URLEncoder.encode(donation.getDonoMessage(),"UTF-8").replaceAll("\\+","%20"));
                }
                URL url = new URL(urlBuilder.toString());
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");
                con.setRequestMethod("POST");
                con.setRequestProperty("Accept","application/json, text/plain, */*");
                con.setDoOutput(true);
                con.setRequestProperty("User-Agent", UUID.randomUUID().toString());
                con.setFixedLengthStreamingMode(0);
                try(BufferedReader br=new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))){
                    System.out.println(br.lines().collect(Collectors.joining("\n")));
                }
                System.out.println("SUCCESS! " +donation);
                donation.onDonated(true);
            } catch (IOException ex) {
                ex.printStackTrace();
                System.out.println("FAILED! " + donation);
                donation.onDonated(false);
            }
        }).start();
    }
    public static String getName(String uuid) {
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid.replace("-", "")).openStream()));
            String line;
            while((line = bufferedReader.readLine())!= null){
                if(line.trim().startsWith("\"name\" : \"")){
                    return line.trim().replaceFirst("\"name\" : \"","").replaceAll("\",","");
                }
            }
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
