import java.awt.Color;
import java.awt.Desktop;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Properties;
import java.util.Scanner;

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
import javax.swing.JTextField;
import javax.swing.Timer;


public class TheNakedBotWindow extends JFrame{

	final static long startTime = System.currentTimeMillis();
	private static final long serialVersionUID = 1L;
	static Properties properties = new Properties();
	static String overlayFolder;
	static String soundFolder;

	TheNakedBotWindow(){
		if(!new File("config.properties").exists()){
		setPropertyValue("overlayFolder","Resources/Overlays/");
		setPropertyValue("soundFolder","Resources/Sounds/");}
		overlayFolder = getPropertyValue("overlayFolder");
		soundFolder = getPropertyValue("soundFolder");
		////////////////////////////////MENU STUFF//////////////////////////////////
		///////////////               ///////////////             	 /////////////// 		
		JMenuBar Menu = new JMenuBar();
		JMenu File = new JMenu("File");
		File.setMnemonic('f');
		JMenu add = new JMenu("Add");
		JMenuItem Option1 = new JMenuItem("Option1");
		JMenu settings = new JMenu("Settings");
		JMenuItem preferences = new JMenuItem("Preferences");
		JMenuItem about = new JMenuItem("About");

		JFrame preferencesWindow = new JFrame("Preferences");
		JPanel overlaysPanel = new JPanel();
		JTextField overlays = new JTextField(overlayFolder,15);
		JButton saveButton1 = new JButton("Save");
		
		JPanel soundsPanel = new JPanel();
		JTextField sounds = new JTextField(soundFolder,15);
		JButton saveButton2 = new JButton("Save");
		
		Option1.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {	
				setOverlay(overlayFolder + "JaredOverlayAmerica.png");
				Timer timer = new Timer(1000, new ActionListener(){
					@Override
					public void actionPerformed(ActionEvent arg0) {
						playSound(soundFolder + "AMERICANEW.wav");
					}});
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
			};
		});
		
		preferencesWindow.setLayout(new GridLayout(7,1));
		overlaysPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		overlaysPanel.add(new JLabel("  Overlays:"));
		overlaysPanel.add(overlays);
		overlaysPanel.add(saveButton1);
		
		soundsPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		soundsPanel.add(new JLabel("  Sounds:  "));
		soundsPanel.add(sounds);
		soundsPanel.add(saveButton2);
		preferencesWindow.add(new JLabel(" File Paths: "));
		preferencesWindow.add(overlaysPanel);
		preferencesWindow.add(soundsPanel);
		
		preferencesWindow.setResizable(false);
		preferencesWindow.setSize(400,300);
		preferencesWindow.setLocationRelativeTo(getParent());
		saveButton1.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				setPropertyValue("overlayFolder",overlays.getText());
				overlays.setText(overlays.getText());
			}
			
		});
		saveButton2.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				setPropertyValue("soundFolder",sounds.getText());
				sounds.setText(sounds.getText());
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
		window.setVisible(true);
		TheNakedBot bot = new TheNakedBot();
		Scanner scanner = new Scanner(System.in); 
		if(scanner.hasNext("dc")){bot.sendMessage("#thenakedpuppet","I'M DEAD"); scanner.close(); bot.disconnect(); System.exit(0);}	
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


			repaint();

		}

	}

}
