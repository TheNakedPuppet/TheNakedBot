import org.jibble.pircbot.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javax.swing.Timer;


public class TheNakedBot extends PircBot{
    
    public TheNakedBot() {
        this.setName("thenakedpuppet");
    }
   
    public static void main(String[] args) throws Exception {
        
    	TheNakedBot bot = new TheNakedBot();
    	Scanner scanner = new Scanner(System.in);
    	
        bot.setVerbose(true);        
        bot.connect("irc.twitch.tv",6667,"oauth:470lgpmgm914vf85gw5z4s84ot0ik4");   
        bot.joinChannel("#azerfrost");
     
        
        if(scanner.hasNext("dc")){scanner.close(); bot.disconnect(); System.exit(0);}
    }
    
    
    public void onMessage(String channel, String sender, String login, String hostname, String message) {
    	/*******************Normal Commands*****************/
    	/*
        if (message.equalsIgnoreCase("!time")) {
            String time = new java.util.Date().toString();
            sendMessage(channel, sender + ": The time is now " + time);
            
        }
        
        if (message.equalsIgnoreCase("!overlay murica")){
        	TheNakedBotWindow.setOverlay(message);
        	Timer timer = new Timer(1000, new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent arg0) {
					TheNakedBotWindow.playSound("src/Resources/AMERICANEW.wav");
				}});
        	timer.setRepeats(false);
        	timer.start();
        	sendMessage(channel,sender + " is a real American");
        }
        
        if (message.equalsIgnoreCase("!overlay default")){
        	TheNakedBotWindow.setOverlay(message);
        	Timer timer = new Timer(1000, new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent arg0) {
					TheNakedBotWindow.playSound("src/Resources/ZeldaTheme.wav");
				}});
        	timer.setRepeats(false);
        	timer.start();
        	sendMessage(channel,sender + " has changed the overlay");
        }
        
        if (message.equalsIgnoreCase("!PEEPEE")){
        	TheNakedBotWindow.playSound("src/Resources/applause.wav");
        	sendMessage(channel,sender + " has a big peepee");

        }
        
        */
        
        if(message.equalsIgnoreCase("!np") || message.equalsIgnoreCase("!song") || message.equalsIgnoreCase("!map")){
        	String content = null;
            try (Scanner scanner = new Scanner(new File("src/np.txt")).useDelimiter("\\Z")) {
                content = ".me " + scanner.next();
                scanner.close();
            } catch (FileNotFoundException e) {
				e.printStackTrace();
			}
            sendMessage(channel,content);
            
        }
        
        if(message.equalsIgnoreCase("!dc") || message.equalsIgnoreCase("!off") && sender.contains("thenakedpuppet")){
            sendMessage(channel,"Goodbye!");
        	disconnect();
        	System.exit(0);
        }}}
       /*
        if (message.equalsIgnoreCase("!hug") || message.contains("sad")|| message.contains("sadder")|| message.contains("saddest")|| message.contains("cri")||message.contains("cry")||message.contains("depressed")){
        	String response = ".me hugs " + sender;
        	sendMessage(channel, response);
        }
        
        /**************Dummy Commands***********//*
        
        if (message.equalsIgnoreCase("!skin")){
        	String response = "Thanks to Tobi/IAmAladdin for the god skin: http://puu.sh/exdNP/820102fd59.osk ";
        	sendMessage(channel, response);
        }
        if (message.equalsIgnoreCase("!jesse")){
        	String response = "Fuck you other jesse DansGame";
        	sendMessage(channel, response);
        }
       
        
        if (message.equalsIgnoreCase("!keyboard")){
        	String response = "Ducky Shine 3 w/ Mx Browns Kreygasm";
        	sendMessage(channel, response);
        }
        if (message.equalsIgnoreCase("!tablet")){
        	String response = "I hover on a CTL-480";
        	sendMessage(channel, response);
        }
        if (message.equalsIgnoreCase("!area")){
        	String response = "4749x4749 on 1920x1080 monitor";
        	sendMessage(channel, response);
        }
        if (message.equalsIgnoreCase("!game")){
        	String response = "I stream CS:GO on osu! because streaming it under CS:GO would show my viewer count in game and I don't like that";
        	sendMessage(channel, response);
        }
        if (message.equalsIgnoreCase("!chippy")){
        	String response = "http://i.imgur.com/46eZfIW.gif";
        	sendMessage(channel, response);
        }
        if (message.equalsIgnoreCase("!res")){
        	String response = "Windowed 1600x900";
        	sendMessage(channel, response);
        }
    }
}*/