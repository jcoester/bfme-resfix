package utility;

import com.sun.jna.platform.win32.Advapi32Util;
import model.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.util.*;

import static com.sun.jna.platform.win32.WinReg.HKEY_LOCAL_MACHINE;
import static gui.MainFrame.logger;
import static model.GameID.*;
import static utility.WindowsDisplayInfo.determineAspectRatio;

public class GameInfo {

    public static List<Game> initializeGames() {
        List<Game> games = new ArrayList<>();
        for (GameID id : values()) {
            Game game = new Game();
            game.setId(id); // Value never changes
            game.setVersionAvailablePatch(retrieveVersionPatch(id)); // Value never changes
            games.add(game);
        }
        return games;
    }

    public static String retrieveRegistryPath(GameID id) {
        String basePath = "SOFTWARE\\WOW6432Node\\Electronic Arts\\";
        switch (id) {
            case BFME1: return basePath + "EA Games\\The Battle for Middle-earth";
            case BFME2: return basePath + "Electronic Arts\\The Battle for Middle-earth II";
            case ROTWK: return basePath + "Electronic Arts\\The Lord of the Rings, The Rise of the Witch-king";
            default: return null;
        }
    }

    public static String retrieveRegistryStringValue(String path, String key) {
        try {
            return Advapi32Util.registryGetValues(HKEY_LOCAL_MACHINE, path).get(key).toString();
        } catch (Exception e) {
            return null;
        }
    }

    public static Path retrieveInstallPath(String regPath) {
        String installPath = retrieveRegistryStringValue(regPath, "InstallPath");
        if (installPath != null)
            return Paths.get(installPath);
        else
            return null;
    }

    public static String retrieveLanguage(String regPath) {
        String language = retrieveRegistryStringValue(regPath, "Language");
        if (language != null && !language.isEmpty())
            return language.substring(0, 1).toUpperCase() + language.substring(1);
        else
            return null;
    }

    public static Path retrieveUserDataPath(String regPath) {
        String userDataLeafName = retrieveRegistryStringValue(regPath, "UserDataLeafName");
        if (userDataLeafName != null)
            return Paths.get(System.getenv("AppData") + "/" + userDataLeafName);
        else
            return null;
    }

    public static Version retrieveVersionInstalled(String regPath) {
        String version = retrieveRegistryStringValue(regPath, "Version");
        if (version != null)
            return new Version(Integer.parseInt(Integer.toHexString(Integer.parseInt(version))));
        else
            return null;
    }

    public static Version retrieveVersionPatch(GameID gameID) {
        switch (gameID) {
            case BFME1: return new Version(10003); // Latest Official Patch
            case BFME2: return new Version(10006); // Latest Official Patch
            case ROTWK: return new Version(20001); // Latest Official Patch
            default: return null;
        }
    }

    public static Resolution retrieveResolution(Path optionsIni) {
        try (BufferedReader br = Files.newBufferedReader(optionsIni)) {
            String st;
            while ((st = br.readLine()) != null) {
                if (st.startsWith("Resolution")) {
                    String resolution = st.split("=")[1].trim();
                    String[] wh = resolution.split(" ");
                    int w = Integer.parseInt(wh[0]);
                    int h = Integer.parseInt(wh[1]);
                    return new Resolution(w, h, determineAspectRatio(w, h));
                }
            }
            return null;
        } catch (NoSuchFileException e) {
            return null;
        } catch (Exception e) {
            logger.error("retrieveResolution( {} )", optionsIni, e);
            return null;
        }
    }

    public static String retrieveMapsBigHash(Path path) {
        if (path != null)
            return generateSHA256(new File(path.toUri()));
        else
            return null;
    }

    public static String generateSHA256(File file) {
        try (FileInputStream fis = new FileInputStream(file)) {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                digest.update(buffer, 0, bytesRead);
            }
            byte[] hashBytes = digest.digest();
            StringBuilder hexString = new StringBuilder();
            for (byte hashByte : hashBytes) {
                String hex = Integer.toHexString(0xff & hashByte);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            logger.error("generateSHA256( {} )", file, e);
            return null;
        }
    }

