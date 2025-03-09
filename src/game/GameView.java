package game;
import city.cs.engine.*;
import org.jbox2d.common.Vec2;

import javax.swing.*;
import java.awt.*;

public class GameView extends UserView {
    //stores the background
    private Image background;
    private Player player;
    //loads the image as the background
    public GameView(World w, int width, int height, Player player) {
        super(w, width, height);
        this.player = player;
        background = new ImageIcon("data/background1.png").getImage();
    }

    protected void paintForeground(Graphics2D g) {

        float minY = -15;  // minimum Y level (ground level)
        float minX = 0;  // minimum X level
        float maxX = 0;  // maximum X level


        // gets player's position
        Vec2 playerPos = player.getPosition();

        if (playerPos.y > 0) {
            // ensures the camera stays within the boundaries
            float cameraY = Math.max(playerPos.y, minY);
            float cameraX = Math.max(minX, Math.min(playerPos.x, maxX));
            // set the camera position
            this.setCentre(new Vec2(cameraX, cameraY));
        }


    }


    //everytime the game is run it would draw the image
    protected void paintBackground(Graphics2D g){
        g.drawImage(background, 0, 0, this.getWidth(), this.getHeight(), this);

    }

}
