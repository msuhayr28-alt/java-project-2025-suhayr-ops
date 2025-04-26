package game;

import city.cs.engine.*;
import org.jbox2d.common.Vec2;

import java.util.ArrayList;
import java.util.List;

/**
 * The GameWorld class configures and manages the game world, including platform generation,
 * enemy spawning, collectibles, and level-specific behaviors.
 */
public class GameWorld extends World {

    private final Player player;
    private final Game game;

    // Platform generation state
    private float lastPlatformY = -7;
    private final List<StaticBody> platforms = new ArrayList<>();
    private final List<StaticBody> movingPlatforms = new ArrayList<>();
    private final List<Float> platformSpeeds = new ArrayList<>();
    private String platformImagePath;
    private String groundImagePath;
    private float platformImageScale;
    private final boolean isLevel2;
    private final boolean isLevel3;

    // Star spawning configuration
    private float lastStarSpawnY = -10;
    private static final float STAR_SPAWN_INTERVAL = 15f;
    private static final float STAR_PROBABILITY = 0.9f;

    // Falling spike configuration
    private static final String[] SPIKE_IMAGES = {"data/spike1.png", "data/spike2.png", "data/spike3.png"};
    private boolean fallingSpikesEnabled = false;
    private int spikeTimer = 0;
    private int spikeInterval = 180;

    // Active enemies in the world
    private final List<Enemy> enemies = new ArrayList<>();

    /**
     * Constructs a new GameWorld.
     *
     * @param game            Reference to the main Game controller
     * @param platformImg     File path for platform image
     * @param groundImg       File path for ground image
     * @param isLevel2        True if this is level 2 (ice-themed)
     * @param isLevel3        True if this is level 3 (fire-themed)
     */
    public GameWorld(Game game, String platformImg, String groundImg, boolean isLevel2, boolean isLevel3) {
        super();
        this.game = game;
        this.platformImagePath = platformImg;
        this.groundImagePath = groundImg;
        this.platformImageScale = 1.6f;
        this.isLevel2 = isLevel2;
        this.isLevel3 = isLevel3;

        // Initialize player
        this.player = new Player(this, game);
        this.player.setPosition(new Vec2(0, -7));

        // Create static geometry
        createInitialPlatform();
        createWalls();
        createGround();

        // Begin update loop
        startUpdateLoop();
    }

    /**
     * Create the first platform under the player.
     */
    private void createInitialPlatform() {
        BoxShape shape = new BoxShape(2f, 0.75f);
        StaticBody platform = new StaticBody(this, shape);
        platform.setPosition(new Vec2(5, -7));
        platform.addImage(new BodyImage(platformImagePath, platformImageScale));
    }

    /**
     * Create the ground tiles across the bottom of the world.
     */
    private void createGround() {
        float tileWidth = 2f;
        for (int i = 0; i < 13; i++) {
            BoxShape shape = new BoxShape(tileWidth / 2, 0.5f);
            StaticBody ground = new StaticBody(this, shape);
            ground.setPosition(new Vec2(-11 + i * tileWidth, -12));
            ground.addImage(new BodyImage(groundImagePath, 2));
        }
    }

    /**
     * Create invisible walls on left and right to confine the player.
     */
    private void createWalls() {
        createWall(-13);
        createWall(13);
    }

    /**
     * Create a single wall at the given x-coordinate.
     *
     * @param x The x-coordinate of the wall to be created.
     */
    private void createWall(float x) {
        BoxShape shape = new BoxShape(0.5f, 1000f);
        StaticBody wall = new StaticBody(this, shape);
        wall.setPosition(new Vec2(x, -2));
        new SolidFixture(wall, shape).setFriction(0);
    }

    /**
     * Add a platform at the specified (x,y) coordinates. Optionally, make it moving, and apply level-specific effects.
     *
     * @param x        The x-coordinate of the platform
     * @param y        The y-coordinate of the platform
     * @param moving   If true, the platform will be moving
     */
    private void addPlatform(float x, float y, boolean moving) {
        BoxShape shape = new BoxShape(2f, 0.75f);
        StaticBody platform = new StaticBody(this, shape);
        platform.setPosition(new Vec2(x, y));
        platform.addImage(new BodyImage(platformImagePath, platformImageScale));
        SolidFixture fixture = new SolidFixture(platform, shape);

        if (isLevel2) {
            if (Math.random() < 0.4f) {
                addSnowOverlay(platform);
                fixture.setFriction(1);
            }
            maybeSpawnStaticSpike(x, y);
        }

        if (moving) {
            movingPlatforms.add(platform);
            platformSpeeds.add(0.05f);
        } else {
            platforms.add(platform);
        }

        if (Math.random() < 0.2) {
            spawnEnemyOnPlatform(x, y);
        }

        lastPlatformY = y;
    }

    /**
     * Attach a snow image overlay to a platform.
     *
     * @param platform The platform to which the snow overlay will be attached.
     */
    private void addSnowOverlay(StaticBody platform) {
        BodyImage snow = new BodyImage("data/snow_overlay.png", platformImageScale);
        new AttachedImage(platform, snow, 1, 0, new Vec2(0, 1f));
    }

