package core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import javax.swing.JOptionPane;

public class BFME2 {
	public static void main(String resolution, String language) throws IOException {
		//Build Folder
		String languagePath = buildLanguage(language);
		String fullPath = Ini.buildPath(languagePath);
		Ini.addFolder(fullPath);
		
		//Build or Edit Ini
		buildIni(fullPath, resolution);
	}
	
	public static String buildLanguage(String language) {
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
	
	public static void buildIni(String fullPath, String resolution) throws IOException {
		String OptionsPath = fullPath + "\\Options.ini";
		
		File check = new File(OptionsPath);
		if(check.exists() && !check.isDirectory()) {
			//Editiere
			editOptions(fullPath, resolution);
			JOptionPane.showMessageDialog(null, "Done! (Edited Options.ini at: \"" + fullPath + "\")");
		}
		else if (!check.exists() && !check.isDirectory()) {
			//Mache eine neue Datei
			createOptions(fullPath, resolution);
			JOptionPane.showMessageDialog(null, "Done! (Created Options.ini at: \"" + fullPath + "\")");
		}
	}
	
	public static void createOptions(String fullPath, String resolution) throws FileNotFoundException, UnsupportedEncodingException {
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
	
	public static void editOptions(String fullPath, String resolution) throws IOException {
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
