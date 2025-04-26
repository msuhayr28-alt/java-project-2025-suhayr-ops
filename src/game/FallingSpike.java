package game;

import city.cs.engine.CollisionEvent;
import city.cs.engine.CollisionListener;
import city.cs.engine.DynamicBody;
import city.cs.engine.PolygonShape;
import city.cs.engine.Shape;
import city.cs.engine.BodyImage;
import city.cs.engine.World;
import org.jbox2d.common.Vec2;

/**
 * A spike that falls from above and damages the player on contact.
 * <p>
 * When it collides with the player, it deals damage and plays a sound,
 * then destroys itself. It also destroys itself on any other collision
 * except with Enemy bodies.
 */
public class FallingSpike extends DynamicBody implements CollisionListener {

    /** Global sound file path for spike destruction. */
    public static String FALLING_SOUND;

    /** The shape of the spike. */
    private static final Shape SPIKE_SHAPE = new PolygonShape(
            -0.3f, 0.5f,
            0.3f, 0.5f,
            0.0f, -0.5f
    );

    /**
     * Create a falling spike at the given position using the specified image.
     *
     * @param world         the physics world
     * @param spawnPosition where to spawn the spike
     * @param imagePath     path to the spike's image file
     */
    public FallingSpike(World world, Vec2 spawnPosition, String imagePath) {
        super(world, SPIKE_SHAPE);

        // Attach the visual representation
        addImage(new BodyImage(imagePath, 1.5f));
        setPosition(spawnPosition);

        // Make the spike fall slowly
        setGravityScale(0.5f);

        // Listen for collisions to apply damage
        addCollisionListener(this);
    }

    /**
     * Handle collisions: damage player or destroy on other impacts.
     *
     * @param e collision event
     */
    @Override
    public void collide(CollisionEvent e) {
        if (e.getOtherBody() instanceof Player) {
            // Damage the player
            ((Player) e.getOtherBody()).decreaseHealth();
            // Play destruction sound
            Sound.playSound(FALLING_SOUND);
            destroy();
        } else if (!(e.getOtherBody() instanceof Enemy)) {
            // Destroy on ground or platform
            Sound.playSound(FALLING_SOUND);
            destroy();
        }
    }
}
