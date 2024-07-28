package filemanager;

import model.Game;
import helper.ExceptionHandler;
import helper.GitHub;

import javax.swing.*;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ResourceBundle;

import static main.Main.logger;

public class PatchManager {
    public static void patchGame(Game game, ResourceBundle properties, ResourceBundle labels, JProgressBar progressBar, JLabel loadingText) {
        URL url = GitHub.getPatchURL(game.getId(), game.getLanguage(), game.getVersionAvailablePatch(), properties);
        downloadAndExecutePatch(url, progressBar, loadingText, labels);
    }

    public static void downloadAndExecutePatch(URL url, JProgressBar loadingBar, JLabel loadingText, ResourceBundle labels) {
        logger.info("downloadPatch: {}", url);

        Path tempFile = null;
        InputStream inputStream = null;
        try {
            // Get the filename from the URL
            String filename = url.getPath().substring(url.getPath().lastIndexOf('/') + 1);

            // Open a connection to the URL
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            /* Get the input stream */
            inputStream = connection.getInputStream();

            // Get the content length of the file
            int contentLength = connection.getContentLength();

            // Create a temporary file to store the executable
            tempFile = Files.createTempFile(filename, ".exe");

            // Buffer for reading data from the input stream
            byte[] buffer = new byte[1024];
            int bytesRead;
            int totalBytesRead = 0;

            // Create FileOutputStream for writing to the temporary file
            try (FileOutputStream outputStream = new FileOutputStream(tempFile.toFile())) {
                // Loop to read data from the input stream and write to the temporary file

                // Calculate total file size in megabytes
                long fileSizeMB = contentLength / (1024 * 1024); // Convert bytes to megabytes
                if (contentLength % (1024 * 1024) > 0)
                    fileSizeMB++;

                SwingUtilities.invokeLater(() -> loadingBar.setIndeterminate(false));

                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);

                    // Update totalBytesRead
                    totalBytesRead += bytesRead;

                    // Calculate progress in megabytes
                    long progressMB = totalBytesRead / (1024 * 1024); // Convert bytes to megabytes
                    if (totalBytesRead % (1024 * 1024) > 0)
                        progressMB++;

                    // Update progress bar and status label on the EDT
                    long finalFileSizeMB = fileSizeMB;
                    long finalProgressMB = progressMB;
                    SwingUtilities.invokeLater(() -> {
                        loadingBar.setValue((int) (finalProgressMB * 100 / finalFileSizeMB)); // Convert to percentage
                        loadingText.setText(String.format("%s %s: %d MB / %d MB", labels.getString("downloading"), filename, finalProgressMB, finalFileSizeMB));
                    });
                }
            }

            // Make the temporary file executable
            boolean executed = tempFile.toFile().setExecutable(true);
            logger.info("executedPatch: {} {}", url, executed);

            // Execute the temporary executable
            Process process = Runtime.getRuntime().exec(tempFile.toString());

            // Wait for the process to finish
            process.waitFor();
        } catch (Exception e) {
            ExceptionHandler.getInstance().promptError(labels.getString("error.patching"), e);
        } finally {
            try {
                if (inputStream != null)
                    inputStream.close();
                if (tempFile != null)
                    Files.deleteIfExists(tempFile);
            } catch (Exception e) {
                ExceptionHandler.getInstance().promptError(labels.getString("error.patching"), e);
            }
        }
    }
}
