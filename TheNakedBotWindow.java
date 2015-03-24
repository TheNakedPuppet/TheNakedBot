import java.awt.Color;
import java.awt.Desktop;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.Timer;

import org.jibble.pircbot.IrcException;


public class TheNakedBotWindow extends JFrame{
	

	private static final long serialVersionUID = 1L;

	TheNakedBotWindow(){
	////////////////////////////////MENU STUFF////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
		JMenuBar Menu = new JMenuBar();
		JMenu File = new JMenu("File");
		File.setMnemonic('f');
		JMenu add = new JMenu("Add");
		JMenuItem Option1 = new JMenuItem("Option1");
		JMenu settings = new JMenu("Settings");
		JMenuItem preferences = new JMenuItem("Preferences");
		JMenuItem about = new JMenuItem("About");
		
		Option1.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {	
				setOverlay("src/Resources/JaredOverlayAmerica.png");
		        Timer timer = new Timer(1000, new ActionListener(){
					@Override
					public void actionPerformed(ActionEvent arg0) {
						playSound("src/Resources/AMERICANEW.wav");
					}});
				};
		});
		
		
		JMenuItem Option2 = new JMenuItem("Option2");
		Option2.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e1) {
				setOverlay("src/Resources/JaredOverlay.png");
		        Timer timer = new Timer(1000, new ActionListener(){
					@Override
					public void actionPerformed(ActionEvent arg0) {
							playSound("src/Resources/ZeldaTheme.wav");
						}});
				};
		});
		/*TODO/*
		preferences.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(arg0.getSource() == preferences){
					JFrame preferencesWindow = new JFrame("Preferences");
					
				}
				
				
			}});
		settings.setMnemonic('s');
		////////////////////////////////////////////////////////////////*/
		about.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				JFrame aboutWindow = new JFrame("About");
				aboutWindow.setBackground(new Color(255,255,255));
				aboutWindow.setLayout(new FlowLayout());
				JButton donate = new JButton("");
				 try {
					    Image img = ImageIO.read(new File("src/Resources/paypal-donate-button.png"));
					    donate.setMargin(new Insets(0, 0, 0, 0));
					    donate.setBackground(new Color(255,255,255));
					    donate.setBorder(null);
					    donate.setIcon(new ImageIcon(img));
					  } catch (IOException ex) { ex.printStackTrace(); }
				 
				donate.addActionListener(new ActionListener(){

					@Override
					public void actionPerformed(ActionEvent e) {
						openWebpage("https://www.twitchalerts.com/donate/thenakedpuppet");
						
					}
					
				});
				aboutWindow.add(new JLabel("Made by TheNakedPuppet"));
				aboutWindow.add(new JLabel("Version b1.001 3/23/15"));
				aboutWindow.add(donate);
				aboutWindow.setVisible(true);
				aboutWindow.setSize(300,200);
				aboutWindow.setLocationRelativeTo(getParent());
				
			}});
		
		
		add.add(Option1);
		add.add(Option2);
		File.add(add);
		settings.add(preferences);
		settings.add(about);
		Menu.add(File);
		Menu.add(settings);
		setJMenuBar(Menu);
		
	////////////////////////////////PANEL STUFF////////////////////////////////
		GraphicsPanel gp1 = new GraphicsPanel();
		add(gp1);		
		setTitle("TNP's Thing");
	////////////////////////////////WINDOW STUFF////////////////////////////////	
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setSize(640,360);	
		
	}

	public static void main(String[] args) {
		
		System.setProperty("awt.useSystemAAFontSettings","on");
		System.setProperty("swing.aatext", "true");
		TheNakedBotWindow window = new TheNakedBotWindow();
		/*TheNakedBot bot = new TheNakedBot();
    	Scanner scanner = new Scanner(System.in);
    	
        bot.setVerbose(true);        
        try {
			bot.connect("irc.twitch.tv",6667,"oauth:470lgpmgm914vf85gw5z4s84ot0ik4");
		} catch (IOException | IrcException e1) {e1.printStackTrace();}   
        bot.joinChannel("#thenakedpuppet");
        if(scanner.hasNext("dc")){scanner.close(); bot.disconnect(); System.exit(0);}
        */
        
		window.setVisible(true);
	}
	
	
	
	
	/////////////////////////////////////////////STATIC METHODS/////////////////////////////////////
	public static void setOverlay(String message) {
		switch(message.toLowerCase()){
		case "!overlay murica": writeImage("src/Resources/JaredOverlayAmerica.png","src/Resources/target.png");
								break;
		case "!overlay default": writeImage("src/Resources/JaredOverlay.png","src/Resources/target.png");
								break;
		default : writeImage(message,"src/Resources/target.png"); 
		}		
	}
	
	public static void openWebpage(String url) {
		try 
        {
            Desktop.getDesktop().browse(new URL(url).toURI());
        }           
        catch (Exception e) {}
	}
	
	public static Image loadImage(String url) throws IOException{
		Image image = null;
		image = ImageIO.read(new File(url));
		return image;
	}
	
	
	
	public static void writeImage(String url,String target) {
	    BufferedImage bi = null;
		try {
			bi = (BufferedImage) loadImage(url);
		} catch (IOException e) {e.printStackTrace();}
		System.out.println("Image Loaded");

	    File outputfile = new File(target);
		try {
			ImageIO.write(bi, "png", outputfile);
		} catch (IOException e) {e.printStackTrace();}
		
		System.out.println("Image Written");
	
	}
	
	 public static synchronized void playSound(final String url) {
		 File file = new File (url);
	        new Thread(new Runnable() {
	            public void run() {
	                try {
	                    Clip clip = AudioSystem.getClip();
	                    AudioInputStream inputStream = AudioSystem.getAudioInputStream(file);                  
	                    clip.open(inputStream);
	                    clip.start();
	                } catch (Exception e) {
	                    e.printStackTrace();
	                    System.err.println(e.getMessage());
	                }
	            }
	        }).start();
	}
	
	////////////////////////////////////GRAPHICS STUFF/////////////////////////////////////////////
	class GraphicsPanel extends JPanel implements ActionListener { 
		private static final long serialVersionUID = 1L;
		Image image = null;
		Timer timer = new Timer(3600000, this);
		
		
		@Override protected void paintComponent(Graphics g) {
			
			super.paintComponent(g);
			/*float f=20.0f; // font size.
			   g.setFont(g.getFont().deriveFont(f));
			g.setColor(new Color(0,255,0));
			g.fillRect(0, 0, this.getWidth(), this.getHeight());

			g.setColor(new Color(255,0,0));
			g.drawImage(image, 0, 0, this.getWidth(), this.getHeight(), null);
			*///g.drawString("THANKS FOR THE MONEY LOSER", 50, this.getHeight()/2);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			/*
			if(e.getSource() == timer){
			File sound = new File("src/NEVERGIVEUP.wav");
			playSound(sound);
			try {
				image = loadImage("src/Ear_Rape_Spider.png");
			} catch (IOException e1) {
				e1.printStackTrace();
			}*/

			repaint();
			
			}
		
	}

}
