package core;

import java.awt.Desktop;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Color;
import java.awt.Cursor;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;
import javax.swing.JTabbedPane;
import javax.swing.ImageIcon;

import java.io.IOException;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Objects;
import java.awt.Toolkit;

public class Main extends JFrame {
	private static final long serialVersionUID = 1L;
	
	private JTabbedPane tabbedPane;
	private JLabel lblTitle_1;
	private JLabel lblTitle_2;
	private JLabel lblTitle_3;
	private JLabel lblTitle_4;
	private JLabel lblLan_11;
	private JLabel lblLan_12;
	private JLabel lblLan_13;
	private JLabel lblLan_21;
	private JLabel lblLan_22;
	private JLabel lblLan_23;
	private JLabel lblRes_11;
	private JLabel lblRes_12;
	private JLabel lblRes_13;
	private JLabel lblHelp1;
	private JLabel lblHelp2;
	private JLabel lblHelp3;
	private JLabel lblHelp4;
	private JLabel lblHelp5;
	private JButton update;
	private JButton button1;
	private JButton button2;
	private JButton button3;
	private JComboBox<Object> cbRes1;
	private JComboBox<Object> cbRes2;
	private JComboBox<Object> cbRes3;
	private JComboBox<Object> cbLan1;
	private JComboBox<Object> cbLan2;
	private JComboBox<Object> cbLan3;
	private JButton langDE;
	private JButton langEN;

	private String version = "Final 2.3";
	private String versionDate = "24.08.2017";
	
	private int game = 0;
	private static int proLang = 0;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main frame = new Main();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	private Main() throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
	// Frame
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setBounds(100, 100, 500, 300);
		setResizable(false);
		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
		setContentPane(contentPane);
		setIconImage(Toolkit.getDefaultToolkit().getImage(Main.class.getResource("/images/icon.png")));
	
	// Database	
		String[] resolutions = {
				Auto.Resolution(), "800*600", "1024*768", "1280*720", "1366*768", "1600*900", "1600*1200", "1920*1080", "1920*1200", 
				"2560*1080", "2560*1440", "3840*2160", "4096*2160", "7680*4320", "8192*4320"
        };
		String[] languages = {
				"Deutsch", "English", "Español", "Français", "Italiano",
				"Nederlands", "Norsk", "Polski", "Svenska"
        };
		String[] languagesR = {
				"Deutsch", "English", "Español", "Français", "Italiano",
				"Nederlands", "Norsk", "Polski", "Russian", "Svenska"
        };
		
	// Initialize empty Labels
		lblTitle_1 = new JLabel("");
		lblTitle_2 = new JLabel("");
		lblTitle_3 = new JLabel("");
		lblTitle_4 = new JLabel("");
		lblLan_11 = new JLabel("");
		lblLan_12 = new JLabel("");
		lblLan_13 = new JLabel("");
		lblLan_21 = new JLabel("");
		lblLan_22 = new JLabel("");
		lblLan_23 = new JLabel("");
		lblRes_11 = new JLabel("");
		lblRes_12 = new JLabel("");
		lblRes_13 = new JLabel("");
		lblHelp1 = new JLabel("");
		lblHelp2 = new JLabel("");
		lblHelp3 = new JLabel("");
		lblHelp4 = new JLabel("");
		lblHelp5 = new JLabel("");
		update = new JButton("");
		button1 = new JButton("");
		button2 = new JButton("");
		button3 = new JButton("");
		cbRes1 = new JComboBox<>(resolutions);
		cbRes2 = new JComboBox<>(resolutions);
		cbRes3 = new JComboBox<>(resolutions);
		cbLan1 = new JComboBox<>(languages);
		cbLan2 = new JComboBox<>(languages);
		cbLan3 = new JComboBox<>(languagesR);
		langDE = new JButton();
		langEN = new JButton();
		
	// Languages	
		langDE.setBounds(460, 6, 30, 20);
		contentPane.add(langDE);
		langDE.setToolTipText("Sprache: Deutsch");
		langDE.addActionListener(arg0 -> setDeutsch());
		
		langEN.setBounds(425, 6, 30, 20);
		contentPane.add(langEN);
		langEN.setToolTipText("Language: English");
		langEN.addActionListener(e -> setEnglish());
		
