import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.Timer;

import org.jibble.pircbot.PircBot;
import org.jibble.pircbot.User;

class Channel extends PircBot{
        	//////////////////////////////////////////
        	public static ArrayList<ArrayList<String>> PointListList = new ArrayList<ArrayList<String>>();
        	public static ArrayList<String> PointList = new ArrayList<String>();		;
        	public static ArrayList<ArrayList<String>> ModListList = new ArrayList<ArrayList<String>>() ;
        	public static ArrayList<String> ModList = new ArrayList<String>();
			private String name;
			
			
			//////////////////////////////////////////
        	Channel(PircBot bot, String name){
        		bot.joinChannel(name);
        		ModList = new ArrayList<String>();
        		ModList.add(name);
        		ModListList.add(ModList);   
        		//Map<String,Integer> userList = new HashMap<String, Integer>();
        		Timer timer1 = new Timer(3000,new ActionListener() {	
        			@Override
        			public void actionPerformed(ActionEvent arg0) {
        				ArrayList<User> Users = new ArrayList<User>(Arrays.asList(getUsers(name)));
        				System.out.println(Users.size());
        				//System.out.println(users[0].getNick());
        				//for(int i = 0;i<users.length;i++){
        					//System.out.println(users[i].getNick());
        				//	}
        				}
        			});
                timer1.start();
        	}      
        	public String getChannelName(){
        		return name;
        	}
        	
        	public static void addMod(String channel, String modName){
        		for(int i = 0; i<ModListList.size();i++){
        			if(ModListList.get(i).get(0).equals(channel)){
        				if(ModListList.get(i).contains(modName)){
        					System.out.println(modName + " is already a mod");
        					break;
                		}	
        				else{
        				ModListList.get(i).add(modName);
    					System.out.println("Added mod " + modName);
    					break;
    					}
        			}
        		}	
        	}
        	
        	public static void removeMod(String channel, String modName){
        		for(int i = 0; i<ModListList.size();i++){
        			if(ModListList.get(i).get(0).equals(channel)){
        				if(ModListList.get(i).contains(modName)){
        					ModListList.get(i).remove(modName);
        					System.out.println("Removed mod " + modName);
        					break;
                		}	
        			}
        		}	
        	}
        	
        	public static boolean isMod(String channel, String name){
        		for(int i = 0; i<ModListList.size();i++){
        			if(ModListList.get(i).get(0).equals(channel)){
        				if(ModListList.get(i).contains(name)){
        					return true;
        				}
        			}
        		}  
        		return false;
        	}
        }