package game;

import city.cs.engine.*;
import org.jbox2d.common.Vec2;

/**
 * Configuration and world-creation for Level 2 (ice-themed).
 */
public class Level2 {

    private final String backgroundImage = "data/background-2.png";
    private final String platformImage   = "data/ice_platform.png";
    private final String groundImage     = "data/ice_platform.png";

    // ice-themed patrol enemy sprites (updated paths)
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
            new BodyImage("data/ice_right5.png", 2f),

    };

    /**
     * Build and return the GameWorld for level 2, wiring in a patrol enemy
     * once the player collects 3 stars.
     */
    public GameWorld createWorld(Game game) {
        GameWorld world = new GameWorld(game, platformImage, groundImage, true, false);
        Player player = world.getPlayer();

        // One-time spawn of patrol enemy after 3 stars
        world.addStepListener(new StepListener() {
            private boolean spawned = false;
            @Override public void preStep(StepEvent e) {
                if (!spawned && player.getStarCount() >= 3) {
                    spawned = true;
                    System.out.println("Spawning patrol enemy!");

                    PatrolEnemy patrol = new PatrolEnemy(
                            world,
                            new Vec2(player.getPosition().x, player.getPosition().y + 5),    // start position
                            -6, 6,              // patrol bounds
                            player,
                            iceLeftSprites,
                            iceRightSprites,
                            200,                  // frame delay ms
                            PatrolEnemy.PatrolMode.PATROL_ONLY

                    );
                    world.addStepListener(new StepListener() {
                        @Override public void preStep(StepEvent e) {
                            patrol.update();
                        }
                        @Override public void postStep(StepEvent e) { }
                    });

                }
            }
            @Override public void postStep(StepEvent e) {}
        });

        return world;
    }

    public String getBackgroundImage() { return backgroundImage; }
    public String getPlatformImage()   { return platformImage;   }
    public String getGroundImage()     { return groundImage;     }
}
