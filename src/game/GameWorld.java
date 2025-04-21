package game;
import city.cs.engine.*;
import org.jbox2d.common.Vec2;

import java.util.ArrayList;
import java.util.List;

public class GameWorld extends World {

    private final Player player;
    private float lastPlatformY = -7; // Track the highest platform generated
    private List<StaticBody> platforms = new ArrayList<>(); // Store platforms
    private String platformImagePath = "data/ground.png"; // default
    private String groundImagePath = "data/ground.png"; // default ground
    private float platformImageScale = 1.6f;

    private Game game;

    private final List<StaticBody> movingPlatforms = new ArrayList<>();
    private final List<Float> platformSpeeds = new ArrayList<>();

    private float lastStarSpawnY = -10;
    private final float SPAWN_INTERVAL = 15f;
    private final float STAR_PROBABILITY = 0.4f;



    public GameWorld(Game game, String platformImg, String groundImg) {
        super();
        this.game = game;
        this.platformImagePath = platformImg;
        this.groundImagePath = groundImg;


        //creates the player
        player = new Player(this, game);
        player.setPosition(new Vec2(0, -7));

        //first platform
        Shape platformShape = new BoxShape(2f , 0.75f);
        StaticBody platform = new StaticBody(this, platformShape);
        platform.setPosition(new Vec2(5, -7));
        platform.addImage(new BodyImage(platformImagePath, platformImageScale));


        createWalls();
        createGround();
        startUpdateLoop();
    }

    private void createGround() {
        float groundWidth = 2f;
        int numOfPlatforms = 13;

        for (int i = 0; i < numOfPlatforms; i++) {
            Shape groundShape = new BoxShape(groundWidth / 2, 0.5f);
            StaticBody ground = new StaticBody(this, groundShape);
            ground.setPosition(new Vec2(-11 + (i * groundWidth), -12));
            ground.addImage(new BodyImage(groundImagePath, 2));
        }
    }



    private void createWalls(){
        //created left wall so player doesn't fall of
        Shape leftWallShape = new BoxShape(0.5f, 1000f);
        StaticBody leftWall = new StaticBody(this, leftWallShape);
        leftWall.setPosition(new Vec2(-13, -2));
        SolidFixture leftWallFixture = new SolidFixture(leftWall, leftWallShape);
        leftWallFixture.setFriction(0);
        //created right wall so player doesn't fall of
        Shape rightWallShape = new BoxShape(0.5f, 1000f);
        StaticBody rightWall = new StaticBody(this, rightWallShape);
        rightWall.setPosition(new Vec2(13, -2));
        SolidFixture rightWallFixture = new SolidFixture(rightWall, rightWallShape);
        rightWallFixture.setFriction(0);
    }
    private final List<Enemy> enemies = new ArrayList<>(); // stores enemies

    private void addPlatform(float x, float y, boolean isMoving, String imagePath, float imageScale) {
        Shape shape = new BoxShape(2f, 0.75f);
        StaticBody platform = new StaticBody(this, shape);
        platform.setPosition(new Vec2(x, y));
        platform.addImage(new BodyImage(imagePath, imageScale));

        if (isMoving) {
            movingPlatforms.add(platform);
            platformSpeeds.add(0.05f); // You can also make speed a param if you want
        } else {
            platforms.add(platform);
        }

        if (Math.random() < 0.2) {
            generateEnemyPlatform(x, y);
        }

        lastPlatformY = y;
    }


    public void updatePlatform(){
        float playerY= player.getPosition().y;
        if(playerY > lastPlatformY - 5){
            generatePlatform();
        }

        if (playerY > lastStarSpawnY + SPAWN_INTERVAL) {
            lastStarSpawnY = playerY;

            if (Math.random() < STAR_PROBABILITY) {
                spawnStarAt(new Vec2(-10 + (float)(Math.random() * 20), playerY + 3));
            }
        }

    }

    private void generatePlatform() {
        for (int i = 0; i < 5; i++) {
            float x = (float) (Math.random() * 20 - 10);
            float y = lastPlatformY + 5 + (float) (Math.random() * 3);
            boolean isMoving = Math.random() < 0.3;

            String platformImage = platformImagePath;
            float imageScale = 1.6f;

            addPlatform(x, y, isMoving, platformImage, imageScale);
        }
    }


    private void generateEnemyPlatform(float x, float y){
        Enemy enemy = new Enemy(this, new Vec2(x, y + 1.65f), player); // adjusts height so it sits on platform
        enemies.add(enemy);
    }

    public void updateMovingPlatforms(){
        for (int i = 0; i < movingPlatforms.size(); i++) {
            StaticBody platform = movingPlatforms.get(i);

            if (platform == null || platform.getWorld() == null) {
                continue; // Skip this platform
            }

            float speed = platformSpeeds.get(i);

            Vec2 pos = platform.getPosition();
            float newX = pos.x + speed;

            // reverse direction when platform reaches the boundary/wall
            if (newX > 10 || newX < -10) {
                speed *= -1;
                platformSpeeds.set(i, speed);
            }

            platform.setPosition(new Vec2(newX, pos.y));

        }
    }
    public void makeEnemiesShoot() {
        for (Enemy enemy : enemies) {
            enemy.shootProjectile();
        }
    }

    private void startUpdateLoop() {
        this.addStepListener(new StepListener() {
            private int counter = 0;

            @Override
            public void preStep(StepEvent e) {
                updatePlatform();
                updateMovingPlatforms();

                counter++;

                if (counter % 180 == 0) { // Every 120 steps (~2 seconds)
                    makeEnemiesShoot();
                }
            }

            @Override
            public void postStep(StepEvent e) {
                // Not needed right now
            }
        });
    }




    public Player getPlayer() {
        return player;
    }




    private void spawnStarAt(Vec2 position) {
        Star star = new Star(this);
        star.setPosition(position);
    }




}