package game;

public class Level2 {

    private GameView view;  // Store the GameView instance

    private String backgroundImage;
    private String platformImage;
    private String groundImage;



    public Level2() {
        // Set the image paths specific to level 2
        this.backgroundImage = "data/background2.png";
        this.platformImage = "data/ice_platform.png";
        this.groundImage = "data/ice_platform.png";


    }

    // Getter methods to provide the image paths
    public String getBackgroundImage() {
        return backgroundImage;
    }

    public String getPlatformImage() {
        return platformImage;
    }

    public String getGroundImage() {
        return groundImage;
    }


    // Create and return the GameWorld
    public GameWorld createWorld(Game game) {
        return new GameWorld(game, platformImage, groundImage);
    }
}
