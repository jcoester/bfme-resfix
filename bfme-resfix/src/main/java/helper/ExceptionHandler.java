package helper;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Bucket;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.StorageClient;
import view.MainView;

import javax.swing.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.CompletableFuture;

import static main.Main.logger;

public class ExceptionHandler {
    private static ExceptionHandler instance;
    private final MainView mainView;
    private final ResourceBundle properties;
    private final ResourceBundle labels;
    private StorageClient storageClient;

    private ExceptionHandler(MainView mainView, ResourceBundle properties, ResourceBundle labels) {
        this.mainView = mainView;
        this.properties = properties;
        this.labels = labels;

        // Setup storageClient
        try {
            InputStream serviceAccountStream = ExceptionHandler.class.getClassLoader().getResourceAsStream("firebase/bfme-resfix.json");
            if (serviceAccountStream == null) {
                System.err.println("ExceptionHandler No AuthFile provided");
                return;
            }
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccountStream))
                    .setStorageBucket(properties.getString("bucket"))
                    .build();
            FirebaseApp fireApp = FirebaseApp.initializeApp(options);
            storageClient = StorageClient.getInstance(fireApp);
        } catch (Exception e) {
            logger.error("ExceptionHandler", e);
        }
    }

    public static void setInstance(MainView mainView, ResourceBundle properties, ResourceBundle labels) {
        if (instance == null) {
            instance = new ExceptionHandler(mainView, properties, labels);
        }
    }

    public static ExceptionHandler getInstance() {
        return instance;
    }

    public void handleException(String identifier, Exception e) {
        logger.error("{}(): ", identifier, e);

        String[] options = {labels.getString("error.report.send"), labels.getString("error.report.dont")};
        int choice = JOptionPane.showOptionDialog(
                mainView,
                formatStackTrace(e),
                getErrorTitle(),
                JOptionPane.YES_NO_OPTION,
                JOptionPane.ERROR_MESSAGE,
                null,
                options,
                options[0]
        );

        if (choice == JOptionPane.YES_OPTION) {
            CompletableFuture.runAsync(this::sendErrorReport);
        }
    }

    public void promptError(String message, Exception e) {
        if (e != null)
            logger.error("{}(): ", message, e);

        String[] options = {labels.getString("error.report.send"), labels.getString("error.report.dont")};
        int choice = JOptionPane.showOptionDialog(
                mainView,
                message,
                getErrorTitle(),
                JOptionPane.YES_NO_OPTION,
                JOptionPane.ERROR_MESSAGE,
                null,
                options,
                options[0]
        );

        if (choice == JOptionPane.YES_OPTION) {
            CompletableFuture.runAsync(this::sendErrorReport);
        }
    }

    private void sendErrorReport() {
        try {
            // Generate Upload file
            long unixTimestamp = System.currentTimeMillis();
            String randomString = UUID.randomUUID().toString().substring(0, 8);
            String uploadName = properties.getString("version") + "/" + unixTimestamp + "_" + randomString + ".log";
            InputStream logFile = Files.newInputStream(Paths.get(System.getProperty("appdata.local") + "/bfme-resfix.log"));

            // Upload file
            storageClient.bucket().create(uploadName, logFile, Bucket.BlobWriteOption.userProject("bfme-resfix"));
            logger.info("{} uploaded successfully", uploadName);
            System.out.println(uploadName + " uploaded successfully");

            // Display Confirmation
            JOptionPane.showMessageDialog(mainView, labels.getString("error.report.success"));

        } catch (IOException e) {
            logger.error("sendErrorReport()", e);
            System.out.println("Upload failed.");
        }
    }

    private String getErrorTitle() {
        if (labels != null) {
            return labels.getString("error");
        } else
            return "Error";
    }

    public static String formatStackTrace(Throwable throwable) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        throwable.printStackTrace(printWriter); // Stack trace
        printWriter.flush();

        String stackTrace = stringWriter.toString();
        String[] lines = stackTrace.split(System.lineSeparator());

        StringBuilder filteredStackTrace = new StringBuilder();
        boolean firstLine = true;
        for (String line : lines) {
            // Include the first line regardless of its content
            if (firstLine) {
                filteredStackTrace.append(line).append(System.lineSeparator());
                firstLine = false;
            } else {
                // Filter out lines starting with "at java."
                if (!line.trim().startsWith("at java")) {
                    filteredStackTrace.append(line).append(System.lineSeparator());
                }
            }
        }
        return filteredStackTrace.toString();
    }
}
