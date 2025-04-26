package game;

import city.cs.engine.*;
import org.jbox2d.common.Vec2;

/**
 * IceSpike is a static obstacle that damages the player on contact.
 */
public class IceSpike extends StaticBody implements CollisionListener {

    /**
     * Polygon shape defining the spike collision area.
     */
    private static final Shape SPIKE_SHAPE = new PolygonShape(
            -0.75f,  0.257f,
            0.2f,   0.964f,
            0.8f,  -0.107f,
            0.75f, -0.727f,
            -0.75f, -0.727f
    );

    /**
     * Creates an ice spike at the given position in the world.
     * @param world   the physics world
     * @param position the spawn position of the spike
     */
    public IceSpike(World world, Vec2 position) {
        super(world, SPIKE_SHAPE);
        setPosition(position);
        addImage(new BodyImage("data/obstacle.png", 1.5f));
        addCollisionListener(this);
    }

    /**
     * Called when another body collides with this spike.
     * If the body is the player, reduces their health.
     */
    @Override
    public void collide(CollisionEvent e) {
        if (e.getOtherBody() instanceof Player) {
            ((Player) e.getOtherBody()).decreaseHealth();
        }
    }
}
