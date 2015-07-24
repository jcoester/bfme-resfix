package core;

import java.io.File;

public class Ini {
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
