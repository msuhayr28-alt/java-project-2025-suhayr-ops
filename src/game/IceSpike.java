package game;

import city.cs.engine.CollisionListener;
import city.cs.engine.*;
import org.jbox2d.common.Vec2;

public class IceSpike extends StaticBody implements CollisionListener {
    private static final Shape spikeShape = new PolygonShape(
            -0.75f, 0.257f,   0.2f, 0.964f,   0.8f, -0.107f,   0.75f, -0.727f,   -0.75f, -0.727f,   -0.75f, 0.257f
    );

    public IceSpike(World world, Vec2 position) {
        super(world, spikeShape);
        addImage(new BodyImage("data/obstacle.png", 1.5f));
        setPosition(position);
        addCollisionListener(this);
    }

    @Override
    public void collide(CollisionEvent e) {
        if (e.getOtherBody() instanceof Player) {
            ((Player) e.getOtherBody()).decreaseHealth();
        }
    }
}

