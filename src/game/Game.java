package game;

import city.cs.engine.*;

import javax.swing.JFrame;



/**
 * Your main game entry point
 */
public class Game {


    /** Initialise a new Game. */
    public Game() {

        // make an empty game world
        GameWorld game = new GameWorld();
        Player player = game.getPlayer();

        // make a view to look into the game world
        GameView view = new GameView(game, 500, 500, player);



        // add PlayerController to handle movement
        PlayerController controller = new PlayerController(player);
        view.addKeyListener(controller);

        // ensures focus stays on the game view
        view.setFocusable(true);
        view.requestFocusInWindow();


        // create a Java window (frame) and add the game
        // and view it
        final JFrame frame = new JFrame("Robo Run: Factory Escape");
        frame.add(view);


        
        // enable the frame to quit the application
        // when the x button is pressed
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationByPlatform(true);
        // don't let the frame be resized
        frame.setResizable(false);
        // size the frame to fit the world view
        frame.pack();
        // finally, make the frame visible
        frame.setVisible(true);

        //optional: uncomment this to make a debugging view
        JFrame debugView = new DebugViewer(game, 500, 500);

        // start our game world simulation!
        game.start();

        // game Loop to Update Platforms
        while (true) {
            game.updatePlatform(); // add more platforms when needed
            try {
                Thread.sleep(100); // small delay to prevent too many updates
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    /** Run the game. */
    public static void main(String[] args) {
        new Game();
    }
}
