package utility;

import com.google.gson.Gson;
import model.GameID;
import model.Version;
import model.updateCheck.Asset;
import model.updateCheck.Release;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ResourceBundle;

import static gui.MainFrame.logger;

public class GitHub {

    public static Release getLatestRelease(ResourceBundle properties) {
        try {
            String response = fetch(new URL("https://api.github.com/repos/" + properties.getString("repo") + "/releases"));
            Release[] releases = new Gson().fromJson(response, Release[].class);
            Release latestRelease = releases[0]; // 0 = latest
            Asset[] assets = new Gson().fromJson(latestRelease.assets, Asset[].class);
            for (Asset asset : assets) {
                if (asset.name.endsWith(properties.getString("format"))) {
                    latestRelease.fileName = asset.name;
                    latestRelease.fileUpdatedAt = asset.updated_at;
                    latestRelease.fileDownloadURL = asset.browser_download_url;
                    break;
                }
            }
            return latestRelease;
        } catch (Exception e) {
            logger.error("Error occurred in getLatestRelease(): ", e);
            return null;
        }
    }

    public static String fetch(URL url) {
        try (InputStream input = url.openStream()) {
            InputStreamReader isr = new InputStreamReader(input, StandardCharsets.UTF_8);
            BufferedReader reader = new BufferedReader(isr);
            StringBuilder json = new StringBuilder();
            int c;
            while ((c = reader.read()) != -1) {
                json.append((char) c);
            }
            return json.toString();
        } catch (IOException e) {
            logger.error("Error occurred in fetch(): url: {}", url, e);
            return null;
        }
    }

    public static String getLocalDate(String updatedAt) {
        String[] releaseDateFields = updatedAt.split("T")[0].split("-");
        int year = Integer.parseInt(releaseDateFields[0]);
        int month = Integer.parseInt(releaseDateFields[1]);
        int day = Integer.parseInt(releaseDateFields[2]);
        LocalDate releaseDate = LocalDate.of(year, month, day);
        return releaseDate.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG));
    }

    public static URL getMapsURL(GameID id, String aspectRatio, ResourceBundle properties) {
        try {
            return new URL("https://github.com/" +
                    properties.getString("storage") + "/blob/main/Maps/" +
                    "Maps_" + id + "_" + aspectRatio.replace(":", "-") + ".big?raw=1");
        } catch (Exception e) {
            logger.error("Error in getMapsURL()", e);
            return null;
        }
    }

    public static URL getHudURL(GameID id, ResourceBundle properties) {
        try {
            return new URL("https://github.com/" +
                    properties.getString("storage") + "/blob/main/HUD/" +
                    "_" + id.toString().toLowerCase() + "-unstretched-hud.big?raw=1");
        } catch (Exception e) {
            logger.error("Error in getMapsURL()", e);
            return null;
        }
    }

    public static URL getPatchURL(GameID id, String language, Version patchVersion, ResourceBundle properties) {
        try {
            return new URL("https://github.com/" +
                    properties.getString("storage") + "/blob/main/Patches/" +
                    id + "_" + patchVersion + "_" + language + ".exe?raw=1");
        } catch (Exception e) {
            logger.error("Error in getPatchURL()", e);
            return null;
        }
    }
}
