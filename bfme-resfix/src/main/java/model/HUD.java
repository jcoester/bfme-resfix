package model;

import java.nio.file.Path;
import java.util.Objects;

public class HUD {
    private Path filePath;
    private String label;
    private boolean original;

    public HUD(Path filePath, boolean isOriginal, String label) {
        this.filePath = filePath;
        this.label = label;
        this.original = isOriginal;
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
        return original;
    }

    public void setOriginal(boolean original) {
        this.original = original;
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
        return original == hud.original;
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
