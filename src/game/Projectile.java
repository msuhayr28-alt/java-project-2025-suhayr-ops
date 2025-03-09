package game;

import city.cs.engine.*;
import org.jbox2d.common.Vec2;

public class Projectile extends DynamicBody implements CollisionListener {

    private static final Shape projectileShape = new CircleShape(0.4f);
    private static final BodyImage projectileImage = new BodyImage("data/shot.png", 0.8f);

    public Projectile(World world, Vec2 position, Vec2 velocity) {
        super(world, projectileShape);
        addImage(projectileImage);
        setPosition(position);
        setLinearVelocity(velocity); // applies the initial velocity

        // listens for collisions
        addCollisionListener(this);
    }

    @Override
    public void collide(CollisionEvent e) {
        if (!(e.getOtherBody() instanceof Enemy)) {
            destroy();
        }
    }
}
