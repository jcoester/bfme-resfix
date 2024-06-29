package model.updateCheck;

import com.google.gson.JsonElement;

public class Release {
    public String tag_name;
    public String body;
    public JsonElement assets;
    public String fileName;
    public String fileUpdatedAt;
    public String fileDownloadURL;
}

