import org.jibble.pircbot.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.*;
import java.util.Scanner;

import javax.swing.Timer;


public class TheNakedBot extends PircBot{
	/////////////////////////////CONSTRUCTOR///////////////////////////////
	boolean canRespond = true;
	ArrayList<String> ModList = new ArrayList<String>();
	static String Channel = TheNakedBotWindow.pointsChannel;
	static String[] SpecialUsers = {"dittochan","obduration","iamcloudchaser","thenakedpuppet","suzuyabot","juicebox5401","e223","wieran1111","emralgreeny","elquilious","zognegnad102","zodiaack","aivycore","cho_bo"};
	static int linksNumberOfLines = 0;
	static File kancolleClipsDir = new File("Resources/Sounds/KancolleSounds/");
	static String commandsFile = "Resources/Commands.ser";
	static Map<String,String> commands = new HashMap<String,String>();

	public TheNakedBot() {
		if(!new File("Resources/Commands.ser").exists()){
			serializeObject(commands,"Resources/Commands.ser");	
		}
		try{
			commands = (HashMap<String,String>)(deserializeObject(commandsFile));
		}catch(Exception e){
			System.out.println(commandsFile);
		}

		this.setName(TheNakedBotWindow.botName);
		try {
			connect("irc.twitch.tv",6667,TheNakedBotWindow.oauth);
			TheNakedBotWindow.appendLog("Connected to twitch\n");
		} catch (IOException | IrcException e) {
			e.printStackTrace();
		}  

		joinChannel(Channel);
		TheNakedBotWindow.appendLog("Joined Channel " + Channel + "\n\n");
		setVerbose(true);

		try {
			File linksFile1 = new File(TheNakedBotWindow.linksFile);
			Scanner scanner = new Scanner(linksFile1);
			linksNumberOfLines = 0;
			while(scanner.hasNextLine()){
				linksNumberOfLines++;
				scanner.nextLine();
			}
			scanner.close();
		} catch (FileNotFoundException e) {e.printStackTrace();}
	}

	///////////////////////////EVENTS/////////////////////////////////////
	@Override
	protected void onUserMode(String channel, String sourceNick,String sourceLogin, String sourceHostname,String recipient){
		super.onUserMode(channel, sourceNick, sourceLogin, sourceHostname, recipient);
		String modName;
		if(recipient.contains("+o")){
			channel = recipient.substring(0, recipient.lastIndexOf(" +"));
			modName = recipient.substring(recipient.lastIndexOf(" ")+1, recipient.length());
			addMod(channel,modName);
		};
		if(recipient.contains("-o")){
			channel = recipient.substring(0, recipient.lastIndexOf(" -"));
			modName = recipient.substring(recipient.lastIndexOf(" ")+1, recipient.length());
			removeMod(channel,modName);
		};
	}