	// TabPane
		tabbedPane.setBounds(0, 0, 495, 272);
		contentPane.add(tabbedPane);
		
		JPanel bfme1 = new JPanel();
		tabbedPane.addTab("", null, bfme1, null);
		bfme1.setLayout(null);
		
		JPanel bfme2 = new JPanel();
		tabbedPane.addTab("", null, bfme2, null);
		bfme2.setLayout(null);
		
		JPanel bfme3 = new JPanel();
		tabbedPane.addTab("", null, bfme3, null);
		bfme3.setLayout(null);
		
		JPanel help = new JPanel();
		tabbedPane.addTab("<html><body leftmargin=10 marginwidth=10 marginheight=5>?</body></html", null, help, null);
		help.setLayout(null);
	
	// Initialize Language
		if (Objects.equals(Auto.Language(), "Deutsch"))
			setDeutsch();
		else 
			setEnglish();
		
	// Label
		// Title
			lblTitle_1.setForeground(Color.WHITE);
			lblTitle_1.setBounds(0, 11, 490, 17);
			bfme1.add(lblTitle_1);
			lblTitle_1.setFont(new Font("Tahoma", Font.BOLD, 14));
			lblTitle_1.setHorizontalAlignment(SwingConstants.CENTER);
			
			lblTitle_2.setForeground(Color.WHITE);
			lblTitle_2.setHorizontalAlignment(SwingConstants.CENTER);
			lblTitle_2.setFont(new Font("Tahoma", Font.BOLD, 14));
			lblTitle_2.setBounds(0, 11, 490, 17);
			bfme2.add(lblTitle_2);
			
			lblTitle_3.setForeground(Color.WHITE);
			lblTitle_3.setHorizontalAlignment(SwingConstants.CENTER);
			lblTitle_3.setFont(new Font("Tahoma", Font.BOLD, 14));
			lblTitle_3.setBounds(0, 11, 490, 17);
			bfme3.add(lblTitle_3);
		
			lblTitle_4.setHorizontalAlignment(SwingConstants.CENTER);
			lblTitle_4.setForeground(Color.WHITE);
			lblTitle_4.setFont(new Font("Tahoma", Font.BOLD, 14));
			lblTitle_4.setBounds(0, 11, 490, 17);
			help.add(lblTitle_4);
			
		// Instructions
			lblLan_11.setForeground(Color.WHITE);
			lblLan_11.setBounds(30, 61, 177, 14);
			bfme1.add(lblLan_11);
			lblLan_11.setHorizontalAlignment(SwingConstants.CENTER);
			
			lblLan_12.setForeground(Color.WHITE);
			lblLan_12.setBounds(30, 61, 177, 14);
			bfme2.add(lblLan_12);
			lblLan_12.setHorizontalAlignment(SwingConstants.CENTER);
			
			lblLan_13.setForeground(Color.WHITE);
			lblLan_13.setBounds(30, 61, 177, 14);
			bfme3.add(lblLan_13);
			lblLan_13.setHorizontalAlignment(SwingConstants.CENTER);
			
			lblLan_21.setForeground(Color.WHITE);
			lblLan_21.setBounds(30, 74, 177, 14);
			bfme1.add(lblLan_21);
			lblLan_21.setHorizontalAlignment(SwingConstants.CENTER);
			
			lblLan_22.setForeground(Color.WHITE);
			lblLan_22.setBounds(30, 74, 177, 14);
			bfme2.add(lblLan_22);
			lblLan_22.setHorizontalAlignment(SwingConstants.CENTER);
			
			lblLan_23.setForeground(Color.WHITE);
			lblLan_23.setBounds(30, 74, 177, 14);
			bfme3.add(lblLan_23);
			lblLan_23.setHorizontalAlignment(SwingConstants.CENTER);
			
			lblRes_11.setForeground(Color.WHITE);
			lblRes_11.setBounds(263, 70, 206, 14);
			bfme1.add(lblRes_11);
			lblRes_11.setHorizontalAlignment(SwingConstants.CENTER);
			
			lblRes_12.setForeground(Color.WHITE);
			lblRes_12.setBounds(263, 70, 206, 14);
			bfme2.add(lblRes_12);
			lblRes_12.setHorizontalAlignment(SwingConstants.CENTER);
		
