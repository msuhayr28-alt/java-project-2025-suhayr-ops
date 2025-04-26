package game;

import city.cs.engine.*;
import city.cs.engine.Shape;
import org.jbox2d.common.Vec2;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The Player class represents the character controlled by the user.
 * It includes methods for movement, jumping, health management, and star collection.
 * The player can also trigger animations for walking, idle, and jumping states.
 */
public class Player extends Walker {

    private static final BodyImage idleRightImage = new BodyImage("data/walk1.png", 4);
    private static final BodyImage idleLeftImage = new BodyImage("data/walk1-1.png", 4);

    private static final BodyImage[] walkLeftImages = {
            new BodyImage("data/walk1-1.png", 4),
            new BodyImage("data/walk2-1.png", 4),
            new BodyImage("data/walk3-1.png", 4)};

    private static final BodyImage[] walkRightImages = {
            new BodyImage("data/walk1.png", 4),
            new BodyImage("data/walk2.png", 4),
            new BodyImage("data/walk3.png", 4)};

    private static final BodyImage jumpingRightImage = new BodyImage("data/jump1.png", 4);
    private static final BodyImage jumpingLeftImage = new BodyImage("data/jump1-1.png", 4);

    private int currentFrame = 0;
    private boolean isMoving = false;
    private boolean facingRight = true;
    private Timer animationTimer; // Timer for walking animation
    private int health = 4;
    private AttachedImage currentImage;
    private Game game;
    private int starsCollected = 0;

    /**
     * Constructor for creating a new Player instance.
     * @param world the world the player will interact with
     * @param game the game that the player is part of
     */
    public Player(World world, Game game) {
        super(world);
        this.game = game;

        // Create character shapes using polygons
        Shape shape1 = new PolygonShape(-0.92f, 1.31f, -0.45f, 2.02f, 0.32f, 2.0f, 0.82f, 1.42f, 0.83f, 0.9f, 0.4f, 0.58f, -0.56f, 0.59f, -0.92f, 1.3f);
        Shape shape2 = new PolygonShape(-0.53f, 0.66f, 0.36f, 0.66f, 0.96f, -0.07f, 0.96f, -1.93f, -1.0f, -1.91f, -0.99f, 0.18f, -0.54f, 0.66f);

        new SolidFixture(this, shape1);
        new SolidFixture(this, shape2);

        // Attach initial idle image to the player
        currentImage = new AttachedImage(this, idleRightImage, 1, 0, new Vec2(0, 0));
        this.setPosition(new Vec2(0, -7));

        // Timer for handling walking animation cycles
        animationTimer = new Timer(100, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isMoving) {
                    updateAnimation();
                }
            }
        });

        // Foot sensor for detecting landing
        Sensor footSensor = new Sensor(this, new BoxShape(1f, 0.5f, new Vec2(0, -2)));
        footSensor.addSensorListener(new SensorListener() {
            @Override
            public void beginContact(SensorEvent e) {
                if (e.getContactBody() instanceof StaticBody) {
                    setIdleImage(); // Switch to idle animation when landing
                }
            }

            @Override
            public void endContact(SensorEvent e) {
                // Future use for double jumping, if implemented
            }
        });
        setGravityScale(1.5f);

        // Collision listener for star collection
        this.addCollisionListener(new CollisionListener() {
            @Override
            public void collide(CollisionEvent e) {
                if (e.getOtherBody() instanceof Star) {
                    e.getOtherBody().destroy();
                    starsCollected++;

                    Sound.playSound("data/point.wav");

                    // Update score display in the game
                    game.updateScoreDisplay(starsCollected);

                    // Enable additional game elements after collecting stars
                    if (starsCollected >= 1) {
                        game.getWorld().enableFallingSpikes();
                    }

                    // Proceed to the next level after collecting enough stars
                    if (starsCollected >= 1) {
                        game.goToNextLevel();
                    }
                }
            }
        });
    }

    /**
     * Update the walking animation based on movement direction.
     */
    public void updateAnimation() {
        if (isMoving) {
            this.removeAttachedImage(currentImage);
            BodyImage[] selectedImage = facingRight ? walkRightImages : walkLeftImages;
            currentImage = new AttachedImage(this, selectedImage[currentFrame], 1, 0, new Vec2(0, 0));
            currentFrame = (currentFrame + 1) % selectedImage.length;
        }
    }

    /**
     * Start walking the player at a given speed.
     * @param speed the speed at which to walk
     */
    public void startWalking(float speed) {
        super.startWalking(speed);
        if (speed > 0 && !facingRight) {
            facingRight = true;
            setIdleImage(); // Switch to idle animation
        } else if (speed < 0 && facingRight) {
            facingRight = false;
            setIdleImage(); // Switch to idle animation
        }
        if (!isMoving) {
            isMoving = true;
            animationTimer.start();
        }
    }

    /**
     * Stop walking the player.
     */
    public void stopWalking() {
        super.stopWalking();
        isMoving = false;
        animationTimer.stop(); // Stop walking animation
        setIdleImage();
        this.setLinearVelocity(new Vec2(0, this.getLinearVelocity().y));
    }

    /**
     * Make the player jump with a specified speed.
     * @param speed the speed at which the player jumps
     */
    public void jump(float speed) {
        super.jump(speed);
        setJumpingImage(); // Switch to jumping image
    }

    /**
     * Set the jumping animation based on the direction the player is facing.
     */
    public void setJumpingImage() {
        this.removeAttachedImage(currentImage);
        currentImage = new AttachedImage(this, facingRight ? jumpingRightImage : jumpingLeftImage, 1, 0, new Vec2(0, 0));
    }

    /**
     * Set the idle image based on the direction the player is facing.
     */
    private void setIdleImage() {
        this.removeAttachedImage(currentImage);
        currentImage = new AttachedImage(this, facingRight ? idleRightImage : idleLeftImage, 1, 0, new Vec2(0, 0));
    }

    /**
     * Decrease the player's health by 1 and trigger game over if health reaches 0.
     */
    public void decreaseHealth() {
        if (health > 1) {
            health--;
            game.updateHealthDisplay(health);
            Sound.playSound("data/hit.wav");
        } else {
            gameOver(); // Trigger game over if health is 0 or below
        }
    }

    /**
     * Reset the star count after a level completion or game restart.
     */
    public void resetStarCount() {
        starsCollected = 0;
    }

    /**
     * Trigger the game over process and display the game over screen.
     */
    private void gameOver() {
        game.gameOver();
    }

    /**
     * Get the current number of stars collected by the player.
     * @return the number of stars collected
     */
    public int getStarCount() {
        return starsCollected;
    }
}
