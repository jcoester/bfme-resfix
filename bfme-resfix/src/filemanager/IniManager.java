package filemanager;

import gui.MainFrame;
import model.Game;
import model.Resolution;
import utility.ExceptionHandler;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static gui.MainFrame.logger;

public class IniManager {

    public static void writeNewFile(Game game, Resolution selectedResolution, ResourceBundle labels) {
        try {
            List<String> input = readLinesFromBundleFile("options/" + game.getId() + "-Options.ini", labels);
            List<String> output = overrideResolution(input, selectedResolution);
            createFolderIfRequired(game.getUserDataPath(), labels);
            writeToFile(Paths.get(game.getUserDataPath() + "/Options.ini"), output, labels);
        } catch (Exception e) {
            ExceptionHandler.getInstance().promptError(labels.getString("error.writing"), e);
        }
    }

    public static List<String> readLinesFromBundleFile(String blueprintPath, ResourceBundle labels) {
        List<String> lines = new ArrayList<>();
        try (InputStream inputStream = MainFrame.class.getClassLoader().getResourceAsStream(blueprintPath);
            BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(inputStream)))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (Exception e) {
            ExceptionHandler.getInstance().promptError(labels.getString("error.writing"), e);
        }
        return lines;
    }

    public static List<String> readLinesFromFile(Path filePath, ResourceBundle labels) {
        List<String> lines = new ArrayList<>();
        try (FileInputStream inputStream = new FileInputStream(filePath.toFile());
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (Exception e) {
            ExceptionHandler.getInstance().promptError(labels.getString("error.writing"), e);
        }
        return lines;
    }

    private static void createFolderIfRequired(Path path, ResourceBundle labels) {
        // Create Directory if required
        File directory = new File(path.toString());
        try {
            if (!directory.exists()) {
                boolean folderCreated = directory.mkdirs();
                logger.info("createFolderIfRequired: {}, {}", path, folderCreated);
            }
        } catch (Exception e) {
            ExceptionHandler.getInstance().promptError(labels.getString("error.writing"), e);
        }
    }

    public static void writeNewResolutionToFile(Path userDataPath, Resolution selectedResolution, ResourceBundle labels) {
        Path optionIni = Paths.get(userDataPath + "/Options.ini");
        try {
            List<String> result = readLinesFromFile(optionIni, labels);
            List<String> output = overrideResolution(result, selectedResolution);
            writeToFile(optionIni, output, labels);
        } catch (Exception e) {
            ExceptionHandler.getInstance().promptError(labels.getString("error.writing"), e);
        }
    }

    private static List<String> overrideResolution(List<String> lines, Resolution selectedResolution) {
        List<String> output = new LinkedList<>();
        for (String line : lines) {
            if (line.startsWith("Resolution")) {
                line = "Resolution = " + selectedResolution.getWidth() + " " + selectedResolution.getHeight();
            }
            output.add(line);
        }
        return output;
    }

    private static void writeToFile(Path path, List<String> content, ResourceBundle labels) {
        File file = new File(path.toString());
        try (FileWriter fileWriter = new FileWriter(file)) {
            for (String str : content)
                fileWriter.write(str + System.lineSeparator());
        } catch (Exception e) {
            ExceptionHandler.getInstance().promptError(labels.getString("error.writing"), e);
        }
    }
}
