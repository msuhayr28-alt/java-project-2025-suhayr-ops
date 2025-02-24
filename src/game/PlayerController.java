package game;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class PlayerController implements KeyListener {

    private final Player player;
    private static final float JUMP_FORCE = 5;
    private static final float WALK_FORCE = 2;

    public PlayerController(Player player){
        this.player = player;
    }

    public void keyPressed(KeyEvent e){
        int key = e.getKeyCode();
        if(key == KeyEvent.VK_LEFT){
            player.startWalking(-WALK_FORCE);
            System.out.println("Moving Left");

        }
        else if(key == KeyEvent.VK_RIGHT){
            player.startWalking(WALK_FORCE);
            System.out.println("Moving Right");

        }
        else if(key ==KeyEvent.VK_UP || key == KeyEvent.VK_SPACE){
            if(player.getLinearVelocity().y == 0){
                player.jump(JUMP_FORCE);
                System.out.println("Jumping");

            }
        }
    }

    public void keyReleased(KeyEvent e){
        int key = e.getKeyCode();

        if(key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_LEFT){
            player.stopWalking();
        }

    }
    public void keyTyped(KeyEvent e){

    }


}
