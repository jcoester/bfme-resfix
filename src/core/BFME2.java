package core;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

class BFME2 {
	static String buildLanguage(String language) {
		String languagePath = null;
		
		switch (language) {
		case "Deutsch":
			languagePath = "/Meine Die Schlacht um Mittelerde™ II-Dateien";
			break;
		case "English":
			languagePath = "/My Battle for Middle-earth(tm) II Files";
			break;
		case "Español":
			languagePath = "/Mis archivos de La Batalla por la Tierra Media™ II";
			break;
		case "Français":
			languagePath = "/La Bataille pour la Terre du Milieu ™ II";
			break;
		case "Italiano":
			languagePath = "/File de La Battaglia per la Terra di Mezzo™ II";
			break;
		case "Nederlands":
			languagePath = "/Mijn Battle for Middle-earth™ II-bestanden";
			break;
		case "Norsk":
			languagePath = "/Mine Kampen om Midgard™ II-filer";
			break;
		case "Polski":
			languagePath = "/Moje pliki Bitwy o Śródziemie™ II";
			break;
		case "Svenska":
			languagePath = "/Mina Slaget om Midgård™ II-filer";
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
		writer.println("IdealStaticGameLOD = Low");
		writer.println("MovieVolume = 70.000000");
		writer.println("MusicVolume = 70.000000");
		writer.println("Resolution = " + resolution);
		writer.println("SFXVolume = 70.000000");
		writer.println("ScrollFactor = 50");
		writer.println("SendDelay = no");
		writer.println("StaticGameLOD = Low");
		writer.println("TimesInGame = 152");
		writer.println("UseEAX3 = no");
		writer.println("VoiceVolume = 70.000000");
		writer.close();	
	}
}
