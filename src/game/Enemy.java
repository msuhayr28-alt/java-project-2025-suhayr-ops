/**
 * Represents an enemy in the game world that can animate through a sequence of sprites
 * and shoot projectiles at the player.
 * <p>
 * Each Enemy cycles through its provided sprite images at a fixed interval and,
 * when triggered, fires a projectile towards the player.
 * </p>
 */
package game;

import city.cs.engine.*;
import org.jbox2d.common.Vec2;
import java.util.Timer;
import java.util.TimerTask;

public class Enemy extends StaticBody {

    /** Shape used for all enemy bodies. */
    private static final Shape ENEMY_SHAPE = new BoxShape(1, 1);

    /** Animation frames for this enemy. */
    private final BodyImage[] sprites;
    /** Index of the current sprite in the animation cycle. */
    private int spriteIndex = 0;
    /** AttachedImage instance representing the currently displayed sprite. */
    private AttachedImage currentImage;
    /** Reference to the game world this enemy belongs to. */
    private final World world;
    /** Reference to the player, used for aiming projectiles. */
    private final Player player;
    /** Image path for the projectile this enemy fires. */
    private final String projectileImagePath;

    /**
     * Construct a new Enemy.
     *
     * @param world                  the physics world to which this enemy belongs
     * @param position               the initial position of the enemy
     * @param player                 the player that the enemy will target
     * @param sprites                array of BodyImage frames for the enemy animation
     * @param projectileImagePath    file path of the image used for fired projectiles
     */
    public Enemy(World world, Vec2 position, Player player, BodyImage[] sprites, String projectileImagePath) {
        super(world, ENEMY_SHAPE);
        this.world = world;
        this.player = player;
        this.sprites = sprites.clone(); // avoid external mutation
        this.projectileImagePath = projectileImagePath;

        // Set initial position and image
        setPosition(position);
        currentImage = addImage(sprites[spriteIndex]);

        // Begin cycling through animation frames
        startAnimation();
    }

    /**
     * Begin the sprite animation loop, cycling every 250ms.
     */
    private void startAnimation() {
        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                // Remove the old sprite
                removeAttachedImage(currentImage);
                // Advance to next frame
                spriteIndex = (spriteIndex + 1) % sprites.length;
                // Attach the new sprite
                currentImage = addImage(sprites[spriteIndex]);
            }
        }, 0, 250);
    }

    /**
     * Fire a projectile towards the player, if both world and player exist.
     * The projectile will travel at a fixed speed towards the player's current position.
     */
    public void shootProjectile() {
        if (player == null || getWorld() == null) {
            return;
        }

        Vec2 enemyPos = getPosition();
        Vec2 playerPos = player.getPosition();
        Vec2 direction = playerPos.sub(enemyPos);
        direction.normalize();
        direction.mulLocal(15f); // set projectile speed

        // Spawn the projectile slightly offset from enemy position
        new Projectile(getWorld(), enemyPos.add(new Vec2(1, 1)), direction, projectileImagePath);
    }
}
