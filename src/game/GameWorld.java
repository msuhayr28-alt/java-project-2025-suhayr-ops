package game;
import city.cs.engine.*;
import org.jbox2d.common.Vec2;

public class GameWorld extends World {
    private final Player player;

    public GameWorld() {
        super();
        player = new Player(this);
        player.setPosition(new Vec2(0, -7));

        float groundWidth = 2f;
        int numOfPlatforms = 13;
        //image im using for the floor is too small to fit the width of the screen
        //so the loop displays the image at regular intervals
        for(int i = 0; i <numOfPlatforms; i++) {
            Shape groundShape = new BoxShape(groundWidth / 2, 0.5f);
            StaticBody ground = new StaticBody(this, groundShape);
            //starting position for the first platform
            ground.setPosition(new Vec2(-11 + (i *groundWidth), -12));
            ground.addImage(new BodyImage("data/ground.png", 2));
        }
        Shape Platform = new BoxShape(groundWidth , 1f);
        StaticBody platform = new StaticBody(this, Platform);
        platform.setPosition(new Vec2(5, -7));
        platform.addImage(new BodyImage("data/ground.png", 2));

    }

    public Player getPlayer() {
       return player;
    }

}
