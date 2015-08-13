package core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.JOptionPane;

public class Ini {
		public static void main(String language, String resolution, int game) throws IOException {
			//Preparation
			String languagePath = null;
			resolution = resolution.replace('*', ' ');
		
			//Check Game
			if (game == 1) 
				languagePath = BFME1.buildLanguage(language);
			else if (game == 2) 
				languagePath = BFME2.buildLanguage(language);
			else if (game == 3) 
				languagePath = BFME3.buildLanguage(language);
			
			//Ausführen
			String fullPath = buildPath(languagePath);
			addFolder(fullPath);
			buildIni(fullPath, resolution, game);
		}
	
		public static void buildIni(String fullPath, String resolution, int game) throws IOException {
			String OptionsPath = fullPath + "\\Options.ini";
			int length = 0;
			File check = new File(OptionsPath);
			
			//Create
			if (!check.exists() && !check.isDirectory()) {
				if (game == 1) 
					BFME1.createOptions(fullPath, resolution);
				else if (game == 2)
					BFME2.createOptions(fullPath, resolution);
				else if (game == 3)
					BFME3.createOptions(fullPath, resolution);
				
				if (Main.getProLang() == 1)
					JOptionPane.showMessageDialog(null, "Fertig! (Erzeugt: Options.ini in: \"" + fullPath + "\")");
				else
					JOptionPane.showMessageDialog(null, "Done! (Created: Options.ini at: \"" + fullPath + "\")");
			}
			//Edit
			else if(check.exists() && !check.isDirectory()) {
				if (game == 1) {
					length = 23;
					editOptions(fullPath, resolution, length);
				}
				else if (game == 2) {
					length = 18;
					editOptions(fullPath, resolution, length);
				}
				else if (game == 3) {
					length = 18;
					editOptions(fullPath, resolution, length);
				}
				
				if (Main.getProLang() == 1)
					JOptionPane.showMessageDialog(null, "Fertig! (Bearbeitet: Options.ini in: \"" + fullPath + "\")");
				else 
					JOptionPane.showMessageDialog(null, "Done! (Edited: Options.ini at: \"" + fullPath + "\")");
			}
		}
		
		public static void editOptions(String fullPath, String resolution, int length) throws IOException {
			//Vorbereitung 
			File f = new File(fullPath + "\\Options.ini");
			FileReader fr = new FileReader(f);
			BufferedReader br = new BufferedReader(fr);
			String s = null;
			String[] oldFile = new String[length];
			String newRes = ("Resolution = " + resolution);
			
			//Auslesen
			for (int i = 0; (s=br.readLine())!=null; i++) {
				oldFile[i] = new String(s);
				//Bearbeiten
				if (oldFile[i].startsWith("Resolution")) 
					oldFile[i] = new String(newRes);
				else if (oldFile[i].startsWith("GameSpyIPAddress"))
					oldFile[i] = new String("GameSpyIPAddress = 0");
			}
			br.close();
			
			//Schreiben
			PrintWriter writer = new PrintWriter(fullPath + "\\Options.ini", "UTF-8");
			for (int i = 0; i < (oldFile.length); i++) {
				writer.println(oldFile[i]);
			}
			writer.close();
		}
		
		public static String buildPath(String languagePath) {
			String roamingPath = System.getenv("AppData");
			String fullPath = roamingPath + languagePath;
			fullPath = fullPath.replace('/', '\\');
			
			return fullPath;
		}
		
		public static void addFolder(String fullPath) {	
			new File(fullPath).mkdir();
		}
}
