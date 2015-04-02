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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import javax.swing.Timer;


public class TheNakedBot extends PircBot{
	/////////////////////////////CONSTRUCTOR///////////////////////////////
	boolean canRespond = true;
	ArrayList<String> ModList = new ArrayList<String>();
	static Map<String,Integer> pointListStatic= (Map<String, Integer>) deserializeObject("Resources/Points/" + TheNakedBotWindow.pointsChannel + ".ser");
	static String Channel = TheNakedBotWindow.pointsChannel;
	static String[] SpecialUsers = {"dittochan","obduration","iamcloudchaser","thenakedpuppet","suzuyabot","juicebox5401","e223","wieran1111","emralgreeny","elquilious","zognegnad102","zodiaack","aivycore"};
	static int linksNumberOfLines = 0;
	int pointsInterval = 60000;
	public TheNakedBot() {

		/////TEMP ///////////
		
		this.setName(TheNakedBotWindow.botName);
		try {
			connect("irc.twitch.tv",6667,TheNakedBotWindow.oauth);
		} catch (IOException | IrcException e) {
			e.printStackTrace();
		}  

		joinChannel(Channel);
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

		@SuppressWarnings("unchecked")
		Map<String,Integer> pointListA = (HashMap<String,Integer>)deserializeObject(TheNakedBotWindow.pointsDatabaseFolder + TheNakedBotWindow.pointsChannel + ".ser");
		if(deserializeObject(TheNakedBotWindow.pointsDatabaseFolder + TheNakedBotWindow.pointsChannel + ".ser")==null){
			pointListA = new HashMap<String,Integer>();
		}

		Map<String,Integer> pointListNew = new HashMap<String,Integer>();
		Map<String,Integer> pointList = pointListA;
		Timer timer1 = new Timer(pointsInterval, new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				//Makes arraylist<String,Integer> (Our PoinList)from User array (Our User List). I forget how this works.
				//This is fucking disgusting
				ArrayList<User>Users = new ArrayList<User>(Arrays.asList(getUsers(Channel)));
				for(int i = 0;i<Users.size();i++){
					if(pointList.containsKey(Users.get(i).getNick())){
						if(pointList.get(Users.get(i).getNick())<1000){
							pointList.put(Users.get(i).getNick(), pointList.get(Users.get(i).getNick()) + pointList.get(Users.get(i).getNick())/100 + 1);
							System.out.println(Channel + " Set user " + Users.get(i).getNick() + "'s total points to " +  pointList.get(Users.get(i).getNick()));
						}
						else{
							pointList.put(Users.get(i).getNick(), pointList.get(Users.get(i).getNick()) + 10);
							System.out.println(Channel + " Set user " + Users.get(i).getNick() + "'s total points to " +  pointList.get(Users.get(i).getNick()));
						}
					}
					else{
						pointList.put(Users.get(i).getNick(), 1);
						System.out.println("Added user " + Users.get(i).getNick());
					}
					pointListNew.putAll(pointList);
				}
				//Saves new poinlist
				Users.clear();
				serializeObject(pointListNew,TheNakedBotWindow.pointsDatabaseFolder + TheNakedBotWindow.pointsChannel + ".ser");
				TheNakedBot.pointListStatic = pointListNew;
				System.out.println(Channel + " suzuyabot points = " + getPoints(Channel, "suzuyabot"));
			}});
		timer1.setInitialDelay(0);
		timer1.start();}


	/////////////////////////////MAIN//////////////////////////////////////
	public static void main(String[] args) throws Exception {
		TheNakedBot bot = new TheNakedBot();
		Scanner scanner = new Scanner(System.in);
		if(scanner.hasNext("dc")){scanner.close(); bot.disconnect(); System.exit(0);}
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
		TheNakedBotWindow.appendLog(sender+ ": " + message + "\n");
		Timer timer = new Timer(5000,new ActionListener(){
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

		if (message.equalsIgnoreCase("!overlay murica") && (( isMod(channel,sender) && canRespond) || isSpecialUser(sender))){
			TheNakedBotWindow.setOverlay(message);

			Timer timera = new Timer(1000, new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent arg0) {
					TheNakedBotWindow.playSound("Resources/Sounds/AMERICANEW.wav");
				}});
			timera.setRepeats(false);
			timera.start();
			timer.restart();
			canRespond = false;
			sendMessage(channel,sender + " is a real American");
		}

		if (message.equalsIgnoreCase("!overlay default") && (( isMod(channel,sender) && canRespond) || isSpecialUser(sender))){
			TheNakedBotWindow.setOverlay(message);

			Timer timer2 = new Timer(1000, new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent arg0) {
					TheNakedBotWindow.playSound("Resources/Sounds/ZeldaTheme.wav");
				}});
			timer2.setRepeats(false);
			timer2.start();
			canRespond = false;
			timer.restart();
			sendMessage(channel,sender + " has changed the overlay");
		}

		if (message.equalsIgnoreCase("!PEEPEE") && canRespond){
			int cost = 0;
			if(getPoints(channel,sender) - cost >= 0 || isSpecialUser(sender)){
				TheNakedBotWindow.playSound("Resources/Sounds/applause.wav");
				sendMessage(channel,sender + " has a big peepee");
				try{
					pointListStatic.put(sender, pointListStatic.get(sender)-cost);
				}catch(NullPointerException e){
					System.out.println("pointListStatic." + sender + "returned null :c");
				}
			}else sendMessage(channel,"Sorry " + sender + ", you're short " + (cost - getPoints(channel,sender)) + " points for that command ;w;");                
			canRespond = false;
			timer.restart();

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

		if (message.equalsIgnoreCase("!healthy") && canRespond){
			int cost = 0;
			if(getPoints(channel,sender) - cost >= 0 || isSpecialUser(sender)){
				sendMessage(channel,getRandomLine(new File(TheNakedBotWindow.linksFile),linksNumberOfLines));
				try{
					pointListStatic.put(sender, pointListStatic.get(sender)-cost);
				}catch(NullPointerException e){
					System.out.println("pointListStatic." + sender + "returned null :c");
				}


			}else sendMessage(channel,"Sorry " + sender + ", you're short " + (cost - getPoints(channel,sender)) + " points for that command ;w;");                
			canRespond = false;
			timer.restart();

		}
		/***************************************************/
		/*******************Normal Commands*****************/
		/***************************************************/

		if(message.equalsIgnoreCase("!points")) {
			sendMessage(channel,sender + " points: " + Integer.toString(getPoints(channel,sender)));
		}
		if (message.equalsIgnoreCase("!time")) {
			String time = new java.util.Date().toString();
			sendMessage(channel, sender + ": The time is now " + time);
		}      
		if(message.equalsIgnoreCase("!moist")) {
			int cost = 2000;
			if(pointListStatic.get(sender)!=null){
				if(pointListStatic.get(sender) - cost >= 0 || isSpecialUser(sender)){
					sendMessage(channel,"AZOOOOOOOOSUUUUUUU");
				}
				else sendMessage(channel,"Sorry " + sender + ", you're short " + (cost - getPoints(channel,sender)) + " points for that command ;w;"); 
			}else System.out.println("pointListStatic.get(" + sender + ") Is null");
		}
		if((message.equalsIgnoreCase("!dc") || message.equalsIgnoreCase("!off")) && (isMod(channel,sender) || isSpecialUser(sender))){
			sendMessage(channel,"Goodbye!");
			disconnect();
			System.exit(0);
		}
		if (message.toLowerCase().contains("!hug ") || message.toLowerCase().contains(" sad ")|| message.toLowerCase().contains(" sadder ")||
				message.toLowerCase().contains(" saddest ")||message.toLowerCase().contains(" cri")||
				message.toLowerCase().contains(" cry ")||message.toLowerCase().contains(" depressed ")){
			String response = ".me hugs " + sender;
			sendMessage(channel, response);
		}

		/***************************************************/
		/*******************Dummy Commands******************/
		/***************************************************/

		if(message.equalsIgnoreCase("!skin")){
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
	public int getPoints(String channel, String user){
		if(pointListStatic != null){
			if(pointListStatic.containsKey(user)){
				if(isSpecialUser(user)) return 9999;
				else return pointListStatic.get(user);
			}}
		return 0;
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