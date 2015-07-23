package core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import javax.swing.JOptionPane;

public class Ini {
	/*
	 * MAIN
	 */
		public static void main1(String resolution, String language) throws IOException {
			//Build Folder
			String languagePath = buildLanguage1(language);
			String fullPath = buildPath(languagePath);
			addFolder(fullPath);
			
			//Build or Edit Ini
			buildIni1(fullPath, resolution);
		}
		
		public static void main2(String resolution, String language) throws IOException {
			//Build Folder
			String languagePath = buildLanguage2(language);
			String fullPath = buildPath(languagePath);
			addFolder(fullPath);
			
			//Build or Edit Ini
			buildIni2(fullPath, resolution);
		}
		
		public static void main3(String resolution, String language) throws IOException {
			//Build Folder
			String languagePath = buildLanguage3(language);
			String fullPath = buildPath(languagePath);
			addFolder(fullPath);
			
			//Build or Edit Ini
			buildIni3(fullPath, resolution);
		}
		
	/*
	 * BuildLanguage
	 */
		public static String buildLanguage1(String language) {
			String languagePath = null;
			
			if (language.equals("Deutsch"))
				languagePath = "/Meine Die Schlacht um Mittelerde-Dateien";
			
			else if (language.equals("English"))
				languagePath = "/My Battle for Middle-earth Files";
			
			else if (language.equals("Español"))
				languagePath = "/Mis archivos de La Batalla por la Tierra Media";
			
			else if (language.equals("Français"))
				languagePath = "/La Bataille pour la Terre du Milieu";
			
			else if (language.equals("Italiano"))
				languagePath = "/File de La Battaglia per la Terra di Mezzo";
			
			else if (language.equals("Nederlands"))
				languagePath = "/Mijn Battle for Middle-earth bestanden";
			
			else if (language.equals("Norsk"))
				languagePath = "/Mine Kampen om Midgard-filer";
			
			else if (language.equals("Polski"))
				languagePath = "/Moje pliki zapisu Bitwy o Sródziemie";
			
			else if (language.equals("Svenska"))
				languagePath = "/Mina Slaget om Midgård-filer";
			
			return languagePath;
		}
		
		public static String buildLanguage2(String language) {
			String languagePath = null;
			
			if (language.equals("Deutsch"))
				languagePath = "/Meine Die Schlacht um Mittelerde™ II-Dateien";
			
			else if (language.equals("English"))
				languagePath = "/My Battle for Middle-earth(tm) II Files";
			
			else if (language.equals("Español"))
				languagePath = "/Mis archivos de La Batalla por la Tierra Media™ II";
			
			else if (language.equals("Français"))
				languagePath = "/La Bataille pour la Terre du Milieu ™ II";
			
			else if (language.equals("Italiano"))
				languagePath = "/File de La Battaglia per la Terra di Mezzo™ II";
			
			else if (language.equals("Nederlands"))
				languagePath = "/Mijn Battle for Middle-earth™ II-bestanden";
			
			else if (language.equals("Norsk"))
				languagePath = "/Mine Kampen om Midgard™ II-filer";
			
			else if (language.equals("Polski"))
				languagePath = "/Moje pliki Bitwy o Śródziemie™ II";
			
			else if (language.equals("Svenska"))
				languagePath = "/Mina Slaget om Midgård™ II-filer";
			
			return languagePath;
		}	
		
		public static String buildLanguage3(String language) {
			String languagePath = null;
			
			if (language.equals("Deutsch"))
				languagePath = "/Meine Der Herr der Ringe™, Aufstieg des Hexenkönigs™-Dateien";
			
			else if (language.equals("English"))
				languagePath = "/My The Lord of the Rings, The Rise of the Witch-king Files";
			
			else if (language.equals("Español"))
				languagePath = "/Mis archivos de El Señor de los Anillos, El Resurgir del Rey Brujo";
			
			else if (language.equals("Français"))
				languagePath = "/Mes fichiers de LSDA, L'Avènement du Roi-sorcier™";
			
			else if (language.equals("Italiano"))
				languagePath = "/File de Il Signore degli Anelli™ - L'Ascesa del Re Stregone™";
			
			else if (language.equals("Nederlands"))
				//BUG
				languagePath = "/Mijn The Lord of the Rings, The Rise of the Witch-king-bestanden";
			
			else if (language.equals("Norsk"))
				//Bug
				languagePath = "/Mine Ringenes Herre - Heksekongen-filer";
			
			else if (language.equals("Polski"))
				//Bug
				languagePath = "/Moje pliki gry Władca Pierścieni, Król Nazguli";
			
			else if (language.equals("Russian"))

				languagePath = "/Властелин Колец, Под знаменем Короля-чародея - Мои файлы";
			
			else if (language.equals("Svenska"))
				//Bug
				languagePath = "/Mina Ringarnas herre™ - Häxkungens tid™-filer";
			
			return languagePath;
		}	
		
