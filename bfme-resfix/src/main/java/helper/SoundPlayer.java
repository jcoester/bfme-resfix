package helper;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class SoundPlayer {

    public static void playSound(String soundFileName) {
        try {
            // Load the audio resource from the JAR
            InputStream resourceStream = SoundPlayer.class.getResourceAsStream("/audio/" + soundFileName);
            if (resourceStream != null) {
                // Wrap the input stream in a BufferedInputStream
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(new BufferedInputStream(resourceStream));
                Clip clip = AudioSystem.getClip();
                clip.open(audioStream);

                // Adjust volume if needed (in decibels)
                FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                gainControl.setValue(6.0f); // Reduce volume by 10 decibels

                clip.start();

                // Debug time and source of sound
                String formattedTime = LocalTime.now().format(DateTimeFormatter.ofPattern("mm:ss.SSS"));
                System.err.println(formattedTime + " " + soundFileName);
            }
        } catch (Exception e) {
            ExceptionHandler.getInstance().promptError("playSound", e);
        }
    }
}
