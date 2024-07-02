package model;

import java.util.List;
import java.util.Objects;

public class Display {
    private Resolution recommendedRes;
    private List<Resolution> allResolutions;

    public Display() {
    }

    public Resolution getRecommendedRes() {
        return recommendedRes;
    }

    public void setRecommendedRes(Resolution recommendedRes) {
        this.recommendedRes = recommendedRes;
    }

    public List<Resolution> getAllResolutions() {
        return allResolutions;
    }

    public void setAllResolutions(List<Resolution> allResolutions) {
        this.allResolutions = allResolutions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Display display = (Display) o;
        return Objects.equals(recommendedRes, display.recommendedRes) && Objects.equals(allResolutions, display.allResolutions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(recommendedRes, allResolutions);
    }

    @Override
    public String toString() {
        return "Display{" +
                "recommendedRes=" + recommendedRes +
                ", allResolutions=" + allResolutions +
                '}';
    }
}
