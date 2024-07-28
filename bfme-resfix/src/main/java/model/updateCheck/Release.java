package model.updateCheck;

import com.google.gson.JsonElement;

public class Release {
    private String tag_name;
    private String body;
    private JsonElement assets;
    private String fileName;
    private String fileUpdatedAt;
    private String fileDownloadURL;

    public String getTag_name() {
        return tag_name;
    }

    public void setTag_name(String tag_name) {
        this.tag_name = tag_name;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public JsonElement getAssets() {
        return assets;
    }

    public void setAssets(JsonElement assets) {
        this.assets = assets;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileUpdatedAt() {
        return fileUpdatedAt;
    }

    public void setFileUpdatedAt(String fileUpdatedAt) {
        this.fileUpdatedAt = fileUpdatedAt;
    }

    public String getFileDownloadURL() {
        return fileDownloadURL;
    }

    public void setFileDownloadURL(String fileDownloadURL) {
        this.fileDownloadURL = fileDownloadURL;
    }
}

