package game;

import city.cs.engine.*;
import org.jbox2d.common.Vec2;

public class Level3 {

    private final String backgroundImage = "data/background3.png";
    private final String platformImage   = "data/fire_platform.png";
    private final String groundImage     = "data/fire_platform.png";

    private final BodyImage[] fireLeftSprites = {
            new BodyImage("data/fire_left1.png", 2f)
    };
    private final BodyImage[] fireRightSprites = {
            new BodyImage("data/fire_right1.png", 2f)
    };

    public GameWorld createWorld(Game game) {
        GameWorld world = new GameWorld(game, platformImage, groundImage, false, true); // isLevel2=false
        Player player = world.getPlayer();

        // Spawn a flying patrol enemy immediately
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

        world.addStepListener(new StepListener() {
            @Override public void preStep(StepEvent e) {
                flyer.update();
            }
            @Override public void postStep(StepEvent e) {}
        });

        return world;
    }

    public String getBackgroundImage() { return backgroundImage; }
    public String getPlatformImage()   { return platformImage;   }
    public String getGroundImage()     { return groundImage;     }
}

