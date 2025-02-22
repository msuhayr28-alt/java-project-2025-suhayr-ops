package game;
import city.cs.engine.*;
import javax.swing.*;
import java.awt.*;

public class GameView extends UserView {
    //stores the background
    private Image background;
    //loads the image as the background
    public GameView(World w, int width, int height) {
        super(w, width, height);
        background = new ImageIcon("data/background1.png").getImage();
    }
    //everytime the game is run it would draw the image
    protected void paintBackground(Graphics2D g){
        g.drawImage(background, 0, 0, this.getWidth(), this.getHeight(), this);

    }
}
