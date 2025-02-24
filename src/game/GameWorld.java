package game;
import city.cs.engine.*;
import org.jbox2d.common.Vec2;

import java.util.ArrayList;
import java.util.List;

public class GameWorld extends World {
    private final Player player;
    private float lastPlatformY = -7; // Track the highest platform generated
    private List<StaticBody> platforms = new ArrayList<>(); // Store platforms

    public GameWorld() {
        super();
        //creates the player
        player = new Player(this);
        player.setPosition(new Vec2(0, -7));


        float groundWidth = 2f;
        int numOfPlatforms = 13;

        //the image im using for the floor is too small to fit the width of the screen
        //so the loop displays the image at regular intervals
        for(int i = 0; i <numOfPlatforms; i++) {
            Shape groundShape = new BoxShape(groundWidth / 2, 0.5f);
            StaticBody ground = new StaticBody(this, groundShape);
            //starting position for the first platform
            ground.setPosition(new Vec2(-11 + (i *groundWidth), -12));
            ground.addImage(new BodyImage("data/ground.png", 2));
        }
        Shape Platform = new BoxShape(1.5f , 1f);
        StaticBody platform = new StaticBody(this, Platform);
        platform.setPosition(new Vec2(5, -7));
        platform.addImage(new BodyImage("data/ground.png", 2));

        //created left wall so player doesn't fall of
        Shape leftWallShape = new BoxShape(0.5f, 1000f);
        StaticBody leftWall = new StaticBody(this, leftWallShape);
        leftWall.setPosition(new Vec2(-13, -2));
        //created right wall so player doesn't fall of
        Shape rightWallShape = new BoxShape(0.5f, 1000f);
        StaticBody rightWall = new StaticBody(this, rightWallShape);
        rightWall.setPosition(new Vec2(13, -2));

    }
    private void addPlatform(float x, float y){
        Shape Platform = new BoxShape(2f, 0.75f);
        StaticBody platform = new StaticBody(this, Platform);
        platform.setPosition(new Vec2(x, y));
        platform.addImage(new BodyImage("data/ground.png", 1.6f));
        lastPlatformY= y;
        platforms.add(platform);
    }

    public void updatePlatform(){
        float playerY= player.getPosition().y;
        if(playerY > lastPlatformY - 5){
            generatePlatform();
        }
    }

    private void generatePlatform(){
        for(int i = 0; i < 3; i++){
            float x = (float) (Math.random() * 10 - 5);
            float y = lastPlatformY + 5 + (float) (Math.random() * 3);
            addPlatform(x, y);
        }
    }

    public Player getPlayer() {
       return player;
    }


}
