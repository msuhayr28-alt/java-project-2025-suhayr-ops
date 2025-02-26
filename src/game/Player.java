package game;
import city.cs.engine.*;

import javax.swing.*;

import org.jbox2d.common.Vec2;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class Player extends Walker{
    private static final BodyImage idleRightImage = new BodyImage("data/walk1.png", 4);
    private static final BodyImage idleLeftImage = new BodyImage("data/walk1-1.png", 4);

    private static final BodyImage[] walkLeftImages = {
            new BodyImage("data/walk1-1.png", 4),
            new BodyImage("data/walk2-1.png", 4),
            new BodyImage("data/walk3-1.png", 4)};

    private static final BodyImage[] walkRightImages = {
            new BodyImage("data/walk1.png", 4),
            new BodyImage("data/walk2.png", 4),
            new BodyImage("data/walk3.png", 4)};

    private int currentFrame = 0;
    private boolean isMoving = false;
    private boolean facingRight = true;
    private Timer animationTimer;// Timer for walking animation

    private AttachedImage currentImage;


    public Player(World world) {
        super(world);

        // character was not convex, so I had to create two convex shapes and attach them to each other
        Shape shape1 = new PolygonShape(-0.92f, 1.31f, -0.45f, 2.02f, 0.32f, 2.0f, 0.82f, 1.42f, 0.83f, 0.9f, 0.4f, 0.58f, -0.56f, 0.59f, -0.92f, 1.3f
        );
        Shape shape2 = new PolygonShape(-0.53f, 0.66f, 0.36f, 0.66f, 0.96f, -0.07f, 0.96f, -1.93f, -1.0f, -1.91f, -0.99f, 0.18f, -0.54f, 0.66f
        );

        new SolidFixture(this, shape1);
        new SolidFixture(this, shape2);
        //attached image to shape and set staring position
        currentImage = new AttachedImage(this, idleRightImage, 1, 0, new Vec2(0, 0));
        this.setPosition(new Vec2(0, -7));

        animationTimer = new Timer(100, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isMoving) {
                    updateAnimation();
                }
            }
        });


    }

        public void updateAnimation(){
            if(isMoving){
                this.removeAttachedImage(currentImage);
                BodyImage[] selectedImage = facingRight ? walkRightImages : walkLeftImages;
                currentImage = new AttachedImage(this, selectedImage[currentFrame], 1, 0, new Vec2(0, 0));
                currentFrame = (currentFrame + 1) % selectedImage.length;
            }
        }

        public void startWalking(float speed){
            super.startWalking(speed);
            if(speed > 0 && !facingRight){
                facingRight = true;
                setIdleImage();
            }else if(speed < 0 && facingRight){
                facingRight = false;
                setIdleImage();
            }
            if (!isMoving) {
                isMoving = true;
                animationTimer.start();
            }

        }
        public void stopWalking() {
            super.stopWalking();
            isMoving = false;
            animationTimer.stop(); // Stop animation
            setIdleImage();
            this.setLinearVelocity(new Vec2(0, this.getLinearVelocity().y));

        }

        private void setIdleImage(){
            this.removeAttachedImage(currentImage);
            currentImage = new AttachedImage(this, facingRight ? idleRightImage : idleLeftImage, 1, 0, new Vec2(0, 0));

        }





}
