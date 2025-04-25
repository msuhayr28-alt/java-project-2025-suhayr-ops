package game;

import city.cs.engine.CollisionListener;
import city.cs.engine.*;
import org.jbox2d.common.Vec2;


public class FallingSpike extends DynamicBody implements CollisionListener {
    public static String fallingSound;

    private static final Shape spikeShape = new PolygonShape(
            -0.3f, 0.5f,
            0.3f, 0.5f,
            0.0f, -0.5f
    );

    public FallingSpike(World world, Vec2 spawnPosition, String imagePath) {
        super(world, spikeShape);

        // Set image
        addImage(new BodyImage(imagePath, 1.5f));
        setPosition(spawnPosition);

        // Gravity will pull it down
        setGravityScale(0.5f);

        // Add collision logic
        addCollisionListener(this);
    }

    @Override
    public void collide(CollisionEvent e) {
        if (e.getOtherBody() instanceof Player) {
            ((Player) e.getOtherBody()).decreaseHealth();  // Or however your health is handled
            Sound.playSound(String.valueOf(fallingSound));
            this.destroy();
        } else if (!(e.getOtherBody() instanceof Enemy)) {
            Sound.playSound(String.valueOf(fallingSound));
            this.destroy(); // Destroy when it hits the ground or platform
        }
    }
}
