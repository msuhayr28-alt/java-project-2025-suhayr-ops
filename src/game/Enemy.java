package game;

import city.cs.engine.*;
import org.jbox2d.common.Vec2;

import java.util.Timer;
import java.util.TimerTask;

public class Enemy extends StaticBody {

    private static final Shape enemyShape = new BoxShape(1, 1);
    private static  BodyImage[] sprites = {
            new BodyImage("data/enemy1.png", 2),
            new BodyImage("data/enemy2.png", 2),
            new BodyImage("data/enemy3.png", 2),
            new BodyImage("data/enemy4.png", 2)
    };

    private int spriteIndex = 0;
    private AttachedImage currentImage;
    private final World world;
    private final Player player;
    private boolean isLevel2Enemy;


    public Enemy(World world, Vec2 position, Player player, boolean isLevel2Enemy) {
        super(world, enemyShape);
        this.world = world;
        this.player = player;
        this.isLevel2Enemy = isLevel2Enemy;

        if (isLevel2Enemy) {
            sprites = new BodyImage[] {
                    new BodyImage("data/enemy_ice1.png", 2),
                    new BodyImage("data/enemy_ice2.png", 2),
                    new BodyImage("data/enemy_ice3.png", 2)
            };
        } else {
            sprites = new BodyImage[] {
                    new BodyImage("data/enemy1.png", 2),
                    new BodyImage("data/enemy2.png", 2),
                    new BodyImage("data/enemy3.png", 2)
            };
        }


        setPosition(position);
        currentImage = addImage(sprites[spriteIndex]); // Initial sprite
        startAnimation();

    }

    private void startAnimation() {
        new Timer().scheduleAtFixedRate(
                new TimerTask() {
                    @Override
                    public void run() {
                        // removes the old image
                        removeAttachedImage(currentImage);
                        // updates the sprite index
                        spriteIndex = (spriteIndex + 1) % sprites.length;

                        // adds the new image
                        currentImage = addImage(sprites[spriteIndex]);
                    }
                }, 0, 250); // change sprite every 250ms
    }

    void shootProjectile() {
        if (player == null || this.getWorld() == null) return;

        Vec2 enemyPos = getPosition();
        Vec2 playerPos = player.getPosition();
        Vec2 direction = playerPos.sub(enemyPos);
        direction.normalize();
        direction.mulLocal(15f); // speed of projectile

        String imagePath = isLevel2Enemy ? "data/ice_shot.png" : "data/shot.png";


        //  makes it look like shot is coming out of enemies mouth
        new Projectile(this.getWorld(), enemyPos.add(new Vec2(1, 1)), direction, imagePath);
    }
}