			lblRes_13.setForeground(Color.WHITE);
			lblRes_13.setBounds(263, 70, 206, 14);
			bfme3.add(lblRes_13);
			lblRes_13.setHorizontalAlignment(SwingConstants.CENTER);
		
		// Help
			lblHelp1.setHorizontalAlignment(SwingConstants.CENTER);
			lblHelp1.setForeground(Color.WHITE);
			lblHelp1.setBounds(0, 59, 490, 17);
			help.add(lblHelp1);
			
			lblHelp2.setHorizontalAlignment(SwingConstants.CENTER);
			lblHelp2.setForeground(Color.WHITE);
			lblHelp2.setBounds(0, 100, 490, 17);
			help.add(lblHelp2);
			
			lblHelp3.setHorizontalAlignment(SwingConstants.CENTER);
			lblHelp3.setForeground(Color.WHITE);
			lblHelp3.setBounds(0, 115, 490, 14);
			help.add(lblHelp3);
			
		// Website Ref
			HTMLEditorKit kit = new HTMLEditorKit();
	        StyleSheet styleSheet = kit.getStyleSheet();
	        styleSheet.addRule("a {color:#4ec5ff;}");
	        
			lblHelp4.setText("<html><a href=justusc.eu/bfme-resfix>justusc.eu/bfme-resfix</a></html>");
			lblHelp4.setToolTipText("justusc.eu/bfme-resfix");
			lblHelp4.setHorizontalAlignment(SwingConstants.CENTER);
			lblHelp4.setBounds(0, 128, 490, 17);
			help.add(lblHelp4);
			lblHelp4.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			lblHelp4.addMouseListener(new MouseAdapter() {
			    public void mouseClicked(MouseEvent e) {
			    	URL myURL = null;
		            try {
		                myURL = new URL("http://justusc.eu/bfme-resfix/");
		            } catch (MalformedURLException e1) {
		                e1.printStackTrace();
		            }
		            openWebpage(myURL);
			    }
			});
			lblHelp5.setHorizontalAlignment(SwingConstants.RIGHT);
			lblHelp5.setForeground(Color.WHITE);
			lblHelp5.setBounds(180, 207, 300, 16);
			help.add(lblHelp5);

