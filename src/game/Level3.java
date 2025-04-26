package game;

import city.cs.engine.*;
import org.jbox2d.common.Vec2;

/**
 * Level3 configures and constructs the fire-themed game world (level 3).
 * <p>
 * It provides custom background, platform and ground images,
 * and spawns a flying patrol enemy that chases the player.
 */
public class Level3 {

    private final String backgroundImage = "data/background3.png";
    private final String platformImage   = "data/fire_platform.png";
    private final String groundImage     = "data/fire_platform.png";

    // Animated sprites for the fire patrol enemy
    private final BodyImage[] fireLeftSprites = {
            new BodyImage("data/fire_left1.png", 2f)
    };
    private final BodyImage[] fireRightSprites = {
            new BodyImage("data/fire_right1.png", 2f)
    };

    /**
     * Creates the GameWorld for level 3.
     * Spawns a flying patrol enemy in CHASE_PLAYER mode immediately.
     * @param game the main Game instance
     * @return a fully configured GameWorld for level 3
     */
    public GameWorld createWorld(Game game) {
        GameWorld world = new GameWorld(game, platformImage, groundImage, false, true);
        Player player = world.getPlayer();

        // Spawn a flying patrol enemy that chases the player
        PatrolEnemy flyer = new PatrolEnemy(
                world,
                new Vec2(player.getPosition().x, player.getPosition().y + 50),
                -10, 10,
                player,
                fireLeftSprites,
                fireRightSprites,
                150,
                PatrolEnemy.PatrolMode.CHASE_PLAYER
        );

        // Ensure the flyer is updated every physics step
        world.addStepListener(new StepListener() {
            @Override public void preStep(StepEvent e) {
                flyer.update();
            }
            @Override public void postStep(StepEvent e) {}
        });

        return world;
    }

    /** @return path to the background image for level 3 */
    public String getBackgroundImage() { return backgroundImage; }
}