	/*
	 * BuildPath
	 */
		public static String buildPath(String languagePath) {
			String roamingPath = System.getenv("AppData");
			String fullPath = roamingPath + languagePath;
			fullPath = fullPath.replace('/', '\\');
			
			return fullPath;
		}
		
	/*
	 * AddFolder
	 */
		public static void addFolder(String fullPath) {	
			boolean success = (new File(fullPath)).mkdir();
			if(success) {
				System.out.println("created");
			}
		}
		
	/*
	 * BuildIni
	 */
		public static void buildIni1(String fullPath, String resolution) throws IOException {
			String OptionsPath = fullPath + "\\Options.ini";
			
			File check = new File(OptionsPath);
			if(check.exists() && !check.isDirectory()) {
				//Editiere
				editOptions1(fullPath, resolution);
				JOptionPane.showMessageDialog(null, "Done! (Edited Options.ini at: \"" + fullPath + "\")");
			}
			else if (!check.exists() && !check.isDirectory()) {
				//Mache eine neue Datei
				createOptions1(fullPath, resolution);
				JOptionPane.showMessageDialog(null, "Done! (Created Options.ini at: \"" + fullPath + "\")");
			}
		}
		
		public static void buildIni2(String fullPath, String resolution) throws IOException {
			String OptionsPath = fullPath + "\\Options.ini";
			
			File check = new File(OptionsPath);
			if(check.exists() && !check.isDirectory()) {
				//Editiere
				editOptions2(fullPath, resolution);
				JOptionPane.showMessageDialog(null, "Done! (Edited Options.ini at: \"" + fullPath + "\")");
			}
			else if (!check.exists() && !check.isDirectory()) {
				//Mache eine neue Datei
				createOptions2(fullPath, resolution);
				JOptionPane.showMessageDialog(null, "Done! (Created Options.ini at: \"" + fullPath + "\")");
			}
		}
		
		public static void buildIni3(String fullPath, String resolution) throws IOException {
			String OptionsPath = fullPath + "\\Options.ini";
			
			File check = new File(OptionsPath);
			if(check.exists() && !check.isDirectory()) {
				//Editiere
				editOptions3(fullPath, resolution);
				JOptionPane.showMessageDialog(null, "Done! (Edited Options.ini at: \"" + fullPath + "\")");
			}
			else if (!check.exists() && !check.isDirectory()) {
				//Mache eine neue Datei
				createOptions3(fullPath, resolution);
				JOptionPane.showMessageDialog(null, "Done! (Created Options.ini at: \"" + fullPath + "\")");
			}
		}
		
	/*
	 * CreateOptions
	 */
		public static void createOptions1(String fullPath, String resolution) throws FileNotFoundException, UnsupportedEncodingException {
			PrintWriter writer = new PrintWriter(fullPath + "\\Options.ini", "UTF-8");
			writer.println("AllHealthBars = yes");
			writer.println("AlternateMouseSetup = no");
			writer.println("AmbientVolume = 50");
			writer.println("AudioLOD = High");
			writer.println("Brightness = 50");
			writer.println("FixedStaticGameLOD = Low");
			writer.println("FlashTutorial = 0");
			writer.println("GameSpyIPAddress = 0");
			writer.println("HasSeenLogoMovies = yes");
			writer.println("HeatEffects = no");
			writer.println("IdealStaticGameLOD = Low");
			writer.println("IsThreadedLoad = yes");
			writer.println("MovieVolume = 70");
			writer.println("MusicVolume = 70");
			writer.println("Resolution = " + resolution);
			writer.println("SFXVolume = 70");
			writer.println("ScrollFactor = 50");
			writer.println("SendDelay = no");
			writer.println("StaticGameLOD = Low");
			writer.println("TimesInGame = 151");
			writer.println("UnitDecals = no");
			writer.println("UseEAX3 = no");
			writer.println("VoiceVolume = 70");
			writer.close();	
		}
		