    public static String retrieveMapsAspectRatio(String givenHash) {
        if (givenHash == null)
            return null;

        Map<String, String> aspectRatioMap = new HashMap<>();
        aspectRatioMap.put("15056c796ff0467e76d2e1d57baaf0b0ab6d91d4a1fe9b5883b2eafe19f766a2", "4:3"); // BFME1 1.00
        aspectRatioMap.put("9885ee7341600959d1f0a40c19d4da93a80048649f1c69e3548963883fae0cdd", "4:3"); // BFME1 1.01
        aspectRatioMap.put("499d60ebd3c37d556dfbba4b0f0cb92bc893c94f8ca3d720b4a7125a91a368d4", "4:3"); // BFME1 1.02-1.03
        aspectRatioMap.put("95255431d435e90f6023d049bc847434953d14afa7cf3b877e69bbcb792b2301", "4:3"); // BFME2 1.00-1.02
        aspectRatioMap.put("73e2d9af67f84d48fdea2dbe0131d99c43f27d4ae64af3add50296c8b2298540", "4:3"); // BFME2 1.03-1.04
        aspectRatioMap.put("8ca19783c3a4969dffeb5a6493a18d6f7447a310bf8b4152b17fc7ed4307e1c8", "4:3"); // BFME2 1.05-1.06
        aspectRatioMap.put("673615e01e2f49a37e46a77c3678221bdae3c7d0e9422a5e39a6997795dd9f56", "4:3"); // ROTWK 2.00
        aspectRatioMap.put("457de594754414445332b3e59aebce8b5fea7336144144057663446e203a2260", "4:3"); // ROTWK 2.01

        aspectRatioMap.put("1db3b1fbf54f7363cad510ae80d57600e1f74edbc0b32354b03183dcd089a8ac", "16:9"); // BFME1 16:9 Mod
        aspectRatioMap.put("c0b7469bcbdb876e6c45a96fde77234e2c68bcae1de68a687c79cc0612e79f6b", "16:9"); // BFME2 16:9 Mod
        aspectRatioMap.put("e515061a2645f5f74f14c57aa7eecbf2823f4a8b3cca16a6ca01aa24521e9aee", "16:9"); // ROTWK 16:9 Mod

        return aspectRatioMap.get(givenHash);
    }

    public static Map<String, Boolean> retrieveGamesRunningStatus() {
        HashMap<String, Boolean> runningStatus = new HashMap<>();
        runningStatus.put("lotrbfme", false);
        runningStatus.put("lotrbfme2", false);
        runningStatus.put("lotrbfme2ep1", false);

        try {
            // Run the task list command to get the list of running processes
            ProcessBuilder builder = new ProcessBuilder("tasklist");
            Process process = builder.start();

            // Read the output of the command
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    for (Map.Entry<String, Boolean> game : runningStatus.entrySet()) {
                        if (line.contains(game.getKey() + ".exe"))
                            runningStatus.replace(game.getKey(), true);
                    }
                }
            }
        } catch (Exception e) {
            ExceptionHandler.getInstance().handleException("isProcessRunning", e);
        }
        return runningStatus;
    }

    static boolean retrieveGameRunningFlag(Game game, Map<String, Boolean> runningStatus) {
        switch (game.getId()) {
            case BFME1: return runningStatus.get("lotrbfme");
            case BFME2: return runningStatus.get("lotrbfme2");
            case ROTWK: return runningStatus.get("lotrbfme2ep1");
            default: return false;
        }
    }

    public static boolean isInvalidResolution(Game game, Display display) {
        return game.isInstalled() && game.getInGameResolution() != null && !display.getAllResolutions().contains(game.getInGameResolution());
    }
}
