package game;
import city.cs.engine.*;
import org.jbox2d.common.Vec2;


public class Player extends Walker {
    public Player(World world){
        super(world);

        // character was not convex, so I had to create two convex shapes and attach them to each other
        Shape shape1 = new PolygonShape(-0.92f,1.31f, -0.45f,2.02f, 0.32f,2.0f, 0.82f,1.42f, 0.83f,0.9f, 0.4f,0.58f, -0.56f,0.59f, -0.92f,1.3f
        );
        Shape shape2 = new PolygonShape(-0.53f,0.66f, 0.36f,0.66f, 0.96f,-0.07f, 0.96f,-1.93f, -1.0f,-1.91f, -0.99f,0.18f, -0.54f,0.66f
        );

        new SolidFixture(this, shape1);
        new SolidFixture(this, shape2);

        //attached image to shape and set staring position
        this.addImage(new BodyImage("data/walk1.png", 4));
        this.setPosition(new Vec2(0,-7));


    }


}
