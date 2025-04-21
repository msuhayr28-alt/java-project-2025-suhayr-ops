package game;

import city.cs.engine.*;
import org.jbox2d.common.Vec2;

import javax.swing.*;
import java.awt.*;

public class GameView extends UserView {

    private Image background;
    private Player player;

    public GameView(World world, int width, int height, Player player, String backgroundImagePath) {
        super(world, width, height);
        this.player = player;
        this.background = new ImageIcon(backgroundImagePath).getImage();
    }

    // Setter to update background image
    public void setBackgroundImage(String backgroundImagePath) {
        this.background = new ImageIcon(backgroundImagePath).getImage();
        repaint(); // Trigger repaint to reflect the change
    }
    public void setPlayer(Player newPlayer) {
        this.player = newPlayer;
    }




    @Override
    protected void paintBackground(Graphics2D g) {
        g.drawImage(background, 0, 0, this.getWidth(), this.getHeight(), this);
    }

    @Override
    protected void paintForeground(Graphics2D g) {
        float minY = -15;
        float minX = 0;
        float maxX = 0;

        Vec2 playerPos = player.getPosition();

        if (playerPos.y > 0) {
            float cameraY = Math.max(playerPos.y, minY);
            float cameraX = Math.max(minX, Math.min(playerPos.x, maxX));
            this.setCentre(new Vec2(cameraX, cameraY));
        }
    }
}

