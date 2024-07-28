import main.Main;

import javax.swing.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;

public class Launch {

    public static void main(String[] args) {
        initLocalLogPath();
        setLookAndFeel();
        Main.setup();
    }

    /**
     * Initializes the LOCALAPPDATA directory used for Logback logging.
     */
    private static void initLocalLogPath() {
        try {
            String localAppData = System.getenv("LOCALAPPDATA");
            if (localAppData != null) {
                Path logDirPath = Paths.get(localAppData, ResourceBundle.getBundle("application").getString("appName"));
                if (Files.notExists(logDirPath)) {
                    Files.createDirectory(logDirPath);
                }
                System.setProperty("appdata.local", logDirPath.toString());
            } else {
                JOptionPane.showMessageDialog(null, "LOCALAPPDATA is empty", "Error in Launch.initLocalLogPath()", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e, "Error in Launch.initLocalLogPath()", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Sets the look and feel of the application to the system look and feel.
     */
    private static void setLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e, "Error in Launch.setLookAndFeel()", JOptionPane.ERROR_MESSAGE);
        }
    }
}
