package model;

import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.Objects;

public class Resolution implements Comparable<Resolution> {
    private final int width;
    private final int height;
    private final String aspectRatio;
    private String label;

    public Resolution(int width, int height, String aspectRatio) {
        this.width = width;
        this.height = height;
        this.aspectRatio = aspectRatio;
    }

    public Resolution(int width, int height, String aspectRatio, String label) {
        this.width = width;
        this.height = height;
        this.aspectRatio = aspectRatio;
        this.label = label;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public String getAspectRatio() {
        return aspectRatio;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Resolution that = (Resolution) o;
        return width == that.width && height == that.height;
    }

    @Override
    public int hashCode() {
        return Objects.hash(width, height);
    }

    @Override
    public int compareTo(@NotNull Resolution res) {
        return Comparator
                .comparingInt(Resolution::getWidth)
                .thenComparingInt(Resolution::getHeight)
                .compare(this, res);
    }

    @Override
    public String toString() {
        if (width == -1)
            return label;

        if (label == null)
            return width + " x " + height + " (" + aspectRatio + ")";

        return width + " x " + height + " (" + aspectRatio + ") -- " + label;
    }
}
