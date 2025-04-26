package game;

import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyListener;

/**
 * The main entry point and controller for Wallace’s Adventure.
 * Manages levels, GUI, health & score displays, background music, and transitions.
 */
public class Game {

    /** Label showing the player’s health as an icon. */
    private JLabel healthLabel;
    /** Main application window. */
    private JFrame frame;
    /** The current game world (level). */
    private GameWorld game;
    /** Label displaying the star‐collection score. */
    private JLabel scoreLabel;
    /** Clip for background music playback. */
    private Clip backgroundClip;
    /** View into the physics world, handles rendering & camera. */
    private GameView view;
    /** Tracks which level the player is on (1,2,3…). */
    private int currentLevel = 1;

    /**
     * Create and initialize a new Game, set up level 1, GUI, controls, and start simulation.
     */
    public Game() {

        // create level 1 world
        game = new GameWorld(this, "data/ground.png", "data/ground.png", false, false);

        Player player = game.getPlayer();

        // set up view
        view = new GameView(game, 500, 500, player, "data/background1.png");
        view.setLayout(null);

        // add keyboard controls
        PlayerController controller = new PlayerController(player);
        view.addKeyListener(controller);
        view.setFocusable(true);
        view.requestFocusInWindow();

        // build frame
        frame = new JFrame("Wallace's Adventure");
        frame.setLayout(new BorderLayout());
        frame.add(view, BorderLayout.CENTER);

        // health display
        healthLabel = new JLabel(new ImageIcon("data/health4.png"));
        healthLabel.setBounds(460, 5, 45, 45);
        view.add(healthLabel);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationByPlatform(true);
        frame.setResizable(false);
        frame.pack();
        frame.setVisible(true);

        // play level 1 music
        Sound.playBackgroundMusic("data/background.wav");

        // score display
        scoreLabel = new JLabel("Stars: 0");
        scoreLabel.setForeground(Color.WHITE);
        scoreLabel.setBounds(10, 40, 100, 20);
        view.add(scoreLabel);

        // start physics
        game.start();

        // timer to spawn platforms
        new Timer(100, e -> game.updatePlatform()).start();
    }

    /**
     * Advance to the next level: stops current world, resets HUD, loads new world, GUI, music.
     */
    public void goToNextLevel() {
        currentLevel++;

        // reset HUD
        updateScoreDisplay(0);
        updateHealthDisplay(4);
        Sound.stopBackgroundMusic();
        if (game != null) {
            game.stop();
        }

        // create appropriate level
        if (currentLevel == 2) {
            Level2 level2 = new Level2();
            game = level2.createWorld(this);
            view.setWorld(game);
            view.setBackgroundImage(level2.getBackgroundImage());
            view.setPlayer(game.getPlayer());
            Sound.playBackgroundMusic("data/ice_music.wav");
            game.start();

        } else if (currentLevel == 3) {
            Level3 level3 = new Level3();
            game = level3.createWorld(this);
            view.setWorld(game);
            view.setBackgroundImage(level3.getBackgroundImage());
            view.setPlayer(game.getPlayer());
            Sound.playBackgroundMusic("data/level3.wav");
            game.start();

        } else {
            gameWon();
            return;
        }

        // re‐attach controls and reset player star count
        Player p = game.getPlayer();
        view.setPlayer(p);
        for (KeyListener kl : view.getKeyListeners()) {
            view.removeKeyListener(kl);
        }
        view.addKeyListener(new PlayerController(p));
        view.requestFocusInWindow();
        p.resetStarCount();
    }

    /**
     * Update the health icon to reflect the given health value.
     * @param health number of health points remaining (1–4)
     */
    public void updateHealthDisplay(int health) {
        healthLabel.setIcon(new ImageIcon("data/health" + health + ".png"));
        healthLabel.repaint();
    }

    /** Trigger the game-over end screen. */
    public void gameOver() {
        endScreen("data/gameover.png");
    }

    /** Trigger the “you win” end screen. */
    public void gameWon() {
        endScreen("data/youWin.png");
    }

    /**
     * Display a translucent overlay with the given image and a Restart button.
     * @param imagePath path to the end-screen image
     */
    private void endScreen(String imagePath) {
        game.stop();
        Sound.stopBackgroundMusic();

        JPanel overlay = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D)g.create();
                g2d.setColor(new Color(0,0,0,150));
                g2d.fillRect(0,0,getWidth(),getHeight());
                g2d.dispose();
            }
        };
        overlay.setLayout(new BoxLayout(overlay, BoxLayout.Y_AXIS));
        overlay.setOpaque(false);
        overlay.setBounds(0,0,500,500);
        overlay.setFocusable(false);

        ImageIcon icon = new ImageIcon(imagePath);
        Image img = icon.getImage().getScaledInstance(300,200,Image.SCALE_SMOOTH);
        JLabel imgLabel = new JLabel(new ImageIcon(img));
        imgLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        overlay.add(Box.createVerticalGlue());
        overlay.add(imgLabel);

        JButton restart = new JButton("Restart");
        restart.setAlignmentX(Component.CENTER_ALIGNMENT);
        restart.addActionListener(e -> restartGame());
        overlay.add(Box.createRigidArea(new Dimension(0,20)));
        overlay.add(restart);
        overlay.add(Box.createVerticalGlue());

        frame.getLayeredPane().add(overlay, JLayeredPane.POPUP_LAYER);
        frame.revalidate();
        frame.repaint();
    }

    /** Dispose current window and launch a fresh Game instance. */
    private void restartGame() {
        frame.dispose();
        new Game();
    }

    /**
     * Update the on-screen star count.
     * @param score new star count
     */
    public void updateScoreDisplay(int score) {
        scoreLabel.setText("Stars: " + score);
    }

    /**
     * @return the current GameWorld
     */
    public GameWorld getWorld() {
        return game;
    }

    /** Launch the application. */
    public static void main(String[] args) {
        new Game();
    }
}
