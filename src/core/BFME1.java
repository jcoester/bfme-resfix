package core;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

class BFME1 {
	static String buildLanguage(String language) {
		String languagePath = null;
		
		switch (language) {
		case "Deutsch":
			languagePath = "/Meine Die Schlacht um Mittelerde-Dateien";
			break;
		case "English":
			languagePath = "/My Battle for Middle-earth Files";
			break;
		case "Español":
			languagePath = "/Mis archivos de La Batalla por la Tierra Media";
			break;
		case "Français":
			languagePath = "/La Bataille pour la Terre du Milieu";
			break;
		case "Italiano":
			languagePath = "/File de La Battaglia per la Terra di Mezzo";
			break;
		case "Nederlands":
			languagePath = "/Mijn Battle for Middle-earth bestanden";
			break;
		case "Norsk":
			languagePath = "/Mine Kampen om Midgard-filer";
			break;
		case "Polski":
			languagePath = "/Moje pliki zapisu Bitwy o Sródziemie";
			break;
		case "Svenska":
			languagePath = "/Mina Slaget om Midgård-filer";
			break;
		}
		
		return languagePath;
	}
	
	static void createOptions(String fullPath, String resolution) throws FileNotFoundException, UnsupportedEncodingException {
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
		writer.println("TimesInGame = 152");
		writer.println("UnitDecals = no");
		writer.println("UseEAX3 = no");
		writer.println("VoiceVolume = 70");
		writer.close();	
	}
}
