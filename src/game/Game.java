package game;

import city.cs.engine.*;
import org.jbox2d.collision.Collision;
import org.jbox2d.common.Vec2;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyListener;
import java.io.File;
/**
 * Your main game entry point
 */
public class Game {

    private JLabel healthLabel;
    private JFrame frame;
    private GameWorld game;
    private JLabel scoreLabel;
    private Clip backgroundClip;
    private GameView view;
    private int currentLevel = 1;


    /**
     * Initialise a new Game.
     */
    public Game() {

        game = new GameWorld(this, "data/ground.png", "data/ground.png", false, false);

        Player player = game.getPlayer();

        // make a view to look into the game world
        view = new GameView(game, 500, 500, player, "data/background1.png");
        view.setLayout(null);


        // add PlayerController to handle movement
        PlayerController controller = new PlayerController(player);
        view.addKeyListener(controller);

        // ensures focus stays on the game view
        view.setFocusable(true);
        view.requestFocusInWindow();


        // create a Java window (frame) and add the game
        // and view it
        frame = new JFrame("Wallace's Adventure");
        frame.setLayout(new BorderLayout());
        frame.add(view, BorderLayout.CENTER);

        healthLabel = new JLabel(new ImageIcon("data/health4.png"));
        healthLabel.setBounds(460, 5, 45, 45); // Position at bottom-left
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

        Sound.playBackgroundMusic("data/background.wav");


        //optional: uncomment this to make a debugging view
        //JFrame debugView = new DebugViewer(game, 500, 500);

        scoreLabel = new JLabel("Stars: 0");
        scoreLabel.setForeground(Color.WHITE);
        scoreLabel.setBounds(10, 40, 100, 20);
        view.add(scoreLabel);

        // start our game world simulation!
        game.start();

        Timer gameTimer = new Timer(100, e -> {
            game.updatePlatform();

        });
        gameTimer.start();
    }

    public void goToNextLevel() {

        currentLevel++;

        updateScoreDisplay(0);
        updateHealthDisplay(4);
        if (game != null) {
            game.stop();
        }

        if (currentLevel == 2) {
            // Use the Level2 factory method to build the world,
            // which also wires in the patrol‐enemy spawn after 3 stars:
            Level2 level2 = new Level2();
            game = level2.createWorld(this);

            // Now hook it into the view:
            view.setWorld(game);
            view.setBackgroundImage(level2.getBackgroundImage());
            view.setPlayer(game.getPlayer());

            game.start();
        } else if (currentLevel == 3) {
            Level3 level3 = new Level3();
            game = level3.createWorld(this);

            view.setWorld(game);
            view.setBackgroundImage(level3.getBackgroundImage());
            view.setPlayer(game.getPlayer());

            game.start();
        } else {
            gameWon();
        }

        // Re-attach controls & reset star count on the new player
        Player p = game.getPlayer();

        view.setPlayer(p);
        for (KeyListener kl : view.getKeyListeners()) {
            view.removeKeyListener(kl);
        }
        view.addKeyListener(new PlayerController(p));
        view.requestFocusInWindow();
        p.resetStarCount();
    }


    public void updateHealthDisplay(int health) {
        healthLabel.setIcon(new ImageIcon("data/health" + health + ".png"));
        healthLabel.repaint(); // makes the icon change visible

    }

    public void gameOver() {

        endScreen("data/gameover.png");

    }

    public void gameWon() {

        endScreen("data/youWin.png");
    }

    private void endScreen(String imagePath) {
        // Stop the game
        game.stop();
        Sound.stopBackgroundMusic();

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
        ImageIcon icon = new ImageIcon(imagePath);
        Image scaledImage = icon.getImage().getScaledInstance(300, 200, Image.SCALE_SMOOTH);
        JLabel imageLabel = new JLabel(new ImageIcon(scaledImage));
        imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        overlay.add(Box.createVerticalGlue());
        overlay.add(imageLabel);

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

        if (backgroundClip != null && backgroundClip.isRunning()) {
            backgroundClip.stop();
        }

    }


    private void restartGame() {
        frame.dispose(); // Close the current game window
        new Game(); // Start a new game
    }

    public void updateScoreDisplay(int score) {
        scoreLabel.setText("Stars: " + score);

    }

    public GameWorld getWorld() {
        return game;
    }


    /**
     * Run the game.
     */
    public static void main(String[] args) {
        new Game();
    }


}