	// CB Resolutions
		cbRes1.setBounds(277, 99, 179, 39);
		bfme1.add(cbRes1);
		cbRes1.setFont(new Font("Tahoma", Font.PLAIN, 27));
		cbRes1.setSelectedItem(Auto.Resolution());
		((JLabel)cbRes1.getRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
		
		cbRes2.setBounds(277, 99, 179, 39);
		bfme2.add(cbRes2);
		cbRes2.setFont(new Font("Tahoma", Font.PLAIN, 27));
		cbRes2.setSelectedItem(Auto.Resolution());
		((JLabel)cbRes2.getRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
		
		cbRes3.setBounds(277, 99, 179, 39);
		bfme3.add(cbRes3);
		cbRes3.setFont(new Font("Tahoma", Font.PLAIN, 27));
		cbRes3.setSelectedItem(Auto.Resolution());
		((JLabel)cbRes3.getRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
			
	// CB Languages
		cbLan1.setBounds(30, 99, 177, 39);
		bfme1.add(cbLan1);
		cbLan1.setFont(new Font("Tahoma", Font.PLAIN, 27));
		cbLan1.setSelectedItem(Auto.Language());
		((JLabel)cbLan1.getRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
		
		cbLan2.setBounds(30, 99, 177, 39);
		bfme2.add(cbLan2);
		cbLan2.setFont(new Font("Tahoma", Font.PLAIN, 27));
		cbLan2.setSelectedItem(Auto.Language());
		((JLabel)cbLan2.getRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
		
		cbLan3.setBounds(30, 99, 177, 39);
		bfme3.add(cbLan3);
		cbLan3.setFont(new Font("Tahoma", Font.PLAIN, 27));
		cbLan3.setSelectedItem(Auto.Language());
		((JLabel)cbLan3.getRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
		
	// Apply Changes
		button1.setBounds(176, 173, 132, 39);
		bfme1.add(button1);
		button1.addActionListener(e -> {
            String language = (String)cbLan1.getSelectedItem();
            String resolution = (String)cbRes1.getSelectedItem();
            game = 1;
            try {
                Ini.main(language, resolution, game);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
				
		button2.setBounds(176, 173, 132, 39);
		bfme2.add(button2);
		button2.addActionListener(e -> {
            String language = (String)cbLan2.getSelectedItem();
            String resolution = (String)cbRes2.getSelectedItem();
            game = 2;
            try {
                Ini.main(language, resolution, game);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });		
		
		button3.setBounds(176, 173, 132, 39);
		bfme3.add(button3);
		button3.addActionListener(e -> {
            String language = (String)cbLan3.getSelectedItem();
            String resolution = (String)cbRes3.getSelectedItem();
            game = 3;
            try {
                Ini.main(language, resolution, game);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });			

	// Update-Check
		update.setBounds(10, 194, 121, 29);
		help.add(update);	
		update.setToolTipText("https://www.mediafire.com/folder/5qvyz9eb03d7i/BFME-Windows-Resolution-Fixer");
		update.addActionListener(e -> {
            URL myURL = null;
            try {
                myURL = new URL("https://www.mediafire.com/folder/5qvyz9eb03d7i/BFME-Windows-Resolution-Fixer");
            } catch (MalformedURLException e1) {
                e1.printStackTrace();
            }
            openWebpage(myURL);
        });
		
	// Images
		JLabel img1 = new JLabel();
		img1.setIcon(new ImageIcon(Main.class.getResource("/images/bfme1.png")));
		img1.setBounds(0, 0, 490, 234);
		bfme1.add(img1);
		
		JLabel img2 = new JLabel();
		img2.setIcon(new ImageIcon(Main.class.getResource("/images/bfme2.png")));
		img2.setBounds(0, 0, 490, 234);
		bfme2.add(img2);
		
		JLabel img3 = new JLabel();
		img3.setIcon(new ImageIcon(Main.class.getResource("/images/bfme3.png")));
		img3.setBounds(0, 0, 490, 234);
		bfme3.add(img3);
		
		JLabel imgHelp = new JLabel();
		imgHelp.setIcon(new ImageIcon(Main.class.getResource("/images/help.png")));
		imgHelp.setBounds(0, 0, 490, 234);
		help.add(imgHelp);
	}
	
	// Update Checker
		private static void openWebpage(URI string) {
		    Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
		    if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
		        try {
		            desktop.browse(string);
		        } catch (Exception e) {
		            e.printStackTrace();
		        }
		    }
		}

		private static void openWebpage(URL url) {
		    try {
		        openWebpage(url.toURI());
		    } catch (URISyntaxException e) {
		        e.printStackTrace();
		    }
		}
		
	// Languages Execute
	private void setDeutsch() {
		// Main
			setProLang(1);
			langEN.setIcon(new ImageIcon(Main.class.getResource("/images/en1.png")));
			langDE.setIcon(new ImageIcon(Main.class.getResource("/images/de0.png")));
			
			setTitle("SUM - Windows & Auflösungs-Fixer");
		
		// Tab
			tabbedPane.setTitleAt(0, "<html><body leftmargin=15 marginwidth=15 marginheight=5><b>SUM I</b></body></html");
			tabbedPane.setTitleAt(1, "<html><body leftmargin=15 marginwidth=15 marginheight=5><b>SUM II</b></body></html");
			tabbedPane.setTitleAt(2, "<html><body leftmargin=15 marginwidth=15 marginheight=5><b>SUM II - ADH</b></body></html");
		
		// Label
			lblTitle_1.setText("Die Schlacht um Mittelerde I");
			lblTitle_2.setText("Die Schlacht um Mittelerde II");
			lblTitle_3.setText("Die Schlacht um Mittelerde II - Aufstieg des Hexenkönigs");
			lblTitle_4.setText("Hilfe");
		
			String lan1 = "Wähle die Sprache, in der";
			lblLan_11.setText(lan1);
			lblLan_12.setText(lan1);
			lblLan_13.setText(lan1);
			
			String lan2 = "du das Spiel installiert hast";
			lblLan_21.setText(lan2);
			lblLan_22.setText(lan2);
			lblLan_23.setText(lan2);
			
			String res1 = "Wähle deine bevorzugte Auflösung";
			lblRes_11.setText(res1);
			lblRes_12.setText(res1);
			lblRes_13.setText(res1);
			
			lblHelp1.setText("Hast du weitere Fragen?");	
			lblHelp2.setText("Klicke \"Aktualisieren\" für die neuste Version");
			lblHelp3.setText("oder besuche meine Webseite mit einer Anleitung:");
			lblHelp5.setText("Version: " + version + " (" + versionDate + ") von Justus Cöster");
			
		// Button
			update.setText("Aktualisieren");
			
			String apply = "Übernehmen";
			button1.setText(apply);
			button2.setText(apply);
			button3.setText(apply);
		
		// ToolTips
			String res = "Die ausgewählte Auflösung kann auch HÖHER sein als deine aktuelle Desktop-Auflösung";
			cbRes1.setToolTipText(res);
			cbRes2.setToolTipText(res);
			cbRes3.setToolTipText(res);
			
			String lan = "Es ist wichtig dass du die Sprache deiner SPIELINSTALLATION auswählst";
			cbLan1.setToolTipText(lan);
			cbLan2.setToolTipText(lan);
			cbLan3.setToolTipText(lan);
			
			String buttonTip = "Generiere oder editiere die Options.ini-Datei in deinem SUM-Ordner";
			button1.setToolTipText(buttonTip);
			button2.setToolTipText(buttonTip);
			button3.setToolTipText(buttonTip);
	}
	
	private void setEnglish() {
		// Main
			setProLang(2);
			langEN.setIcon(new ImageIcon(Main.class.getResource("/images/en0.png")));
			langDE.setIcon(new ImageIcon(Main.class.getResource("/images/de1.png")));
			
			setTitle("BFME - Windows & Resolution-Fixer");
		
		// Tab
			tabbedPane.setTitleAt(0, "<html><body leftmargin=15 marginwidth=15 marginheight=5><b>BFME I</b></body></html");
			tabbedPane.setTitleAt(1, "<html><body leftmargin=15 marginwidth=15 marginheight=5><b>BFME II</b></body></html");
			tabbedPane.setTitleAt(2, "<html><body leftmargin=15 marginwidth=15 marginheight=5><b>BFME II - ROTWK</b></body></html");
		
		// Label
			lblTitle_1.setText("The Battle for Middle-earth I");
			lblTitle_2.setText("The Battle for Middle-earth II");
			lblTitle_3.setText("The Battle for Middle-earth II: The Rise of the Witch-king");
			lblTitle_4.setText("Help");
			
			String lan1 = "Select the language, in which";
			lblLan_11.setText(lan1);
			lblLan_12.setText(lan1);
			lblLan_13.setText(lan1);
			
			String lan2 = "you've installed the game";
			lblLan_21.setText(lan2);
			lblLan_22.setText(lan2);
			lblLan_23.setText(lan2);
			
			String res1 = "Select your preferred resolution";
			lblRes_11.setText(res1);
			lblRes_12.setText(res1);
			lblRes_13.setText(res1);
			
			lblHelp1.setText("You need additional help?");	
			lblHelp2.setText("Click \"Check for updates\" for the latest version");
			lblHelp3.setText("or visit my website with additional instructions:");
			lblHelp5.setText("Version: " + version + " (" + versionDate + ") by Justus Cöster");
		
		// Button
			update.setText("Check for updates");
			
			String apply = "Apply changes";
			button1.setText(apply);
			button2.setText(apply);
			button3.setText(apply);
		
		// ToolTip
			String res = "You can also set your In-game-resolution HIGHER than your current desktop-resolution";
			cbRes1.setToolTipText(res);
			cbRes2.setToolTipText(res);
			cbRes3.setToolTipText(res);
			
			String lan = "It is important that you select the language of your GAME-INSTALLATION";
			cbLan1.setToolTipText(lan);
			cbLan2.setToolTipText(lan);
			cbLan3.setToolTipText(lan);
			
			String buttonTip = "Generate or edit the Options.ini-file in your BFME-folder";
			button1.setToolTipText(buttonTip);
			button2.setToolTipText(buttonTip);
			button3.setToolTipText(buttonTip);
	}

	static int getProLang() {
		return proLang;
	}

	private static void setProLang(int proLang) {
		Main.proLang = proLang;
	}
}