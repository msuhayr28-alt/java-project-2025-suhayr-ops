package game;

import city.cs.engine.*;
import org.jbox2d.common.Vec2;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * A patrol enemy that either paces between two X-bounds or chases the player.
 */
public class PatrolEnemy extends Walker {

    private final float leftBound;
    private final float rightBound;
    private final Player player;
    private final BodyImage[] walkLeftImages;
    private final BodyImage[] walkRightImages;
    private final int frameDelay;
    private final PatrolMode mode;

    private int currentFrame = 0;
    private boolean facingRight = true;
    private boolean chasing = false;

    private Timer animationTimer;
    private AttachedImage currentImage;

    /**
     * Patrol behavior modes.
     */
    public enum PatrolMode {
        PATROL_ONLY,    // only move back-and-forth within bounds
        CHASE_PLAYER    // actively fly toward the player
    }

    /**
     * Construct a PatrolEnemy.
     *
     * @param world        the physics world
     * @param spawn        initial spawn position
     * @param leftBound    leftmost X coordinate to patrol
     * @param rightBound   rightmost X coordinate to patrol
     * @param player       reference to the Player
     * @param walkLeft     left-walking animation frames
     * @param walkRight    right-walking animation frames
     * @param frameDelay   ms between animation frames
     * @param mode         patrol behavior mode
     */
    public PatrolEnemy(
            World world,
            Vec2 spawn,
            float leftBound,
            float rightBound,
            Player player,
            BodyImage[] walkLeft,
            BodyImage[] walkRight,
            int frameDelay,
            PatrolMode mode
    ) {
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

        Shape shape = new BoxShape(1, 1);
        new SolidFixture(this, shape);

        currentImage = new AttachedImage(this, walkRightImages[0], 1, 0, new Vec2(0, 0));
        initAnimation();
        initDetectionSensor();
        initCollisionHandler();
    }

    /**
     * Starts the animation timer cycling through frames.
     */
    private void initAnimation() {
        animationTimer = new Timer(frameDelay, new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                removeAttachedImage(currentImage);
                BodyImage[] frames = facingRight ? walkRightImages : walkLeftImages;
                currentImage = new AttachedImage(PatrolEnemy.this, frames[currentFrame], 1, 0, new Vec2(0, 0));
                currentFrame = (currentFrame + 1) % frames.length;
            }
        });
        animationTimer.start();
    }

    /**
     * Creates a sensor that triggers chase mode when the player enters.
     */
    private void initDetectionSensor() {
        float width = (rightBound - leftBound) / 2 + 2;
        Vec2 offset = new Vec2((rightBound + leftBound) / 2 - getPosition().x, 0);
        Sensor sensor = new Sensor(this, new BoxShape(width, 2, offset));
        sensor.addSensorListener(new SensorListener() {
            @Override public void beginContact(SensorEvent e) {
                if (e.getContactBody() == player) {
                    chasing = true;
                }
            }
            @Override public void endContact(SensorEvent e) { }
        });
    }

    /**
     * Handles collision with the player: damage, destroy, and schedule respawn.
     */
    private void initCollisionHandler() {
        addCollisionListener(new CollisionListener() {
            @Override public void collide(CollisionEvent e) {
                if (e.getOtherBody() == player) {
                    player.decreaseHealth();
                    destroy();
                    scheduleRespawn();
                }
            }
        });
    }

    /**
     * After the player rises 50 units, respawn a new patrol enemy.
     */
    private void scheduleRespawn() {
        final World world = getWorld();
        final float targetY = player.getPosition().y + 50;
        world.addStepListener(new StepListener() {
            private boolean respawned = false;
            @Override public void preStep(StepEvent e) {
                if (!respawned && player.getPosition().y >= targetY) {
                    respawned = true;
                    Vec2 spawn = new Vec2(player.getPosition().x, player.getPosition().y + 5);
                    PatrolEnemy fresh = new PatrolEnemy(
                            world, spawn, leftBound, rightBound,
                            player, walkLeftImages, walkRightImages,
                            frameDelay, mode
                    );
                    world.addStepListener(new StepListener() {
                        @Override public void preStep(StepEvent e) { fresh.update(); }
                        @Override public void postStep(StepEvent e) { }
                    });
                }
            }
            @Override public void postStep(StepEvent e) { }
        });
    }

    /**
     * Update called each world step: patrol or chase behavior.
     */
    public void update() {
        Vec2 pos = getPosition();
        switch (mode) {
            case PATROL_ONLY:
                if (pos.x < leftBound || pos.x > rightBound) {
                    stopWalking();
                    return;
                }
                if (chasing) {
                    float dx = player.getPosition().x - pos.x;
                    startWalking(dx > 0 ? 3 : -3);
                    facingRight = dx > 0;
                } else {
                    if (pos.x <= leftBound) {
                        startWalking(3); facingRight = true;
                    } else if (pos.x >= rightBound) {
                        startWalking(-3); facingRight = false;
                    }
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

