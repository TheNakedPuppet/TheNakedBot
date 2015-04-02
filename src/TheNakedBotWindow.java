import java.awt.Color;
import java.awt.Desktop;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Properties;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.Timer;


public class TheNakedBotWindow extends JFrame{

	final static long startTime = System.currentTimeMillis();
	private static final long serialVersionUID = 1L;
	static Properties properties = new Properties();
	static String overlayFolder;
	static String soundFolder;
	static String linksFile = "Resources/Links.txt";
	static String pointsDatabaseFolder;
	static String pointsChannel;
	static String botName;
	static String oauth;
	static String a;
	static JTextArea chatLog = new JTextArea();

	public static void main(String[] args) {
		TheNakedBotWindow window = new TheNakedBotWindow();
		window.setVisible(true);  
	}

	TheNakedBotWindow(){

		if(!new File("config.properties").exists()){
			setPropertyValue("overlayFolder","Resources/Overlays/");
			setPropertyValue("soundFolder","Resources/Sounds/");
			setPropertyValue("linksFile","Resources/Links.txt");
			setPropertyValue("pointsDatabaseFolder","Resources/Points/");
			setPropertyValue("pointsChannel","#forlorn_king");
			setPropertyValue("botName","Enter Bot Name");
			setPropertyValue("oauth","Enter Bot's oauth (From http://waa.ai/Iq6)");
		}
		overlayFolder = getPropertyValue("overlayFolder");
		soundFolder = getPropertyValue("soundFolder");
		linksFile = getPropertyValue("linksFile");
		pointsDatabaseFolder = getPropertyValue("pointsDatabaseFolder");;
		pointsChannel = getPropertyValue("pointsChannel");;    
		botName = getPropertyValue("botName");
		oauth = getPropertyValue("oauth");
		
		TheNakedBot bot = new TheNakedBot();
		////////////////////////////////MENU STUFF//////////////////////////////////
		///////////////               ///////////////                    ///////////////
		KeyListener ctrlWActionListener = new KeyListener(){

			@Override
			public void keyPressed(KeyEvent e) {
				if ((e.getKeyCode() == KeyEvent.VK_W) && ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0)) {
					System.exit(0);
				}
			}

			@Override
			public void keyReleased(KeyEvent arg0) {	
			}

			@Override
			public void keyTyped(KeyEvent arg0) {	
			}
		};
		this.addKeyListener(ctrlWActionListener);

		FlowLayout fLayoutLeft = new FlowLayout(FlowLayout.LEFT);
		JMenuBar Menu = new JMenuBar();
		JMenu File = new JMenu("File");
		File.setMnemonic('f');

		JMenu add = new JMenu("Add");
		JMenuItem reconnect = new JMenuItem("Reconnect");
		JMenuItem exit = new JMenuItem("Exit");
		JMenuItem Option1 = new JMenuItem("Option1");
		JMenu settings = new JMenu("Settings");
		JMenuItem preferences = new JMenuItem("Preferences");
		JMenuItem about = new JMenuItem("About");

		JFrame preferencesWindow = new JFrame("Preferences");

		JPanel overlaysPanel = new JPanel(fLayoutLeft);
		JTextField overlays = new JTextField(overlayFolder,15);

		JPanel soundsPanel = new JPanel(fLayoutLeft);
		JTextField sounds = new JTextField(soundFolder,15);
		
		JPanel linksFilePanel = new JPanel(fLayoutLeft);
		JTextField linksFileField = new JTextField(linksFile,15);

		JPanel pointsDatabasePanel = new JPanel(fLayoutLeft);
		JTextField pointsDatabaseField = new JTextField(pointsDatabaseFolder,15);

		JPanel pointsChannelPanel = new JPanel(fLayoutLeft);
		JTextField pointsChannelField = new JTextField(pointsChannel,15);

		JPanel botNamePanel = new JPanel(fLayoutLeft);
		JTextField botNameField = new JTextField(botName,15);


		JPanel oauthPanel = new JPanel(fLayoutLeft);
		JTextField oauthField = new JTextField(oauth,15);

		JPanel saveButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		JButton save = new JButton("Save");

		saveButtonPanel.add(save);


		preferencesWindow.setLayout(new GridLayout(9,1));

		overlaysPanel.add(new JLabel("Overlays:"));
		overlaysPanel.add(overlays);


		soundsPanel.add(new JLabel("Sounds:  "));
		soundsPanel.add(sounds);
		
		linksFilePanel.add(new JLabel("Links File: "));
		linksFilePanel.add(linksFileField);

		pointsDatabasePanel.add(new JLabel("Points Folder :"));
		pointsDatabasePanel.add(pointsDatabaseField);

		pointsChannelPanel.add(new JLabel("Channel to Connect to :"));
		pointsChannelPanel.add(pointsChannelField);

		botNamePanel.add(new JLabel("Bot Name:  "));
		botNamePanel.add(botNameField);

		oauthPanel.add(new JLabel("Bot OAuth:  "));
		oauthPanel.add(oauthField);


