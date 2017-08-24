package core;

import java.awt.Dimension;
import java.awt.Toolkit;

class Auto {
	static String Resolution() {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int width = (int) screenSize.getWidth();
		int height = (int) screenSize.getHeight();
		return width + "*" + height;
	}
	
	static String Language() {
		String osLang = System.getProperty("user.language");
		String currentLang;
		
		switch (osLang) {
		case "de":
			currentLang = "Deutsch";
			break;
		case "es":
			currentLang = "Español";
			break;
		case "fr":
			currentLang = "Français";
			break;
		case "it":
			currentLang = "Italiano";
			break;
		case "nl":
			currentLang = "Nederlands";
			break;
		case "no":
			currentLang = "Norsk";
			break;
		case "pl":
			currentLang = "Polski";
			break;
		case "ru":
			currentLang = "Russian";
			break;
		case "sv":
			currentLang = "Svenska";
			break;
		default:
			currentLang = "English";
			break;
		}			
		return currentLang;
	}
}
