package helper;

import com.sun.jna.platform.win32.GDI32;
import com.sun.jna.platform.win32.WinDef;
import model.Resolution;

import java.awt.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static main.Main.logger;

/**
 * <p>This <code>WindowsDisplayInfo</code> class retrieves the current <code>Windows Resolution(s)</code>, <code>Aspect ratio(s)</code>,
 * and <code>Scale factor</code> throughout a <code>Java</code> applications' runtime.</p>
 *
 * <p>It uses <code>Java AWT</code> and the <code>Java Native Access (JNA) platform</code>
 * to access the <code>Display Device Context (DC)</code> from <code>Microsoft
 * Windows graphics device interface (GDI)</code></p>
 *
 * @implNote Requires <code>net.java.dev.jna.platform (Maven)</code>
 * <p>Full support for <code>Java 8</code>: Value updates throughout runtime</p>
 * <p>Partial support for <code>Java 11+</code>: Value from application startup</p>
 *
 * @author     <a href="https://github.com/jcoester/Java-WindowsDisplayInfo/">jcoester</a>
 * @version    1.2 (2024 May. 05)
 */
public class WindowsDisplayInfo {

    private static final int HORZ_RES = 8;
    private static final int VERT_RES = 10;
    private static final int DESKTOP_VERT_RES = 117;
    private static final int DESKTOP_HORZ_RES = 118;

    public static void main(String[] args) {
        // For demonstration: Check Windows Display every 5 seconds
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(WindowsDisplayInfo::update, 0, 5, TimeUnit.SECONDS);
    }

    private static void update() {
        // Usage
        System.out.println("Scale (double)  : " + retrieveScaleFactor()); // e.g. 1.0, 1.25, 1.5, 2.0
        System.out.println("Scale ( int% )  : " + retrieveScaleFactorPercentage()); // e.g. 100%, 125%, 150%, 200%
        System.out.println("Native          : " + retrieveNativeResolution()); // Native
        System.out.println("Adjusted        : " + retrieveAdjustedResolution()); // Native, adjusted by scale factor
        System.out.println("Maximum         : " + retrieveMaximumResolution()); // Maximum
        System.out.println("All (Ascending) : " + retrieveAllResolutions(true)); // List of all, sorted smallest to largest
        System.out.println("All (Descending): " + retrieveAllResolutions(false)); // List of all, sorted largest to smallest
        System.out.println(); // Empty line for easier readability during demonstration
    }

    public static double retrieveScaleFactor() {
        WinDef.HDC hdc = null;

        try {
            // Using AWT, retrieve the initial scaleFactor from application startup
            double awtScale = Toolkit.getDefaultToolkit().getScreenResolution() / 96.0f;

            // Retrieve HDC (Handle to Device Context (DC))
            hdc = GDI32.INSTANCE.CreateCompatibleDC(null);
            if (hdc == null)
                return 0;

            // Using HDC, detect changes to the scaleFactor during the runtime
            double a = GDI32.INSTANCE.GetDeviceCaps(hdc, VERT_RES);
            double b = GDI32.INSTANCE.GetDeviceCaps(hdc, DESKTOP_VERT_RES);
            if (a == 0 || b == 0)
                return 0;

            // Offset DESKTOP_VERT_RES with the initial AWT scaleFactor
            b = b * awtScale;

            // Calculate scaleFactor
            double scaleFactor = a > b ? a / b : b / a;

            // Round to two decimals and return
            return BigDecimal.valueOf(scaleFactor).setScale(2, RoundingMode.HALF_UP).doubleValue();

        } catch (Exception e) {
            // Handle Exception
            logger.error("retrieveScaleFactor():", e);
        } finally {
            // Close HDC (Handle to Device Context (DC))
            if (hdc != null) {
                GDI32.INSTANCE.DeleteDC(hdc);
            }
        }
        return 0;
    }

    public static int retrieveScaleFactorPercentage() {
        return (int) (retrieveScaleFactor() * 100);
    }

    public static Resolution retrieveNativeResolution() {
        WinDef.HDC hdc = null;

        try {
            // Retrieve HDC (Handle to Device Context (DC))
            hdc = GDI32.INSTANCE.CreateCompatibleDC(null);
            if (hdc == null)
                return null;

            // Using HDC, detect changes to the scaleFactor during the runtime
            double a = GDI32.INSTANCE.GetDeviceCaps(hdc, DESKTOP_HORZ_RES);
            double b = GDI32.INSTANCE.GetDeviceCaps(hdc, DESKTOP_VERT_RES);
            if (a == 0 || b == 0)
                return null;

            // Return Native Resolution
            return new Resolution((int) a, (int) b, determineAspectRatio((int) a, (int) b));

        } catch (Exception e) {
            // Handle Exception
            logger.error("retrieveNativeResolution():", e);
        } finally {
            // Close HDC (Handle to Device Context (DC))
            if (hdc != null) {
                GDI32.INSTANCE.DeleteDC(hdc);
            }
        }
        return null;
    }

