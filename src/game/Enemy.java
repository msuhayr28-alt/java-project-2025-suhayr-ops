package game;

import city.cs.engine.*;
import org.jbox2d.common.Vec2;
import java.util.Timer;
import java.util.TimerTask;

public class Enemy extends StaticBody {

    private static final Shape enemyShape = new BoxShape(1, 1);
    private BodyImage[] sprites;
    private int spriteIndex = 0;
    private AttachedImage currentImage;
    private final World world;
    private final Player player;
    private String projectileImagePath;


    public Enemy(World world, Vec2 position, Player player, BodyImage[] sprites, String projectileImagePath) {
        super(world, enemyShape);
        this.world = world;
        this.player = player;
        this.sprites = sprites;
        this.projectileImagePath = projectileImagePath;

        setPosition(position);
        currentImage = addImage(sprites[spriteIndex]);
        startAnimation();
    }

    private void startAnimation() {
        new Timer().scheduleAtFixedRate(
                new TimerTask() {
                    @Override
                    public void run() {
                        removeAttachedImage(currentImage);
                        spriteIndex = (spriteIndex + 1) % sprites.length;
                        currentImage = addImage(sprites[spriteIndex]);
                    }
                }, 0, 250
        );
    }

    void shootProjectile() {
        if (player == null || this.getWorld() == null) return;

        Vec2 enemyPos = getPosition();
        Vec2 playerPos = player.getPosition();
        Vec2 direction = playerPos.sub(enemyPos);
        direction.normalize();
        direction.mulLocal(15f); // projectile speed

        new Projectile(this.getWorld(), enemyPos.add(new Vec2(1, 1)), direction, projectileImagePath);
    }
}