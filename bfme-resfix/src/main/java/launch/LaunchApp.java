package launch;

import javax.swing.*;
import gui.MainFrame;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

import static gui.MainFrame.logger;

public class LaunchApp {

    public static void main(String[] args) {
        initLocalLogPath();
        setLookAndFeel();
        launchApp();
    }

    private static void initLocalLogPath() {
        // Get the LOCALAPPDATA environment variable and construct the log directory path
        String appDataLocal = System.getenv("LOCALAPPDATA") + "\\bfme-resfix";

        // Ensure the directory exists
        if (Files.notExists(Paths.get(appDataLocal)))
            if (!new File(appDataLocal).mkdirs())
                System.err.println("Error when creating LOCALAPPDATA/bfme-resfix directory");

        // Set the system property for Logback
        System.setProperty("appdata.local", appDataLocal);
    }

    private static void setLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            logger.error("setLookAndFeel()", e);
        }
    }

    private static void launchApp() {
        SwingUtilities.invokeLater(MainFrame::new);
    }
}
