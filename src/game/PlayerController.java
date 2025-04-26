package game;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * The PlayerController class listens to key events and translates them into actions for the Player.
 * It manages movement (left, right) and jumping (space or up arrow key).
 */
public class PlayerController implements KeyListener {

    private final Player player;
    private static final float JUMP_FORCE = 15; // Force applied when the player jumps
    private static final float WALK_FORCE = 15; // Speed at which the player walks

    /**
     * Constructor for the PlayerController class.
     * @param player the Player object that this controller will control
     */
    public PlayerController(Player player) {
        this.player = player;
    }

    /**
     * Handles key press events. This method is called when a key is pressed.
     * Depending on the key pressed, the player will start moving or jumping.
     *
     * @param e the KeyEvent that represents the key press
     */
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT) {
            player.startWalking(-WALK_FORCE); // Move left
        } else if (key == KeyEvent.VK_RIGHT) {
            player.startWalking(WALK_FORCE); // Move right
        } else if (key == KeyEvent.VK_UP || key == KeyEvent.VK_SPACE) {
            if (player.getLinearVelocity().y < 6f) {
                player.jump(JUMP_FORCE); // Jump if the player is not already moving too fast vertically
            }
        }
    }

    /**
     * Handles key release events. This method is called when a key is released.
     * If the player was moving left or right, they will stop walking when the key is released.
     *
     * @param e the KeyEvent that represents the key release
     */
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_LEFT) {
            player.stopWalking(); // Stop walking when the left or right key is released
        }
    }

    /**
     * This method is called when a key is typed. It's not used in this case but must be implemented for the KeyListener interface.
     *
     * @param e the KeyEvent that represents the key typed
     */
    public void keyTyped(KeyEvent e) {
        // Not used in this implementation
    }
}
