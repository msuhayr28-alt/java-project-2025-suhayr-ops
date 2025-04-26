package game;

import city.cs.engine.*;
import org.jbox2d.common.Vec2;

/**
 * Level2 configures and constructs the ice-themed game world (level 2).
 * <p>
 * It provides custom background, platform and ground images,
 * and spawns a patrol enemy once the player collects three stars.
 */
public class Level2 {

    private final String backgroundImage = "data/background-2.png";
    private final String platformImage   = "data/ice_platform.png";
    private final String groundImage     = "data/ice_platform.png";

    // Animated sprites for the ice patrol enemy
    private final BodyImage[] iceLeftSprites = {
            new BodyImage("data/ice_left1.png", 2f),
            new BodyImage("data/ice_left2.png", 2f),
            new BodyImage("data/ice_left3.png", 2f),
            new BodyImage("data/ice_left4.png", 2f),
            new BodyImage("data/ice_left5.png", 2f)
    };
    private final BodyImage[] iceRightSprites = {
            new BodyImage("data/ice_right1.png", 2f),
            new BodyImage("data/ice_right2.png", 2f),
            new BodyImage("data/ice_right3.png", 2f),
            new BodyImage("data/ice_right4.png", 2f),
            new BodyImage("data/ice_right5.png", 2f)
    };

    /**
     * Creates the GameWorld for level 2.
     * Attaches a StepListener that spawns a patrol enemy after 3 stars are collected.
     * @param game the main Game instance
     * @return a fully configured GameWorld for level 2
     */
    public GameWorld createWorld(Game game) {
        GameWorld world = new GameWorld(game, platformImage, groundImage, true, false);
        Player player = world.getPlayer();

        world.addStepListener(new StepListener() {
            private boolean spawned = false;

            @Override
            public void preStep(StepEvent e) {
                // Spawn patrol enemy once when player has 3 or more stars
                if (!spawned && player.getStarCount() >= 3) {
                    spawned = true;
                    System.out.println("Spawning patrol enemy!");

                    PatrolEnemy patrol = new PatrolEnemy(
                            world,
                            new Vec2(player.getPosition().x, player.getPosition().y + 5),
                            -6, 6,
                            player,
                            iceLeftSprites,
                            iceRightSprites,
                            200,
                            PatrolEnemy.PatrolMode.PATROL_ONLY
                    );

                    // Ensure patrol logic runs each step
                    world.addStepListener(new StepListener() {
                        @Override public void preStep(StepEvent e) { patrol.update(); }
                        @Override public void postStep(StepEvent e) {}
                    });
                }
            }

            @Override public void postStep(StepEvent e) { }
        });

        return world;
    }

    /** @return path to the background image for level 2 */
    public String getBackgroundImage() { return backgroundImage; }
}
