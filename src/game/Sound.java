package game;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

/**
 * The Sound class provides methods to play sound effects and background music in the game.
 * It uses Java's `javax.sound.sampled` package to manage audio playback.
 */
public class Sound {

    // Holds the background music clip that can be looped
    private static Clip backgroundClip;

    /**
     * Plays a sound effect once.
     *
     * @param filename the path to the audio file to be played
     */
    public static void playSound(String filename) {
        try {
            // Load the audio file
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(filename));
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream); // Prepare the clip
            clip.start(); // Play the sound effect
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace(); // Log the error if something goes wrong
        }
    }

    /**
     * Plays background music in a loop.
     *
     * @param filename the path to the audio file to be played continuously
     */
    public static void playBackgroundMusic(String filename) {
        try {
            // Load the background music audio file
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(filename));
            backgroundClip = AudioSystem.getClip(); // Get the clip object
            backgroundClip.open(audioInputStream); // Prepare the clip
            backgroundClip.loop(Clip.LOOP_CONTINUOUSLY); // Loop the music indefinitely
            backgroundClip.start(); // Start the music
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace(); // Log the error if something goes wrong
        }
    }

    /**
     * Stops the background music if it's currently playing.
     * This method is used to stop the looping background music.
     */
    public static void stopBackgroundMusic() {
        if (backgroundClip != null && backgroundClip.isRunning()) {
            backgroundClip.stop(); // Stop the music
            backgroundClip.close(); // Close the clip
        }
    }
}

