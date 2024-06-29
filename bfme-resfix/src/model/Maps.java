package model;

import java.nio.file.Path;
import java.util.Objects;

public class Maps {
    private Path filePath;
    private Path backupPath;
    private String hash;
    private String aspectRatio;
    private String label;
    private boolean backup;

    public Maps() {
    }

    public Maps(Path filePath, String aspectRatio, String label) {
        this.filePath = filePath;
        this.aspectRatio = aspectRatio;
        this.label = label;
    }

    public Path getFilePath() {
        return filePath;
    }

    public void setFilePath(Path filePath) {
        this.filePath = filePath;
    }

    public Path getBackupPath() {
        return backupPath;
    }

    public void setBackupPath(Path backupPath) {
        this.backupPath = backupPath;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getAspectRatio() {
        return aspectRatio;
    }

    public void setAspectRatio(String aspectRatio) {
        this.aspectRatio = aspectRatio;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public boolean isBackup() {
        return backup;
    }

    public void setBackup(boolean backup) {
        this.backup = backup;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Maps maps = (Maps) o;
        return Objects.equals(aspectRatio, maps.aspectRatio);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(aspectRatio);
    }

    @Override
    public String toString() {
        if (aspectRatio == null)
            return label;

        return aspectRatio + " " + label;
    }
}
