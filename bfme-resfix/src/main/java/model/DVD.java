package model;

import java.nio.file.Path;
import java.util.Objects;

public class DVD {
    private Path filePath;
    private Path backupPath;
    private String hash;
    private String label;
    private boolean original;

    public DVD(Path filePath, boolean isOriginal, String label) {
        this.filePath = filePath;
        this.label = label;
        this.original = isOriginal;
    }

    public DVD() {

    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public Path getFilePath() {
        return filePath;
    }

    public void setFilePath(Path filePath) {
        this.filePath = filePath;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public boolean isOriginal() {
        return original;
    }

    public void setOriginal(boolean original) {
        this.original = original;
    }

    public Path getBackupPath() {
        return backupPath;
    }

    public void setBackupPath(Path backupPath) {
        this.backupPath = backupPath;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DVD dvd = (DVD) o;
        return original == dvd.original;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(original);
    }

    @Override
    public String toString() {
        return label;
    }
}
