package game;

import city.cs.engine.*;

import javax.swing.*;
import java.awt.*;

/**
 * Your main game entry point
 */
public class Game {

    private JLabel healthLabel;
    private JFrame frame;
    private GameWorld game;
    private JLabel scoreLabel;



    /** Initialise a new Game. */
    public Game() {


        // make an empty game world
        game = new GameWorld(this);
        Player player = game.getPlayer();

        // make a view to look into the game world
        GameView view = new GameView(game, 500, 500, player);
        view.setLayout(null);


        // add PlayerController to handle movement
        PlayerController controller = new PlayerController(player);
        view.addKeyListener(controller);

        // ensures focus stays on the game view
        view.setFocusable(true);
        view.requestFocusInWindow();


        // create a Java window (frame) and add the game
        // and view it
        frame = new JFrame("Robo Run: Factory Escape");
        frame.setLayout(new BorderLayout());
        frame.add(view, BorderLayout.CENTER);

        healthLabel = new JLabel(new ImageIcon("data/health4.png"));
        healthLabel.setBounds(460,5 , 45, 45); // Position at bottom-left
        view.add(healthLabel);



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
        //JFrame debugView = new DebugViewer(game, 500, 500);

        scoreLabel = new JLabel("Stars: 0");
        scoreLabel.setForeground(Color.WHITE);
        scoreLabel.setBounds(10, 40, 100, 20);
        view.add(scoreLabel);

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

    public void updateHealthDisplay(int health){
        healthLabel.setIcon(new ImageIcon("data/health" + health + ".png"));
        healthLabel.repaint(); // makes the icon change visible

    }

    public void gameOver() {
        System.out.println("Game Over!");

        // Stop the game
        game.stop();

        // Create a transparent panel over the game view
        JPanel overlay = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Draw translucent background
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setColor(new Color(0, 0, 0, 150)); // black with alpha 150
                g2d.fillRect(0, 0, getWidth(), getHeight());
                g2d.dispose();
            }
        };
        overlay.setLayout(new BoxLayout(overlay, BoxLayout.Y_AXIS));
        overlay.setOpaque(false);
        overlay.setBounds(0, 0, 500, 500); // match game view size
        overlay.setFocusable(false);

        // Add Game Over image
        ImageIcon gameOverIcon = new ImageIcon("data/gameover.png");
        Image scaledImage = gameOverIcon.getImage().getScaledInstance(300, 200, Image.SCALE_SMOOTH);
        JLabel gameOverLabel = new JLabel(new ImageIcon(scaledImage));
        gameOverLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        overlay.add(Box.createVerticalGlue());
        overlay.add(gameOverLabel);

        // Add restart button
        JButton restartButton = new JButton("Restart");
        restartButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        restartButton.addActionListener(e -> restartGame());
        overlay.add(Box.createRigidArea(new Dimension(0, 20)));
        overlay.add(restartButton);
        overlay.add(Box.createVerticalGlue());

        // Add overlay to the view
        frame.getLayeredPane().add(overlay, JLayeredPane.POPUP_LAYER);
        frame.revalidate();
        frame.repaint();
    }

    private void restartGame() {
        frame.dispose(); // Close the current game window
        new Game(); // Start a new game
    }

    public void updateScoreDisplay(int score) {
        scoreLabel.setText("Stars: " + score);

    }

    /** Run the game. */
    public static void main(String[] args) {
        new Game();
    }
}