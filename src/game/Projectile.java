package game;

import city.cs.engine.*;
import org.jbox2d.common.Vec2;

/**
 * The Projectile class represents a projectile that moves through the world and can collide with other bodies.
 * The projectile applies an initial velocity and listens for collisions, reacting based on the type of body it collides with.
 */
public class Projectile extends DynamicBody implements CollisionListener {

    private static final Shape projectileShape = new CircleShape(0.4f); // Shape of the projectile (a circle)
    // private static final BodyImage projectileImage = new BodyImage("data/shot.png", 0.8f); // Image for the projectile (optional)

    /**
     * Constructor for the Projectile class.
     *
     * @param world the world in which the projectile exists
     * @param position the initial position of the projectile
     * @param velocity the initial velocity of the projectile
     * @param imagePath the path to the image that represents the projectile
     */
    public Projectile(World world, Vec2 position, Vec2 velocity, String imagePath) {
        super(world, projectileShape);
        addImage(new BodyImage(imagePath, 0.95f)); // Add the image for the projectile
        setPosition(position); // Set the initial position
        setLinearVelocity(velocity); // Apply the initial velocity

        // Listen for collisions with other bodies
        addCollisionListener(this);
    }

    /**
     * This method is called whenever a collision occurs.
     * If the projectile collides with a player, the player's health is decreased.
     * The projectile is destroyed after collision, regardless of the type of body it collides with,
     * except for the enemy.
     *
     * @param e the CollisionEvent that contains details about the collision
     */
    @Override
    public void collide(CollisionEvent e) {
        if (e.getOtherBody() instanceof Player) {
            Player player = (Player) e.getOtherBody();
            player.decreaseHealth(); // Decrease player health on collision

            destroy(); // Destroy the projectile after it hits the player
        } else if (!(e.getOtherBody() instanceof Enemy)) {
            destroy(); // Destroy the projectile if it collides with anything other than an enemy
        }
    }
}
