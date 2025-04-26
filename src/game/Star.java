package game;

import city.cs.engine.*;

/**
 * The Star class represents a collectible star object in the game.
 * It is a static object (does not move) that the player can collect to earn points or trigger events.
 */
public class Star extends StaticBody {

    /**
     * Constructs a new Star object in the given world.
     * The star is represented by a circular shape and an image.
     *
     * @param world the world where the star will be added
     */
    public Star(World world) {
        // Call the constructor of StaticBody with a CircleShape
        super(world, new CircleShape(0.5f));

        // Add an image to represent the star
        addImage(new BodyImage("data/star.png", 1.0f));

        // Set the name of the star for collision detection or identification
        setName("star");
    }
}