	//////////////////////////////////////COMMANDS///////////////////////////////////////////
	public void onMessage(String channel, String sender, String login, String hostname, String message) {
		long yourmilliseconds = System.currentTimeMillis();
		SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm:ss");    
		Date resultdate = new Date(yourmilliseconds);
		TheNakedBotWindow.appendLog(sdf.format(resultdate) + " " + sender + ": " + message + "\n");
		Timer timer = new Timer(3000,new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				canRespond = true;
			}              
		});
		timer.setRepeats(false);
		timer.start();

		/***************************************************/
		/*******************TIMED Commands******************/
		/***************************************************/

		if (message.equalsIgnoreCase("!PEEPEE") && canRespond){
			TheNakedBotWindow.playSound("Resources/Sounds/applause.wav");
			sender= Character.toUpperCase(sender.charAt(0)) + sender.substring(1);
			sendMessage(channel,sender + " has a big peepee");    
			canRespond = false;
			timer.restart();

		}

		if(message.matches("!suzuya addcom .+")){
			String[] params= getParams(message,2,true);
			commands.put(params[1], params[0]);
			serializeObject(commands,"Resources/Commands.ser");
			System.out.println(params[1] + " " + params[0]);
		}


		if(message.equalsIgnoreCase("!np") || message.equalsIgnoreCase("!song") || message.equalsIgnoreCase("!map")){
			String content = null;
			try (Scanner scanner = new Scanner(new File("Resources/NP/np.txt")).useDelimiter("\\Z")) {
				content = ".me - " +scanner.next();
				scanner.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			canRespond = false;
			timer.restart();
			sendMessage(channel,content);
		}
		if(message.equalsIgnoreCase("!uptime")&& canRespond ){
			long timeElapsed = System.currentTimeMillis() - TheNakedBotWindow.startTime;
			int seconds = (int)(timeElapsed / 1000);
			int minutes = seconds / 60;
			seconds -= minutes * 60;
			int hours = minutes / 60;
			minutes -= hours * 60;
			sendMessage(channel, hours + " hours " + minutes + " minutes " + seconds + " seconds");
			canRespond = false;
			timer.restart();
		}  
		if (message.toLowerCase().matches("!healthy .+")){
			String[] params = getParams(message,1,false);
			sendMessage(channel,getRandomLine(new File(TheNakedBotWindow.linksFile),linksNumberOfLines,Integer.parseInt(params[0])));
			canRespond = false;
			timer.restart();
		}

		if (message.equalsIgnoreCase("!healthy")){
			sendMessage(channel,getRandomLine(new File(TheNakedBotWindow.linksFile),linksNumberOfLines));
			canRespond = false;
			timer.restart();

		}
		if(message.toLowerCase().matches("!kancolle .+")){
			if(message.matches("!kancolle .+.\\d+")){
				String[] params = getParams(message, 2,false);
				sendMessage(channel,TheNakedBot.playKancolleClip(params[1],params[0]));
			}
			else{
				String[] params = getParams(message, 1,false);
				sendMessage(channel,TheNakedBot.playKancolleClip(params[0]));
			}
			canRespond = false;
			timer.restart();

		}

		/***************************************************/
		/*******************Normal Commands*****************/
		/***************************************************/

		if (message.equalsIgnoreCase("!time")) {
			String time = new java.util.Date().toString();
			sendMessage(channel, sender + ": The time is now " + time);
		}      
		if(message.equalsIgnoreCase("!moist")) {
			sendMessage(channel,"AZOOOOOOOOSUUUUUUU");
		}
		if((message.equalsIgnoreCase("!dc") || message.equalsIgnoreCase("!off")) && (isMod(channel,sender) || isSpecialUser(sender))){
			sendMessage(channel,"Goodbye!");
			disconnect();
			System.exit(0);
		}
		if (message.toLowerCase().equalsIgnoreCase("!hug") || message.toLowerCase().contains(" sad ")|| message.toLowerCase().contains(" sadder ")||
				message.toLowerCase().contains(" saddest ")||message.toLowerCase().contains(" cri ")||message.toLowerCase().contains(";w;")||
				message.toLowerCase().contains(" cry ")||message.toLowerCase().contains(" depressed ")||message.toLowerCase().contains(";_;")){
			sender = Character.toUpperCase(sender.charAt(0)) + sender.substring(1);
			String response = ".me hugs " + sender;
			sendMessage(channel, response);
		}

		if (message.toLowerCase().matches("!hug .+")){
			String[] params = getParams(message,1,false);
			sender = Character.toUpperCase(sender.charAt(0)) + sender.substring(1);
			sendMessage(channel,".me and " + sender + " hug " + params[0]);
		}
		if (message.toLowerCase().matches("!pet .+")){
			sender = Character.toUpperCase(sender.charAt(0)) + sender.substring(1);
			String[] params = getParams(message,1,false);
			sendMessage(channel,".me and " + sender + " pet " + params[0]);
		}
			
		/***************************************************/
		/*******************Dummy Commands******************/
		/***************************************************/

		if(message.equalsIgnoreCase("!skin") && channel.equals("#thenakedpuppet")){
			String response = "Thanks to Tobi/IAmAladdin for the god skin: http://puu.sh/exdNP/820102fd59.osk ";
			sendMessage(channel, response);
		}

		if (message.equalsIgnoreCase("!jesse")){
			String response = "Fuck you other jesse DansGame";
			sendMessage(channel, response);
		}
		//////////////////Making fun of ditto///////////////////
		if (message.equalsIgnoreCase("!healthy dii") || message.equalsIgnoreCase("!healthy ditto")){
			String response = "#0 http://waa.ai/vjXo.jpg";
			sendMessage(channel, response);
		}
		if (message.equalsIgnoreCase("!dittostream")){
			String response = "When ditto starts her stream: http://waa.ai/vjq8.gif";
			sendMessage(channel, response);
		}	
		if (message.equalsIgnoreCase("!dittomic")){
			String response = "When ditto says something: http://waa.ai/vjq8.gif";
			sendMessage(channel, response);
		}	
		if (message.equalsIgnoreCase("!ditto")){
			String response = "\"you dick *** hole mother fudgind raisin eating lameo\"";
			sendMessage(channel, response);
		}
		/////////////////////////////////////////////////////////////////


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
		if (message.equalsIgnoreCase("!calvin")){
			String response = "http://waa.ai/vvLj.jpg";
			sendMessage(channel, response);
		}
	}

	///////////////////////////CHANNEL STUFF//////////////////////////////

	public void addMod(String channel, String modName){
		if(ModList.contains(modName)){
			System.out.println(modName + " is already a mod");
		}      
		else{
			ModList.add(modName);
			System.out.println("Added mod " + modName);
		}
	}
	public  void removeMod(String channel, String modName){
		if(ModList.contains(modName)){
			ModList.remove(modName);
			System.out.println("Removed mod " + modName);
		}      
	}
	public boolean isMod(String channel, String name){
		if(ModList.contains(name)){
			return true;
		}
		return false;
	}
	public boolean isSpecialUser(String name){
		for(int i=0;i<SpecialUsers.length;i++){
			if(SpecialUsers[i].contains(name)){
				System.out.println(name + " is a special user");
				return true;
			}}
		System.out.println(name + " is NOT a special user");
		return false;
	}
	public void serializeObject(Object object,String file){
		try
		{
			FileOutputStream fileOut =
					new FileOutputStream(file);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(object);
			out.close();
			fileOut.close();
			System.out.println("Serialized data is saved in "+ file);
		}catch(IOException i)
		{
			i.printStackTrace();
		}
	}
	public static Object deserializeObject(String file){
		Object e = null;
		try
		{
			FileInputStream fileIn = new FileInputStream(file);
			ObjectInputStream in = new ObjectInputStream(fileIn);
			e = in.readObject();
			in.close();
			fileIn.close();
			return e;
		}catch(IOException i)
		{
			i.printStackTrace();
			return null;
		}catch(ClassNotFoundException c)
		{
			System.out.println("Object was not found(?!?!?!?!)");
			c.printStackTrace();
			return null;
		}
	}

	/************KANCOLLE STUFF******************/
	public String[] getParams(String message,int numberOfParameters,boolean isAddcom){
		message = message.trim();
		String[] array = new String[numberOfParameters];
		System.out.println("MESSAGE =" + message);
		if(isAddcom){
			message = message.substring(message.lastIndexOf("addcom ")+7,message.length());
			numberOfParameters = 2;
			String result = message.substring(message.lastIndexOf(" ")+1);
			array[0] = result;
			result = message.substring(message.lastIndexOf(" ")+1);
		}else{
			for(int i = numberOfParameters;i!=0;i--){
				System.out.println("LAST INDEX OF SPACE = " + message.lastIndexOf(" "));
				String result = message.substring(message.lastIndexOf(" ")+1);
				System.out.println(i + "  RESULT = " + result);
				message = message.substring(0, message.lastIndexOf(" "));
				System.out.println("MESSAGE SUBSTRING = " + message);
				array[numberOfParameters-i] = result;
			}			
		}return array;
	}	

	public static String playKancolleClip(String girlName){
		if(kancolleClipsDir.isDirectory()){
			System.out.println("IS DIRECTORY");
			File girlFolder = new File(kancolleClipsDir.getPath() + "/" + girlName + "/");
			System.out.println("PATH: " + girlFolder.getPath());
			if(girlFolder.exists()){
				System.out.println("EXISTS");
				int seed = (int) (Math.random() * (girlFolder.listFiles().length -1) + 1);
				if(seed > 0){
					TheNakedBotWindow.playSound(kancolleClipsDir.getPath() +"/" +  girlName + "/" + seed + ".wav");
					System.out.println(kancolleClipsDir.getPath() +"\\" +  girlName + "\\Image\\image 9.png");
					TheNakedBotWindow.writeImage(kancolleClipsDir.getPath() +"\\" +  girlName + "\\Image\\image 9.png",TheNakedBotWindow.overlayFolder + "target.png");
					return girlName + " - " + seed;
				}
			}
		}
		return "";
	}
	public static String playKancolleClip(String girlName,String number){
		if(kancolleClipsDir.isDirectory()){
			System.out.println(kancolleClipsDir.getPath() + "/" + girlName + "/" + number + ".wav");
			File sound = new File(kancolleClipsDir.getPath() + "/" + girlName + "/" + number + ".wav");	
			if(sound.exists()){
				TheNakedBotWindow.playSound(kancolleClipsDir.getPath() + "/" + girlName + "/" + number + ".wav");
				TheNakedBotWindow.writeImage(kancolleClipsDir.getPath() +"\\" +  girlName + "\\Image\\image 9.png",TheNakedBotWindow.overlayFolder + "target.png");
				return girlName + " - " + number;
			}
		}
		return "";
	}
	////////////////////////////////////////////////////////////////////////////////////
	public String getRandomLine(File file,int numberOfLines,int seed)
	{	
		Scanner scanner = null;
		try {
			scanner = new Scanner(file);
			for(int i=0;i<numberOfLines;i++){
				if(i == seed-1){       
					String result = "#" + (i+1) + " " + scanner.nextLine();
					scanner.close();
					return result;
				}
				scanner.nextLine();}
			scanner.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		scanner.close();
		return "#1 licdn.awwni.me/npmp.jpg";
	}

	public String getRandomLine(File file,int numberOfLines)
	{	
		Scanner scanner = null;
		try {
			scanner = new Scanner(file);
			System.out.println("number of lines: " + numberOfLines);
			int seed = (int) (Math.random() * numberOfLines + 1);
			System.out.println("seed: " + seed);
			for(int i=0;i<numberOfLines;i++){
				if(i == seed-1){       
					String result = "#" + (i+1) + " " + scanner.nextLine();
					scanner.close();
					return result;
				}
				scanner.nextLine();}
			scanner.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		scanner.close();
		return "#1 licdn.awwni.me/npmp.jpg";
	}
}