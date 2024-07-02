package model;

import java.nio.file.Path;
import java.util.Objects;

public class HUD {
    private Path filePath;
    boolean isOriginal;
    private String label;

    public HUD(Path filePath, boolean isOriginal, String label) {
        this.filePath = filePath;
        this.label = label;
        this.isOriginal = isOriginal;
    }

    public HUD() {
    }

    public Path getFilePath() {
        return filePath;
    }

    public void setFilePath(Path filePath) {
        this.filePath = filePath;
    }

    public boolean isOriginal() {
        return isOriginal;
    }

    public void setOriginal(boolean original) {
        isOriginal = original;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HUD hud = (HUD) o;
        return isOriginal == hud.isOriginal;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(isOriginal);
    }

    @Override
    public String toString() {
        return label;
    }
}
