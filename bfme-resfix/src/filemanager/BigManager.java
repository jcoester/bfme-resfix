package filemanager;

import utility.ExceptionHandler;

import javax.swing.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ResourceBundle;

import static gui.MainFrame.logger;

public class BigManager {

    public static void downloadBigFile(URL url, Path saveDirectory, JProgressBar loadingBar, JLabel loadingText, ResourceBundle labels) {
        try {
            long fileSize = getFileSize(url, labels);
            if (fileSize == 0)
                return;

            String filename = url.getPath().substring(url.getPath().lastIndexOf('/') + 1);

            try (InputStream inputStream = url.openStream();
                 FileOutputStream outputStream = new FileOutputStream(saveDirectory.toFile())) {

                byte[] buffer = new byte[4096];
                long totalBytesRead = 0;
                int bytesRead;

                SwingUtilities.invokeLater(() -> loadingBar.setIndeterminate(false));

                // Calculate total file size in megabytes
                long fileSizeMB = fileSize / (1024 * 1024); // Convert bytes to megabytes
                if (fileSize % (1024 * 1024) != 0) { // Check if there's a remainder
                    fileSizeMB++; // Round up
                }

                final long final_fileSizeMB = fileSizeMB;

                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);

                    // Update totalBytesRead
                    totalBytesRead += bytesRead;

                    // Calculate progress in megabytes
                    long progressMB = totalBytesRead / (1024 * 1024); // Convert bytes to megabytes

                    // Update progress bar and status label on the EDT
                    SwingUtilities.invokeLater(() -> {
                        loadingBar.setValue((int) (progressMB * 100 / final_fileSizeMB)); // Convert to percentage
                        loadingText.setText(String.format("%s %s: %d MB / %d MB", labels.getString("downloading"), filename, progressMB, final_fileSizeMB));
                    });
                }

                logger.info("Successfully downloaded: {} > {}", url, saveDirectory);
            }
        } catch (Exception e) {
            ExceptionHandler.getInstance().promptError(labels.getString("error.download"), e);
        }

        SwingUtilities.invokeLater(() -> {
            loadingBar.setValue(0);
            loadingBar.setIndeterminate(true);
            loadingText.setText(".");
        });
    }

    public static long getFileSize(URL url, ResourceBundle labels) {
        try {
            return url.openConnection().getContentLengthLong();
        } catch (IOException e) {
            ExceptionHandler.getInstance().promptError(labels.getString("error.download"), e);
            return 0;
        }
    }

    public static boolean copyBigFileFromTo(Path src, Path des, ResourceBundle labels) {
        try {
            Files.copy(src, des, StandardCopyOption.REPLACE_EXISTING);
            logger.info("Successfully copied: {} > {}", src, des);
            return true;
        } catch (IOException e) {
            ExceptionHandler.getInstance().promptError(labels.getString("error.backup"), e);
            return false;
        }
    }

    public static void removeBigFile(Path path, ResourceBundle labels) {
        try {
            boolean success = Files.deleteIfExists(path);
            if (success)
                logger.info("Successfully removed: {}", path);
            else
                ExceptionHandler.getInstance().promptError(labels.getString("error.remove"), null);
        } catch (IOException e) {
            ExceptionHandler.getInstance().promptError(labels.getString("error.remove"), e);
        }
    }
}