		public static void createOptions2(String fullPath, String resolution) throws FileNotFoundException, UnsupportedEncodingException {
			PrintWriter writer = new PrintWriter(fullPath + "\\Options.ini", "UTF-8");
			writer.println("AllHealthBars = yes");
			writer.println("AmbientVolume = 50.000000");
			writer.println("AudioLOD = Low");
			writer.println("Brightness = 50");
			writer.println("FlashTutorial = 0");
			writer.println("GameSpyIPAddress = 0");
			writer.println("HasSeenLogoMovies = yes");
			writer.println("IdealStaticGameLOD = Low");
			writer.println("MovieVolume = 70.000000");
			writer.println("MusicVolume = 70.000000");
			writer.println("Resolution = " + resolution);
			writer.println("SFXVolume = 70.000000");
			writer.println("ScrollFactor = 50");
			writer.println("SendDelay = no");
			writer.println("StaticGameLOD = Low");
			writer.println("TimesInGame = 150");
			writer.println("UseEAX3 = no");
			writer.println("VoiceVolume = 70.000000");
			writer.close();	
		}
		
		public static void createOptions3(String fullPath, String resolution) throws FileNotFoundException, UnsupportedEncodingException {
			PrintWriter writer = new PrintWriter(fullPath + "\\Options.ini", "UTF-8");
			writer.println("AllHealthBars = yes");
			writer.println("AmbientVolume = 50.000000");
			writer.println("AudioLOD = Low");
			writer.println("Brightness = 50");
			writer.println("FlashTutorial = 0");
			writer.println("GameSpyIPAddress = 0");
			writer.println("HasSeenLogoMovies = yes");
			writer.println("IdealStaticGameLOD = High");
			writer.println("MovieVolume = 70.000000");
			writer.println("MusicVolume = 70.000000");
			writer.println("Resolution = " + resolution);
			writer.println("SFXVolume = 70.000000");
			writer.println("ScrollFactor = 50");
			writer.println("SendDelay = no");
			writer.println("StaticGameLOD = High");
			writer.println("TimesInGame = 105");
			writer.println("UseEAX3 = no");
			writer.println("VoiceVolume = 70.000000");
			writer.close();	
		}
		
		/*
		 * EditOptions
		 */
		
		public static void editOptions1(String fullPath, String resolution) throws IOException {
			//Vorbereitung 
			File f = new File(fullPath + "\\Options.ini");
			FileReader fr = new FileReader(f);
			BufferedReader br = new BufferedReader(fr);
			String s = null;
			String[] oldFile;
			oldFile = new String[23];
			
			//Auslesen
			for (int i = 0; (s=br.readLine())!=null; i++) {
				oldFile[i] = new String(s);
				//System.out.println(oldFile[i]);
			}
			br.close();
			
			//Überarbeiten
			String newRes = ("Resolution = " + resolution);
			oldFile[14] = new String(newRes);
					
			//Neu schreiben
			PrintWriter writer = new PrintWriter(fullPath + "\\Options.ini", "UTF-8");		
			for (int i = 0; i < oldFile.length; i++) {
				writer.println(oldFile[i]);
			}
			writer.close();
		}
		
		public static void editOptions2(String fullPath, String resolution) throws IOException {
			//Vorbereitung 
			File f = new File(fullPath + "\\Options.ini");
			FileReader fr = new FileReader(f);
			BufferedReader br = new BufferedReader(fr);
			String s = null;
			String[] oldFile;
			oldFile = new String[18];
			
			//Auslesen
			for (int i = 0; (s=br.readLine())!=null; i++) {
				oldFile[i] = new String(s);
				//System.out.println(oldFile[i]);
			}
			br.close();
			
			//Überarbeiten
			String newRes = ("Resolution = " + resolution);
			oldFile[10] = new String(newRes);
					
			//Neu schreiben
			PrintWriter writer = new PrintWriter(fullPath + "\\Options.ini", "UTF-8");		
			for (int i = 0; i < oldFile.length; i++) {
				writer.println(oldFile[i]);
			}
			writer.close();
		}

		public static void editOptions3(String fullPath, String resolution) throws IOException {
			//Vorbereitung 
			File f = new File(fullPath + "\\Options.ini");
			FileReader fr = new FileReader(f);
			BufferedReader br = new BufferedReader(fr);
			String s = null;
			String[] oldFile;
			oldFile = new String[18];
			
			//Auslesen
			for (int i = 0; (s=br.readLine())!=null; i++) {
				oldFile[i] = new String(s);
				//System.out.println(oldFile[i]);
			}
			br.close();
			
			//Überarbeiten
			String newRes = ("Resolution = " + resolution);
			oldFile[10] = new String(newRes);
					
			//Neu schreiben
			PrintWriter writer = new PrintWriter(fullPath + "\\Options.ini", "UTF-8");		
			for (int i = 0; i < oldFile.length; i++) {
				writer.println(oldFile[i]);
			}
			writer.close();
		}
}
