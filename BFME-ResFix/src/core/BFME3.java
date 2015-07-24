package core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import javax.swing.JOptionPane;

public class BFME3 {
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
			languagePath = "/Mijn The Lord of the Rings, The Rise of the Witch-king-bestanden";
		
		else if (language.equals("Norsk"))
			languagePath = "/Mine Ringenes Herre - Heksekongen-filer";
		
		else if (language.equals("Polski"))
			languagePath = "/Moje pliki gry Władca Pierścieni, Król Nazguli";
		
		else if (language.equals("Russian"))
			languagePath = "/Властелин Колец, Под знаменем Короля-чародея - Мои файлы";
		
		else if (language.equals("Svenska"))
			languagePath = "/Mina Ringarnas herre™ - Häxkungens tid™-filer";
		
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
		writer.println("GameSpyIPAddress = 0");
		writer.println("HasSeenLogoMovies = yes");
		writer.println("IdealStaticGameLOD = VeryLow");
		writer.println("MovieVolume = 70.000000");
		writer.println("MusicVolume = 70.000000");
		writer.println("SFXVolume = 70.000000");
		writer.println("ScrollFactor = 50");
		writer.println("SendDelay = no");
		writer.println("StaticGameLOD = High");
		writer.println("TimesInGame = 105");
		writer.println("UseEAX3 = no");
		writer.println("VoiceVolume = 70.000000");
		writer.println("Resolution = " + resolution);
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
		String newRes = ("Resolution = " + resolution);
		
		//Auslesen
		for (int i = 0; (s=br.readLine())!=null; i++) {
			oldFile[i] = new String(s);
			if (oldFile[i].startsWith("Resolution")) 
				oldFile[i] = new String(newRes);
		}
		oldFile[4] = new String("GameSpyIPAddress = 0");
		br.close();
		
		//Bearbeiten
		PrintWriter writer = new PrintWriter(fullPath + "\\Options.ini", "UTF-8");
		for (int i = 0; i < (oldFile.length-1); i++) {
			writer.println(oldFile[i]);
		}
		writer.close();
	}
}
