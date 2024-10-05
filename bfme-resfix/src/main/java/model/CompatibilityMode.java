package model;

import java.util.Objects;

public class CompatibilityMode {
    private String label;
    private boolean original;

    public CompatibilityMode(boolean original, String label) {
        this.original = original;
        this.label = label;
    }

    public CompatibilityMode() {

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
        CompatibilityMode that = (CompatibilityMode) o;
        return original == that.original;
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
