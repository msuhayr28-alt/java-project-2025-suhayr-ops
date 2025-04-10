package game;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class PlayerController implements KeyListener {

    private final Player player;
    private static final float JUMP_FORCE = 15;
    private static final float WALK_FORCE = 15;

    public PlayerController(Player player){
        this.player = player;
    }

    public void keyPressed(KeyEvent e){
        int key = e.getKeyCode();
        if(key == KeyEvent.VK_LEFT){
            player.startWalking(-WALK_FORCE);
        }
        else if(key == KeyEvent.VK_RIGHT){
            player.startWalking(WALK_FORCE);
        }
        else if(key ==KeyEvent.VK_UP || key == KeyEvent.VK_SPACE){
            if(player.getLinearVelocity().y < 6f){
                player.jump(JUMP_FORCE);
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
