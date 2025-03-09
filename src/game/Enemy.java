package game;

import city.cs.engine.*;
import org.jbox2d.common.Vec2;

import java.util.Timer;
import java.util.TimerTask;

public class Enemy extends StaticBody {

    private static final Shape enemyShape = new BoxShape(1, 1);
    private static final BodyImage[] sprites = {
            new BodyImage("data/enemy1.png", 2),
            new BodyImage("data/enemy2.png", 2),
            new BodyImage("data/enemy3.png", 2),
            new BodyImage("data/enemy4.png", 2)
    };

    private int spriteIndex = 0;
    private AttachedImage currentImage;
    private final World world;
    private final Player player;


    public Enemy(World world, Vec2 position, Player player) {
        super(world, enemyShape);
        this.world = world;
        this.player = player;

        setPosition(position);
        currentImage = addImage(sprites[spriteIndex]); // Initial sprite
        startAnimation();
        startShooting();

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
    private void startShooting() {
        new Timer().scheduleAtFixedRate(
                new TimerTask() {
                    @Override
                    public void run() {
                        shootProjectile();
                    }
                }, 1000, 3000); // fires shot every 3 seconds
    }

    private void shootProjectile() {
        if (player == null) return;

        Vec2 enemyPos = getPosition();
        Vec2 playerPos = player.getPosition();
        Vec2 direction = playerPos.sub(enemyPos);
        direction.normalize();
        direction.mulLocal(15f);

        // makes it look like shot is coming out of enemies mouth
        new Projectile(world, enemyPos.add(new Vec2(1, 1)), direction);
    }
}
