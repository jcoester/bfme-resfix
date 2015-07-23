package core;

import java.awt.Dimension;
import java.awt.Toolkit;

public class Auto {
	static String Resolution() {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int width = (int) screenSize.getWidth();
		int height = (int) screenSize.getHeight();
		String currentRes = (width + "*" + height);
		return currentRes;
	}
	
	static String Language() {
		String oslang = System.getProperty("user.language");
		String currentLang;
		
		if (oslang.equals("de")) 
			currentLang = "Deutsch";
		else if (oslang.equals("es"))
			currentLang = "Español";
		else if (oslang.equals("fr"))
			currentLang = "Français";
		else if (oslang.equals("it"))
			currentLang = "Italiano";
		else if (oslang.equals("nl"))
			currentLang = "Nederlands";
		else if (oslang.equals("no"))
			currentLang = "Norsk";
		else if (oslang.equals("pl"))
			currentLang = "Polski";
		else if (oslang.equals("ru"))
			currentLang = "Russian";
		else if (oslang.equals("sv"))
			currentLang = "Svenska";
		else 
			currentLang = "English";		
		return currentLang;
	}
}
