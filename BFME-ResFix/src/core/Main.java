package core;

import java.awt.Desktop;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import javax.swing.JTabbedPane;
import javax.swing.ImageIcon;

import java.awt.Color;

public class Main extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private String version = "1.3.0";
	private String versionDate = "25.07.2015";
	
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

	public Main() throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
	//Frame
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 500, 300);
		setResizable(false);
		setTitle("BFME - Windows & Resolution Fixer");
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
		setContentPane(contentPane);
	
	//Datenbank		
		String[] resolutions = {
				Auto.Resolution(), "800*600", "1024*768", "1280*720", "1280*960", "1280*1024", "1366*768", "1400*1050", 
				"1440*960", "1600*900", "1600*1200", "1680*1050", "1920*1080", "1920*1200", "2560*1600", "3840*2160"
        };
		String[] languages = {
				"Deutsch", "English", "Español", "Français", "Italiano",
				"Nederlands", "Norsk", "Polski", "Svenska"
        };
		String[] languagesR = {
				"Deutsch", "English", "Español", "Français", "Italiano",
				"Nederlands", "Norsk", "Polski", "Russian", "Svenska"
        };
		
	//TabPane
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(0, 0, 495, 272);
		contentPane.add(tabbedPane);
		
		JPanel bfme1 = new JPanel();
		tabbedPane.addTab("<html><body leftmargin=15 marginwidth=15 marginheight=5>BFME I</body></html", null, bfme1, null);
		bfme1.setLayout(null);
		
		JPanel bfme2 = new JPanel();
		tabbedPane.addTab("<html><body leftmargin=15 marginwidth=15 marginheight=5>BFME II</body></html", null, bfme2, null);
		bfme2.setLayout(null);
		
		JPanel bfme3 = new JPanel();
		tabbedPane.addTab("<html><body leftmargin=15 marginwidth=15 marginheight=5>BFME II - ROTWK</body></html", null, bfme3, null);
		bfme3.setLayout(null);
		
		JPanel help = new JPanel();
		tabbedPane.addTab("<html><body marginheight=5>?</body></html", null, help, null);
		help.setLayout(null);
		
	//Label
		//Title
			JLabel lblTitle_1 = new JLabel("The Battle for Middle-earth I");
			lblTitle_1.setForeground(Color.WHITE);
			lblTitle_1.setBounds(0, 11, 490, 17);
			bfme1.add(lblTitle_1);
			lblTitle_1.setFont(new Font("Tahoma", Font.BOLD, 14));
			lblTitle_1.setHorizontalAlignment(SwingConstants.CENTER);
			
			JLabel lblTitle_2 = new JLabel("The Battle for Middle-earth II");
			lblTitle_2.setForeground(Color.WHITE);
			lblTitle_2.setHorizontalAlignment(SwingConstants.CENTER);
			lblTitle_2.setFont(new Font("Tahoma", Font.BOLD, 14));
			lblTitle_2.setBounds(0, 11, 490, 17);
			bfme2.add(lblTitle_2);
			
			JLabel lblTitle_3 = new JLabel("The Battle for Middle-earth II: The Rise of the Witch-king");
			lblTitle_3.setForeground(Color.WHITE);
			lblTitle_3.setHorizontalAlignment(SwingConstants.CENTER);
			lblTitle_3.setFont(new Font("Tahoma", Font.BOLD, 14));
			lblTitle_3.setBounds(0, 11, 490, 17);
			bfme3.add(lblTitle_3);
			
			JLabel lbltitle4 = new JLabel("Help");
			lbltitle4.setHorizontalAlignment(SwingConstants.CENTER);
			lbltitle4.setForeground(Color.WHITE);
			lbltitle4.setFont(new Font("Tahoma", Font.BOLD, 14));
			lbltitle4.setBounds(0, 11, 490, 17);
			help.add(lbltitle4);
			
		//Instructions
			JLabel lblLan_11 = new JLabel("Select the Language in which");
			lblLan_11.setForeground(Color.WHITE);
			lblLan_11.setBounds(30, 61, 177, 14);
			bfme1.add(lblLan_11);
			lblLan_11.setHorizontalAlignment(SwingConstants.CENTER);
			
			JLabel lblLan_12 = new JLabel("Select the Language in which");
			lblLan_12.setForeground(Color.WHITE);
			lblLan_12.setBounds(30, 61, 177, 14);
			bfme2.add(lblLan_12);
			lblLan_12.setHorizontalAlignment(SwingConstants.CENTER);
			
			JLabel lblLan_13 = new JLabel("Select the Language in which");
			lblLan_13.setForeground(Color.WHITE);
			lblLan_13.setBounds(30, 61, 177, 14);
			bfme3.add(lblLan_13);
			lblLan_13.setHorizontalAlignment(SwingConstants.CENTER);
			
			JLabel lblLan_21 = new JLabel("you've installed the Game");
			lblLan_21.setForeground(Color.WHITE);
			lblLan_21.setBounds(30, 74, 177, 14);
			bfme1.add(lblLan_21);
			lblLan_21.setHorizontalAlignment(SwingConstants.CENTER);
			
			JLabel lblLan_22 = new JLabel("you've installed the Game");
			lblLan_22.setForeground(Color.WHITE);
			lblLan_22.setBounds(30, 74, 177, 14);
			bfme2.add(lblLan_22);
			lblLan_22.setHorizontalAlignment(SwingConstants.CENTER);
			
			JLabel lblLan_23 = new JLabel("you've installed the Game");
			lblLan_23.setForeground(Color.WHITE);
			lblLan_23.setBounds(30, 74, 177, 14);
			bfme3.add(lblLan_23);
			lblLan_23.setHorizontalAlignment(SwingConstants.CENTER);
			
			JLabel lblRes_11 = new JLabel("Select your preferred resolution");
			lblRes_11.setForeground(Color.WHITE);
			lblRes_11.setBounds(270, 70, 194, 14);
			bfme1.add(lblRes_11);
			lblRes_11.setHorizontalAlignment(SwingConstants.CENTER);
			
			JLabel lblRes_12 = new JLabel("Select your preferred resolution");
			lblRes_12.setForeground(Color.WHITE);
			lblRes_12.setBounds(270, 70, 194, 14);
			bfme2.add(lblRes_12);
			lblRes_12.setHorizontalAlignment(SwingConstants.CENTER);
			
			JLabel lblRes_13 = new JLabel("Select your preferred resolution");
			lblRes_13.setForeground(Color.WHITE);
			lblRes_13.setBounds(270, 70, 194, 14);
			bfme3.add(lblRes_13);
			lblRes_13.setHorizontalAlignment(SwingConstants.CENTER);
		
		//Help
			JLabel lblHelp1 = new JLabel("It doesn't work for you?");
			lblHelp1.setHorizontalAlignment(SwingConstants.CENTER);
			lblHelp1.setForeground(Color.WHITE);
			lblHelp1.setBounds(0, 69, 490, 17);
			help.add(lblHelp1);
			
			JLabel lblHelp2 = new JLabel("You need additional help?");
			lblHelp2.setHorizontalAlignment(SwingConstants.CENTER);
			lblHelp2.setForeground(Color.WHITE);
			lblHelp2.setBounds(0, 85, 490, 17);
			help.add(lblHelp2);
			
			JLabel lblHelp3 = new JLabel("Contact me: MorgulLord29isback@gmail.com");
			lblHelp3.setHorizontalAlignment(SwingConstants.CENTER);
			lblHelp3.setForeground(Color.WHITE);
			lblHelp3.setBounds(0, 117, 490, 17);
			help.add(lblHelp3);
			
			JLabel lblHelp4 = new JLabel("Version v" + version + " (" + versionDate + ") by MorCJul");
			lblHelp4.setHorizontalAlignment(SwingConstants.RIGHT);
			lblHelp4.setForeground(Color.WHITE);
			lblHelp4.setBounds(248, 207, 232, 16);
			help.add(lblHelp4);

	//CB Resolutions
		JComboBox<Object> cbRes1 = new JComboBox<Object>(resolutions);
		cbRes1.setBounds(277, 99, 179, 39);
		bfme1.add(cbRes1);
		cbRes1.setFont(new Font("Tahoma", Font.PLAIN, 27));
		cbRes1.setSelectedItem(Auto.Resolution());
		((JLabel)cbRes1.getRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
		cbRes1.setToolTipText("Setting your In-Game-Resolution higher than your current Desktop-Resolution will NOT work");
		
		JComboBox<Object> cbRes2 = new JComboBox<Object>(resolutions);
		cbRes2.setBounds(277, 99, 179, 39);
		bfme2.add(cbRes2);
		cbRes2.setFont(new Font("Tahoma", Font.PLAIN, 27));
		cbRes2.setSelectedItem(Auto.Resolution());
		((JLabel)cbRes2.getRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
		cbRes2.setToolTipText("Setting your In-Game-Resolution higher than your current Desktop-Resolution will NOT work");
		
		JComboBox<Object> cbRes3 = new JComboBox<Object>(resolutions);
		cbRes3.setBounds(277, 99, 179, 39);
		bfme3.add(cbRes3);
		cbRes3.setFont(new Font("Tahoma", Font.PLAIN, 27));
		cbRes3.setSelectedItem(Auto.Resolution());
		((JLabel)cbRes3.getRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
		cbRes3.setToolTipText("Setting your In-Game-Resolution higher than your current Desktop-Resolution will NOT work");
			
	//CB Languages
		JComboBox<Object> cbLan1 = new JComboBox<Object>(languages);
		cbLan1.setBounds(30, 99, 177, 39);
		bfme1.add(cbLan1);
		cbLan1.setFont(new Font("Tahoma", Font.PLAIN, 27));
		cbLan1.setSelectedItem(Auto.Language());
		((JLabel)cbLan1.getRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
		cbLan1.setToolTipText("It is important that you select the Language of your Game-Installation, otherwise the fix will NOT work");
		
		JComboBox<Object> cbLan2 = new JComboBox<Object>(languages);
		cbLan2.setBounds(30, 99, 177, 39);
		bfme2.add(cbLan2);
		cbLan2.setFont(new Font("Tahoma", Font.PLAIN, 27));
		cbLan2.setSelectedItem(Auto.Language());
		((JLabel)cbLan2.getRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
		cbLan2.setToolTipText("It is important that you select the Language of your Game-Installation, otherwise the fix will NOT work");
		
		JComboBox<Object> cbLan3 = new JComboBox<Object>(languagesR);
		cbLan3.setBounds(30, 99, 177, 39);
		bfme3.add(cbLan3);
		cbLan3.setFont(new Font("Tahoma", Font.PLAIN, 27));
		cbLan3.setSelectedItem(Auto.Language());
		((JLabel)cbLan3.getRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
		cbLan3.setToolTipText("It is important that you select the Language of your Game-Installation, otherwise the fix will NOT work");
		
	//Apply Changes
		JButton button1 = new JButton("Apply changes");
		button1.setBounds(176, 173, 132, 39);
		bfme1.add(button1);
		button1.setToolTipText("Generate or edit the Options.ini-File in your BFME-Folder");
		button1.addActionListener(new ActionListener() {
			//ActionPerformed
			public void actionPerformed(ActionEvent e) {
				String language = (String)cbLan1.getSelectedItem();
		        String resolution = (String)cbRes1.getSelectedItem();
		        resolution = resolution.replace('*', ' ');

				try {
					BFME1.main(resolution, language);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});			
				
		JButton button2 = new JButton("Apply changes");
		button2.setBounds(176, 173, 132, 39);
		bfme2.add(button2);
		button2.setToolTipText("Generate or edit the Options.ini-File in your BFME-Folder");
		button2.addActionListener(new ActionListener() {
			//ActionPerformed
			public void actionPerformed(ActionEvent e) {
				String language = (String)cbLan2.getSelectedItem();
		        String resolution = (String)cbRes2.getSelectedItem();
		        resolution = resolution.replace('*', ' ');
		        
				try {
					BFME2.main(resolution, language);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});		
		
		JButton button3 = new JButton("Apply changes");
		button3.setBounds(176, 173, 132, 39);
		bfme3.add(button3);
		button3.setToolTipText("Generate or edit the Options.ini-File in your BFME-Folder");
		button3.addActionListener(new ActionListener() {
			//ActionPerformed
			public void actionPerformed(ActionEvent e) {
				String language = (String)cbLan3.getSelectedItem();
		        String resolution = (String)cbRes3.getSelectedItem();
		        resolution = resolution.replace('*', ' ');
		        
				try {
					BFME3.main(resolution, language);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});				
		
	//Update-Check
		JButton update = new JButton("Check for updates");
		update.setBounds(10, 194, 121, 29);
		help.add(update);	
		update.setToolTipText("Opens the Mediafire.com-Folder with the latest Version inside.");
		update.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				URL myURL = null;
				try {
					myURL = new URL("http://bit.ly/1KmgFXN");
				} catch (MalformedURLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				openWebpage(myURL);
			}
		});
		
	//Images
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
	
	public static void openWebpage(URI string) {
	    Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
	    if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
	        try {
	            desktop.browse(string);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	}

	public static void openWebpage(URL url) {
	    try {
	        openWebpage(url.toURI());
	    } catch (URISyntaxException e) {
	        e.printStackTrace();
	    }
	}
}