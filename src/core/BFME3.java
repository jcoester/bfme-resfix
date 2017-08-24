package core;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

class BFME3 {
	static String buildLanguage(String language) {
		String languagePath = null;
		
		switch (language) {
		case "Deutsch":
			languagePath = "/Meine Der Herr der Ringe™, Aufstieg des Hexenkönigs™-Dateien";
			break;
		case "English":
			languagePath = "/My The Lord of the Rings, The Rise of the Witch-king Files";
			break;
		case "Español":
			languagePath = "/Mis archivos de El Señor de los Anillos, El Resurgir del Rey Brujo";
			break;
		case "Français":
			languagePath = "/Mes fichiers de LSDA, L'Avènement du Roi-sorcier™";
			break;
		case "Italiano":
			languagePath = "/File de Il Signore degli Anelli™ - L'Ascesa del Re Stregone™";
			break;
		case "Nederlands":
			languagePath = "/Mijn The Lord of the Rings, The Rise of the Witch-king-bestanden";
			break;
		case "Norsk":
			languagePath = "/Mine Ringenes Herre - Heksekongen-filer";
			break;
		case "Polski":
			languagePath = "/Moje pliki gry Władca Pierścieni, Król Nazguli";
			break;
		case "Russian":
			languagePath = "/Властелин Колец, Под знаменем Короля-чародея - Мои файлы";
			break;
		case "Svenska":
			languagePath = "/Mina Ringarnas herre™ - Häxkungens tid™-filer";
			break;
		}
		
		return languagePath;
	}	
	
	static void createOptions(String fullPath, String resolution) throws FileNotFoundException, UnsupportedEncodingException {
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