    /**
     * Randomly spawn a static ice spike above the platform.
     *
     * @param x The x-coordinate of the platform
     * @param y The y-coordinate of the platform
     */
    private void maybeSpawnStaticSpike(float x, float y) {
        if (Math.random() < 0.15) {
            float offsetX = (Math.random() < 0.5) ? x - 1.3f : x + 1.3f;
            Vec2 pos = new Vec2(offsetX, y + 1.5f);
            new IceSpike(this, pos);
        }
    }

    /**
     * Spawn an enemy on top of a platform.
     *
     * @param x The x-coordinate of the platform
     * @param y The y-coordinate of the platform
     */
    private void spawnEnemyOnPlatform(float x, float y) {
        Vec2 pos = new Vec2(x, y + (isLevel3 ? 2.2f : 1.65f));
        BodyImage[] sprites;
        if (isLevel2) {
            sprites = new BodyImage[]{new BodyImage("data/enemy_ice1.png", 2),
                    new BodyImage("data/enemy_ice2.png", 2),
                    new BodyImage("data/enemy_ice3.png", 2)};
        } else if (isLevel3) {
            sprites = new BodyImage[]{new BodyImage("data/flameShooter1.png", 4)};
        } else {
            sprites = new BodyImage[]{new BodyImage("data/enemy1.png", 2)};
        }
        String shot = isLevel2 ? "data/ice_shot.png" : isLevel3 ? "data/fireball.png" : "data/shot.png";
        Enemy enemy = new Enemy(this, pos, player, sprites, shot);
        enemies.add(enemy);
    }

    /**
     * Update platforms and spawn stars as the player ascends.
     */
    public void updatePlatform() {
        float py = player.getPosition().y;
        if (py > lastPlatformY - 5) generatePlatforms(5);
        if (py > lastStarSpawnY + STAR_SPAWN_INTERVAL) {
            lastStarSpawnY = py;
            if (Math.random() < STAR_PROBABILITY) {
                spawnStar(new Vec2(randomX(), py + 3));
            }
        }
    }

    /**
     * Generate multiple platforms above the highest one.
     *
     * @param count The number of platforms to generate.
     */
    private void generatePlatforms(int count) {
        for (int i = 0; i < count; i++) {
            float x = randomX();
            float y = lastPlatformY + 5 + (float) (Math.random() * 3);
            addPlatform(x, y, Math.random() < 0.3);
        }
    }

    /**
     * Return a random X-position within world bounds.
     *
     * @return The random X-coordinate.
     */
    private float randomX() {
        return -10 + (float) (Math.random() * 20);
    }

    /**
     * Set up the main physics step listener to drive world updates.
     */
    private void startUpdateLoop() {
        addStepListener(new StepListener() {
            int counter;
            @Override public void preStep(StepEvent e) {
                updatePlatform();
                updateMovingPlatforms();
                if (++counter % 180 == 0) makeEnemiesShoot();
                if (fallingSpikesEnabled && ++spikeTimer >= spikeInterval) {
                    spawnRandomFallingSpike();
                    spikeTimer = 0;
                }
            }
            @Override public void postStep(StepEvent e) {}
        });
    }

    /**
     * Move platforms marked as moving back and forth.
     */
    private void updateMovingPlatforms() {
        for (int i = 0; i < movingPlatforms.size(); i++) {
            StaticBody p = movingPlatforms.get(i);
            float speed = platformSpeeds.get(i);
            Vec2 pos = p.getPosition();
            float nx = pos.x + speed;
            if (nx > 10 || nx < -10) {
                speed = -speed;
                platformSpeeds.set(i, speed);
            }
            p.setPosition(new Vec2(nx, pos.y));
        }
    }

    /**
     * Make each enemy fire a projectile.
     */
    private void makeEnemiesShoot() {
        for (Enemy e : enemies) {
            e.shootProjectile();
        }
    }

    /**
     * Spawn a collectible star at the given position.
     *
     * @param pos The position where the star will be spawned.
     */
    private void spawnStar(Vec2 pos) {
        Star star = new Star(this);
        star.setPosition(pos);
    }

    /**
     * Spawn a falling spike from above the player.
     */
    private void spawnRandomFallingSpike() {
        String img = SPIKE_IMAGES[(int) (Math.random() * SPIKE_IMAGES.length)];
        if (isLevel2) FallingSpike.FALLING_SOUND = "data/spikeShatter.wav";
        else if (isLevel3) FallingSpike.FALLING_SOUND = "data/fireSound.wav";
        new FallingSpike(this, new Vec2(randomX(), player.getPosition().y + 15), img);
    }

    /**
     * Enable falling spikes behavior for ice or fire levels.
     */
    public void enableFallingSpikes() {
        if (!fallingSpikesEnabled && (isLevel2 || isLevel3)) {
            fallingSpikesEnabled = true;
            spikeInterval = isLevel3 ? 100 : 200;
        }
    }

    /**
     * @return The player object in this world.
     */
    public Player getPlayer() { return player; }
}