    public static Resolution retrieveEffectiveResolution() {
        WinDef.HDC hdc = null;

        try {
            // Using AWT, retrieve the initial scaleFactor from application startup
            double awtScale = Toolkit.getDefaultToolkit().getScreenResolution() / 96.0f;

            // Retrieve HDC (Handle to Device Context (DC))
            hdc = GDI32.INSTANCE.CreateCompatibleDC(null);
            if (hdc == null)
                return null;

            // Using HDC, detect changes to the scaleFactor during the runtime
            double a = GDI32.INSTANCE.GetDeviceCaps(hdc, HORZ_RES);
            double b = GDI32.INSTANCE.GetDeviceCaps(hdc, VERT_RES);
            if (a == 0 || b == 0)
                return null;

            // Offset HORZ_RES and VERT_RES with the initial AWT scaleFactor
            a = a / awtScale;
            b = b / awtScale;

            // Return Resolution
            return new Resolution((int) a, (int) b, determineAspectRatio((int) a, (int) b));

        } catch (Exception e) {
            // Handle Exception
            logger.error("retrieveEffectiveResolution():", e);
        } finally {
            // Close HDC (Handle to Device Context (DC))
            if (hdc != null) {
                GDI32.INSTANCE.DeleteDC(hdc);
            }
        }
        return null;
    }

    public static Resolution retrieveAdjustedResolution() {
        Resolution effective = retrieveEffectiveResolution();
        List<Resolution> allResolutions = retrieveAllResolutions(true);

        // If Effective is valid
        if (allResolutions.contains(effective))
            return effective;

        // If Effective is not valid, look for closest larger resolution
        for (Resolution res : allResolutions) {
            // Same aspect ratio
            if (effective != null && res.getAspectRatio().equals(effective.getAspectRatio()))
                // First larger resolution
                if (res.getWidth() >= effective.getWidth())
                    return res;
        }

        return retrieveNativeResolution(); // Fallback: Return Native
    }

    public static Resolution retrieveMaximumResolution() {
        List<Resolution> resolutions = retrieveAllResolutions(false);
        return resolutions.isEmpty() ? null : resolutions.get(0);
    }

    public static List<Resolution> retrieveAllResolutions(boolean ascending) {
        Resolution minimum = new Resolution(800, 600, "4:3"); // Minimum resolution available in Windows 11

        Set<Resolution> resolutionSet = new HashSet<>(); // Set for unique resolutions
        GraphicsDevice dev = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[0]; // [0] main display

        for (DisplayMode m : dev.getDisplayModes())
            if (m.getWidth() >= minimum.getWidth())
                resolutionSet.add(new Resolution(m.getWidth(), m.getHeight(), determineAspectRatio(m.getWidth(), m.getHeight())));

        List<Resolution> sortResolutions = new ArrayList<>(resolutionSet);
        sortResolutions.sort(ascending ? Comparator.naturalOrder() : Comparator.reverseOrder());

        return sortResolutions;
    }

    /**
     * <p>Returns the aspect ratio of given width and height in "X:Y"-format </p>
     * <p>
     * e.g. <code>1024 x 768</code> > <code>4:3</code><br>
     * e.g  <code>1920 x 1080</code> > <code>16:9</code><br>
     * e.g. <code>3440 x 1440</code> > <code>21:9</code>
     * </p>
     * <p>This also works for <code>1366 x 768</code> which is mathematically not <code>16:9</code> but marketed as such.</p>
     *
     * @param width         e.g. 1920
     * @param height        e.g. 1080
     * @return aspectRatio  "16:9"
     */
    static String determineAspectRatio(int width, int height) {

        // Define aspect ratios and calculate their decimal conversions
        // List from https://en.wikipedia.org/wiki/Display_aspect_ratio
        List<String> ratios = Arrays.asList("1:1", "5:4", "4:3", "3:2", "16:10", "16:9", "17:9", "21:9", "32:9");
        List<Double> ratiosDecimals = ratioDecimalFromStrings(ratios);

        // Calculate absolute difference between given the resolution's aspect ratio and the defined list of aspect ratios
        double ratioArgs = (double) width / height;
        List<Double> ratiosDiffs = new ArrayList<>();
        for (Double ratioDec : ratiosDecimals) {
            ratiosDiffs.add(Math.abs(ratioArgs - ratioDec));
        }

        // Determine and return closest available aspect ratio
        int index = ratiosDiffs.indexOf(Collections.min(ratiosDiffs));
        return ratios.get(index);
    }

    private static List<Double> ratioDecimalFromStrings(List<String> ratios) {
        List<Double> ratiosDecimals = new LinkedList<>();
        for (String ratio : ratios) {
            int w = Integer.parseInt(ratio.split(":")[0]);
            int h = Integer.parseInt(ratio.split(":")[1]);
            ratiosDecimals.add((double) w / h);
        }
        return ratiosDecimals;
    }
}