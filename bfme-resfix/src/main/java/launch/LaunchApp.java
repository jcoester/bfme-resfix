package launch;

import javax.swing.*;
import gui.MainFrame;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class LaunchApp {

    public static void main(String[] args) {
        initLocalLogPath();
        setLookAndFeel();
        launchApp();
    }

    /**
     * Initializes the LOCALAPPDATA directory used for Logback logging.
     */
    private static void initLocalLogPath() {
        // Get the LOCALAPPDATA environment variable and construct the log directory path
        String appDataLocal = System.getenv("LOCALAPPDATA");
        if (appDataLocal != null) {
            Path logDirPath = Paths.get(appDataLocal, "bfme-resfix");

            // Ensure the directory exists
            try {
                if (Files.notExists(logDirPath)) {
                    Files.createDirectory(logDirPath);
                }
                System.setProperty("appdata.local", logDirPath.toString());
            } catch (Exception e) {
                System.err.println("Error in initLocalLogPath(): " + e);
            }
        } else {
            System.err.println("LOCALAPPDATA environment variable is not set.");
        }
    }

    /**
     * Sets the look and feel of the application to the system look and feel.
     */
    private static void setLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("Error in setLookAndFeel(): " + e);
        }
    }

    /**
     * Launches the main application frame.
     */
    private static void launchApp() {
        SwingUtilities.invokeLater(MainFrame::new);
    }
}
