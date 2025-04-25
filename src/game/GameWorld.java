package game;

import city.cs.engine.*;
import org.jbox2d.common.Vec2;

import java.util.ArrayList;
import java.util.List;

/**
 * A game world that handles platforms, enemies, stars, and falling spikes.
 */
public class GameWorld extends World {

    private final Player player;
    private final Game game;

    // Platform generation
    private float lastPlatformY = -7;
    private final List<StaticBody> platforms = new ArrayList<>();
    private final List<StaticBody> movingPlatforms = new ArrayList<>();
    private final List<Float> platformSpeeds = new ArrayList<>();
    private String platformImagePath;
    private String groundImagePath;
    private float platformImageScale;
    private final boolean isLevel2;
    private final boolean isLevel3;


    // Star spawning
    private float lastStarSpawnY = -10;
    private static final float STAR_SPAWN_INTERVAL = 15f;
    private static final float STAR_PROBABILITY = 0.9f;

    // Falling spikes
    private static final String[] SPIKE_IMAGES = {"data/spike1.png", "data/spike2.png", "data/spike3.png"};
    private boolean fallingSpikesEnabled = false;
    private int spikeTimer = 0;
    private int spikeInterval = 180;

    // Enemies
    private final List<Enemy> enemies = new ArrayList<>();

    public GameWorld(Game game, String platformImg, String groundImg, boolean isLevel2, boolean isLevel3) {
        super();
        this.game = game;
        this.platformImagePath = platformImg;
        this.groundImagePath = groundImg;
        this.isLevel3 = isLevel3;
        this.platformImageScale = 1.6f;
        this.isLevel2 = isLevel2;
        this.player = new Player(this, game);
        this.player.setPosition(new Vec2(0, -7));

        createInitialPlatform();
        createWalls();
        createGround();
        startUpdateLoop();
    }

    private void createInitialPlatform() {
        BoxShape shape = new BoxShape(2f, 0.75f);
        StaticBody platform = new StaticBody(this, shape);
        platform.setPosition(new Vec2(5, -7));
        platform.addImage(new BodyImage(platformImagePath, platformImageScale));
    }

    private void createGround() {
        float tileWidth = 2f;
        for (int i = 0; i < 13; i++) {
            BoxShape shape = new BoxShape(tileWidth / 2, 0.5f);
            StaticBody ground = new StaticBody(this, shape);
            ground.setPosition(new Vec2(-11 + i * tileWidth, -12));
            ground.addImage(new BodyImage(groundImagePath, 2));
        }
    }

    private void createWalls() {
        createWall(-13);
        createWall(13);
    }

    private void createWall(float x) {
        BoxShape shape = new BoxShape(0.5f, 1000f);
        StaticBody wall = new StaticBody(this, shape);
        wall.setPosition(new Vec2(x, -2));
        new SolidFixture(wall, shape).setFriction(0);
    }

    private void addPlatform(float x, float y, boolean moving) {
        BoxShape shape = new BoxShape(2f, 0.75f);
        StaticBody platform = new StaticBody(this, shape);
        platform.setPosition(new Vec2(x, y));
        platform.addImage(new BodyImage(platformImagePath, platformImageScale));
        SolidFixture fixture = new SolidFixture(platform, shape);

        if (isLevel2) {
            if(Math.random() < 0.4f) {
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

    private void addSnowOverlay(StaticBody platform) {
        BodyImage snow = new BodyImage("data/snow_overlay.png", platformImageScale);
        new AttachedImage(platform, snow, 1, 0, new Vec2(0, 1f));
    }

    private void maybeSpawnStaticSpike(float x, float y) {
        if (Math.random() < 0.15) {
            float offsetX = (Math.random() < 0.5) ? x - 1.3f : x + 1.3f;
            Vec2 pos = new Vec2(offsetX, y + 1.5f);
            new IceSpike(this, pos);
        }
    }

    private void spawnEnemyOnPlatform(float x, float y) {
        boolean lvl2 = platformImagePath.contains("ice");
        boolean lvl3 = platformImagePath.contains("fire");

        Enemy enemy = new Enemy(this, new Vec2(x, y + 1.65f), player, lvl2, lvl3 );
        enemies.add(enemy);
    }

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

    private void generatePlatforms(int count) {
        for (int i = 0; i < count; i++) {
            float x = randomX();
            float y = lastPlatformY + 5 + (float) (Math.random() * 3);
            addPlatform(x, y, Math.random() < 0.3);
        }
    }

    private float randomX() {
        return -10 + (float) (Math.random() * 20);
    }

    private void startUpdateLoop() {
        addStepListener(new StepListener() {
            int counter = 0;
            @Override public void preStep(StepEvent e) {
                updatePlatform();
                updateMovingPlatforms();
                if (++counter % 180 == 0) makeEnemiesShoot();
                if (fallingSpikesEnabled && ++spikeTimer >= spikeInterval) {
                    spawnRandomFallingSpike();
                    spikeTimer = 0;
                }
            }
            @Override public void postStep(StepEvent e) { }
        });
    }

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

    private void makeEnemiesShoot() {
        for (Enemy e : enemies) e.shootProjectile();
    }

    private void spawnStar(Vec2 pos) {
        Star star = new Star(this);
        star.setPosition(pos);
    }

    private void spawnRandomFallingSpike() {
        if(isLevel2) {
            String img = SPIKE_IMAGES[(int) (Math.random() * SPIKE_IMAGES.length)];
            FallingSpike.fallingSound = "data/spikeShatter.wav";
            new FallingSpike(this, new Vec2(randomX(), player.getPosition().y + 15), img);

        } else if (isLevel3) {
            FallingSpike.fallingSound = "data/fireSound.wav";
            new FallingSpike(this, new Vec2(randomX(), player.getPosition().y + 15), "data/fireball.png");

        }
    }

    public void enableFallingSpikes() {
        if (!fallingSpikesEnabled && (isLevel2 || isLevel3)){
            fallingSpikesEnabled = true;
            spikeInterval = 200;
        }
    }

    public Player getPlayer() { return player; }
}