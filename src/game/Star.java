package game;

import city.cs.engine.*;

public class Star extends StaticBody {

    public Star(World world) {
        super(world, new CircleShape(0.5f));
        addImage(new BodyImage("data/star.png", 1.0f));
        setName("star");


    }
}
