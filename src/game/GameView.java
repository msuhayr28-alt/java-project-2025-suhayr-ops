package game;

import city.cs.engine.*;
import org.jbox2d.common.Vec2;

import javax.swing.*;
import java.awt.*;

/**
 * The GameView class is responsible for rendering the visual representation of the game world
 * to the screen. It handles both the background image and the camera's following of the player.
 */
public class GameView extends UserView {

    private Image background;  // Background image of the game view
    private Player player;     // The player character

    /**
     * Constructor for the GameView. Initializes the view with a specified world, size, player,
     * and background image.
     *
     * @param world             The game world to be displayed in the view.
     * @param width             The width of the view window.
     * @param height            The height of the view window.
     * @param player            The player character to be followed by the camera.
     * @param backgroundImagePath The path to the background image file.
     */
    public GameView(World world, int width, int height, Player player, String backgroundImagePath) {
        super(world, width, height);
        this.player = player;
        this.background = new ImageIcon(backgroundImagePath).getImage();
    }

    /**
     * Updates the background image of the view.
     *
     * @param backgroundImagePath The path to the new background image file.
     */
    public void setBackgroundImage(String backgroundImagePath) {
        this.background = new ImageIcon(backgroundImagePath).getImage();
        repaint(); // Trigger a repaint to reflect the change
    }

    /**
     * Sets a new player character for the view.
     *
     * @param newPlayer The new player character to be set.
     */
    public void setPlayer(Player newPlayer) {
        this.player = newPlayer;
    }

    /**
     * Paints the background of the game view. This is called every time the view needs to be redrawn.
     *
     * @param g The Graphics2D object used for drawing the background.
     */
    @Override
    protected void paintBackground(Graphics2D g) {
        g.drawImage(background, 0, 0, this.getWidth(), this.getHeight(), this);
    }

    /**
     * Paints the foreground elements (e.g., player, interactive objects) in the game view.
     * This includes following the player with the camera.
     *
     * @param g The Graphics2D object used for drawing the foreground elements.
     */
    @Override
    protected void paintForeground(Graphics2D g) {
        // Define the bounds within which the camera can move
        float minY = -15;  // Minimum Y position of the camera
        float minX = 0;    // Minimum X position of the camera
        float maxX = 0;    // Maximum X position of the camera

        // Get the player's position to center the camera on them
        Vec2 playerPos = player.getPosition();

        // Only move the camera if the player's position is above the ground
        if (playerPos.y > 0) {
            float cameraY = Math.max(playerPos.y, minY);
            float cameraX = Math.max(minX, Math.min(playerPos.x, maxX));

            // Set the camera's center to the player's position
            this.setCentre(new Vec2(cameraX, cameraY));
        }
    }
}