		preferencesWindow.add(new JLabel(" File Paths: "));
		preferencesWindow.add(overlaysPanel);
		preferencesWindow.add(soundsPanel);
		preferencesWindow.add(linksFilePanel);
		preferencesWindow.add(pointsDatabasePanel);
		preferencesWindow.add(pointsChannelPanel);
		preferencesWindow.add(botNamePanel);
		preferencesWindow.add(oauthPanel);
		preferencesWindow.add(saveButtonPanel);

		preferencesWindow.setResizable(false);
		preferencesWindow.setSize(400,300);
		preferencesWindow.setLocationRelativeTo(getParent());

		save.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				setPropertyValue("overlayFolder",overlays.getText());
				setPropertyValue("soundFolder",sounds.getText());
				setPropertyValue("linksFile",linksFileField.getText());
				setPropertyValue("pointsDatabaseFolder",pointsDatabaseField.getText());                
				if(getPropertyValue("pointsChanel") != pointsChannelField.getText() && getPropertyValue("pointsChanel") != "#"+pointsChannelField.getText()){
					if(getPropertyValue("pointsChannel") != pointsChannelField.getText() && pointsChannelField.getText().contains("#")){
						setPropertyValue("pointsChannel",pointsChannelField.getText());
					}
					else{
						setPropertyValue("pointsChannel","#" + pointsChannelField.getText());
						pointsChannelField.setText("#" + pointsChannelField.getText());
					}
				}
				setPropertyValue("botName",botNameField.getText());
				setPropertyValue("oauth",oauthField.getText());
			}

		});

		preferences.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(arg0.getSource() == preferences){
					preferencesWindow.setVisible(true);
				}      
			}
		});
		//////////////////////////////////////////////////////////////////
		settings.setMnemonic('s');
		////////////////////////////////////////////////////////////////*/

		Option1.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {   
				setOverlay(overlayFolder + "JaredOverlayAmerica.png");
				Timer timer = new Timer(1000, new ActionListener(){
					@Override
					public void actionPerformed(ActionEvent arg0) {
						playSound(soundFolder + "AMERICANEW.wav");
					}});
				timer.setRepeats(false);
				timer.start();
			};
		});


		JMenuItem Option2 = new JMenuItem("Option2");
		Option2.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e1) {
				setOverlay(overlayFolder + "JaredOverlay.png");
				Timer timer = new Timer(1000, new ActionListener(){
					@Override
					public void actionPerformed(ActionEvent arg0) {
						playSound(soundFolder + "ZeldaTheme.wav");
					}});
				timer.setRepeats(false);
				timer.start();
			};
		});
		reconnect.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				bot.partChannel(pointsChannel);
				bot.joinChannel(pointsChannel);
			}              
		});
		exit.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}              
		});
		about.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				JFrame aboutWindow = new JFrame("About");
				aboutWindow.setBackground(new Color(255,255,255));
				aboutWindow.setLayout(new FlowLayout());
				JButton donate = new JButton("");
				try {
					Image img = ImageIO.read(new File("Resources/paypal-donate-button.png"));
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
				aboutWindow.add(new JLabel("Special thanks to: "));
				aboutWindow.add(new JLabel("Obduration and IAmCloudChaser for testing"));
				aboutWindow.add(new JLabel(" and Hatsuney for general advice"));
				aboutWindow.add(donate);
				aboutWindow.setVisible(true);
				aboutWindow.setSize(300,225);
				aboutWindow.setLocationRelativeTo(getParent());                        
			}});
		add.add(Option1);
		add.add(Option2);
		File.add(add);
		File.addSeparator();
		File.add(reconnect);
		File.addSeparator();
		File.add(exit);
		settings.add(preferences);
		settings.add(about);
		Menu.add(File);
		Menu.add(settings);
		setJMenuBar(Menu);
		////////////////////////////////WINDOW STUFF////////////////////////////////   
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setSize(640,360);      
	}

	public static void appendLog(String str){
		chatLog.append(str);
	}

	public static void setPropertyValue(String property, String value){
		OutputStream output = null;
		try {
			output = new FileOutputStream("config.properties");
			properties.setProperty(property, value);
			properties.store(output, null);
			System.out.println("Set Property " + property + " to value:" + properties.getProperty(property));

		} catch (IOException io) {
			io.printStackTrace();
		} finally {
			if (output != null) {
				try {
					output.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
	}

	public String getPropertyValue(String property) {
		String propFileName = "config.properties";
		InputStream inputStream;
		try {
			inputStream = new FileInputStream(propFileName);
			properties.load(inputStream);
			inputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		String result = properties.getProperty(property);
		System.out.println("Got value: " + result + " from property " + property);
		return result;
	}
	/////////////////////////////////////////////STATIC METHODS/////////////////////////////////////
	public static void setOverlay(String message) {
		switch(message.toLowerCase()){
		case "!overlay murica": writeImage(overlayFolder + "JaredOverlayAmerica.png",overlayFolder + "target.png");
		break;
		case "!overlay default": writeImage(overlayFolder + "JaredOverlay.png",overlayFolder + "target.png");
		break;
		default : writeImage(message,overlayFolder + "target.png");
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
					inputStream.close();
					clip.start();
				} catch (Exception e) {
					e.printStackTrace();
					System.err.println(e.getMessage());
				}
			}
		}).start();
	}

	////////////////////////////////////GRAPHICS STUFF/////////////////////////////////////////////

}