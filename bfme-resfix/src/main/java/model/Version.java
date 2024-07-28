package model;

import helper.ExceptionHandler;

import java.util.Objects;

public class Version {
    private final int number;

    public Version(int number) {
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Version version = (Version) o;
        return number == version.number;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(number);
    }

    @Override
    public String toString() {
        try {
            return Integer.toString(number).replaceFirst("00", ".");
        } catch (Exception e) {
            ExceptionHandler.getInstance().promptError("Error in Version.toString()", e);
            return null;
        }
    }
}
