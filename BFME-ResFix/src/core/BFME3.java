package core;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class BFME3 {
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
	
	public static void createOptions(String fullPath, String resolution) throws FileNotFoundException, UnsupportedEncodingException {
		PrintWriter writer = new PrintWriter(fullPath + "\\Options.ini", "UTF-8");
		writer.println("AllHealthBars = yes");
		writer.println("AmbientVolume = 50.000000");
		writer.println("AudioLOD = Low");
		writer.println("Brightness = 50");
		writer.println("FlashTutorial = 0");
		writer.println("GameSpyIPAddress = 0");
		writer.println("HasSeenLogoMovies = yes");
		writer.println("IdealStaticGameLOD = VeryLow");
		writer.println("MovieVolume = 70.000000");
		writer.println("MusicVolume = 70.000000");
		writer.println("Resolution = " + resolution);
		writer.println("SFXVolume = 70.000000");
		writer.println("ScrollFactor = 50");
		writer.println("SendDelay = no");
		writer.println("StaticGameLOD = VeryLow");
		writer.println("TimesInGame = 106");
		writer.println("UseEAX3 = no");
		writer.println("VoiceVolume = 70.000000");
		writer.close();	
	}
}