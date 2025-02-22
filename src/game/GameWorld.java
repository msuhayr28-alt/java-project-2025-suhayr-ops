package game;
//import city.cs.engine.BoxShape;
import city.cs.engine.*;
//import city.cs.engine.Shape;
//import city.cs.engine.StaticBody;
import org.jbox2d.common.Vec2;

public class GameWorld extends World {

    public GameWorld() {
        super();
        float groundWidth = 2f;
        int numOfPlatforms = 13;

        for(int i = 0; i <numOfPlatforms; i++) {
            Shape groundShape = new BoxShape(groundWidth / 2, 0.5f);
            StaticBody ground = new StaticBody(this, groundShape);

            ground.setPosition(new Vec2(-11 + (i *groundWidth), -12));
            ground.addImage(new BodyImage("data/ground.png", 2));
        }
        Shape Platform = new BoxShape(groundWidth / 2, 0.5f);
        StaticBody platform = new StaticBody(this, Platform);
        platform.setPosition(new Vec2(5, -7));
        platform.addImage(new BodyImage("data/ground.png", 2));

    }

}
