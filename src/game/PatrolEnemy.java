package game;

import city.cs.engine.*;
import org.jbox2d.common.Vec2;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * A patrol enemy with configurable left/right animations and chase behavior.
 */
public class PatrolEnemy extends Walker {
    private final float leftBound, rightBound;
    private final Player player;
    private final BodyImage[] walkLeftImages;
    private final BodyImage[] walkRightImages;
    private final int frameDelay;
    private int currentFrame = 0;
    private boolean facingRight = true;
    private Timer animationTimer;
    private AttachedImage currentImage;
    private boolean chasing = false;
    private final PatrolMode mode;


    /*
     * @param world         the physics world
     * @param spawn         initial position
     * @param leftBound     left x-limit for patrol
     * @param rightBound    right x-limit for patrol
     * @param player        the player to chase
     * @param walkLeft      array of images for left-walking animation
     * @param walkRight     array of images for right-walking animation
     * @param frameDelay    milliseconds between animation frames
     */

    public enum PatrolMode {
        PATROL_ONLY,    // Level 2
        CHASE_PLAYER    // Level 3
    }

    public PatrolEnemy(World world, Vec2 spawn, float leftBound, float rightBound,
                       Player player, BodyImage[] walkLeft, BodyImage[] walkRight, int frameDelay, PatrolMode mode) {
        super(world);
        this.leftBound = leftBound;
        this.rightBound = rightBound;
        this.player = player;
        this.walkLeftImages = walkLeft;
        this.walkRightImages = walkRight;
        this.frameDelay = frameDelay;

        this.mode = mode;

        setPosition(spawn);
        setGravityScale(0);

        Shape shape = new BoxShape(1,1);
        new SolidFixture(this, shape);

        // start with first frame
        currentImage = new AttachedImage(this, walkRightImages[0], 1, 0, new Vec2(0,0));
        setupAnimationTimer();
        setupDetectionSensor();
    }

    private void setupAnimationTimer() {
        animationTimer = new Timer(frameDelay, new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                updateAnimation();
            }
        });
        animationTimer.start();
    }

    private void setupDetectionSensor() {
        float width = (rightBound - leftBound) / 2 + 2;
        Sensor detect = new Sensor(this, new BoxShape(width, 2,
                new Vec2((rightBound + leftBound) / 2 - getPosition().x, 0)));
        detect.addSensorListener(new SensorListener() {
            public void beginContact(SensorEvent e) {
                if (e.getContactBody() == player) chasing = true;
            }
            public void endContact(SensorEvent e) { }
        });
    }

    private void updateAnimation() {
        // remove old frame
        this.removeAttachedImage(currentImage);
        BodyImage[] frames = facingRight ? walkRightImages : walkLeftImages;
        currentImage = new AttachedImage(this, frames[currentFrame], 1, 0, new Vec2(0,0));
        currentFrame = (currentFrame + 1) % frames.length;
    }

    /**
     * Call every step to patrol or chase.
     */
    public void update() {
        Vec2 pos = getPosition();
        switch (mode) {
            case PATROL_ONLY:
                if (pos.x < leftBound || pos.x > rightBound) {
                    stopWalking(); // stop moving outside patrol bounds
                    return;
                }

                if (chasing) {
                    float dx = player.getPosition().x - pos.x;
                    startWalking(dx > 0 ? 5 : -5);
                    facingRight = dx > 0;
                } else {
                    if (pos.x > leftBound) { startWalking(3); facingRight = true; }
                    else if (pos.x < rightBound) { startWalking(-3); facingRight = false; }
                }
                break;

            case CHASE_PLAYER:
                Vec2 dir = player.getPosition().sub(pos);
                setLinearVelocity(new Vec2(dir.x > 0 ? 5 : -5, dir.y > 0 ? 2 : -2));
                facingRight = dir.x > 0;
                break;
        }
    }





}
