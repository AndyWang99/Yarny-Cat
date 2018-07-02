package com.sodirea.yarnycat.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import static com.sodirea.yarnycat.states.PlayState.GROUND_OFFSET;

public class Cat {
    private static final int GRAVITY = -30;
    private static final int MOVEMENT = 100;
    private int initialY;
    public static final float SCALING_FACTOR = 0.017f;
    private Texture ground;
    private Vector2 position;
    private Vector2 velocity;
    private Array<Texture> catArray;
    private Texture pounce;
    private Animation catAnim;
    private Rectangle bound;
    //private Sound meow;
    private Sound running;
    private Sound jumping;
    private boolean isRunning = false;

    public Cat(int x, int y) {
        ground = new Texture("ground.png");
        initialY = ground.getHeight() - GROUND_OFFSET;
        position = new Vector2(x, y);
        velocity = new Vector2(0, 0);
        catArray = new Array<Texture>();
        catArray.add(new Texture("cat.png"));
        catArray.add(new Texture("cat2.png"));
        catArray.add(new Texture("cat3.png"));
        catArray.add(new Texture("cat4.png"));
        catArray.add(new Texture("cat5.png"));
        catArray.add(new Texture("cat6.png"));
        catAnim = new Animation(catArray, 0.2f);
        pounce = new Texture("pounce.png");
        bound = new Rectangle(position.x, position.y + (float) (catAnim.getActiveTexture().getHeight() * 0.21), catAnim.getActiveTexture().getWidth(), catAnim.getActiveTexture().getHeight() - (float) (catAnim.getActiveTexture().getHeight() * 0.21)); // remove legs from hitbox; it has no effect on the overall hit box when walking, and has little effect when jumping (due to the nature of the sprite)
        //meow = Gdx.audio.newSound(Gdx.files.internal("catmeow.wav"));
        running = Gdx.audio.newSound(Gdx.files.internal("running.wav"));
        jumping = Gdx.audio.newSound(Gdx.files.internal("jumping.wav"));
    }

    public Texture getTexture() {
        return catAnim.getActiveTexture();
    }

    public Vector2 getPosition() {
        return position;
    }

    public void update(float dt) {
        if (velocity.x != 500) { // if not running, then stop playing the running music (if it is playing)
            isRunning = false;
            running.stop();
        }
        if (position.y > initialY){
            velocity.add(0, GRAVITY);
        }
        catAnim.setMaxFrameTime((float) 0.2 / ((MOVEMENT + velocity.x) / MOVEMENT));
        velocity.scl(SCALING_FACTOR);
        position.add(MOVEMENT * SCALING_FACTOR + velocity.x, velocity.y);
        velocity.scl(1/SCALING_FACTOR);
        if (position.y <= initialY) {
            position.y = initialY;
            velocity.y = 0;
            velocity.x = 0;
        }
        bound.setPosition(position.x, position.y + (float) (catAnim.getActiveTexture().getHeight() * 0.21));
        catAnim.update(dt);
    }

    public void render(SpriteBatch sb) {
        if (position.y == initialY) {
            sb.draw(catAnim.getActiveTexture(), position.x, position.y);
        } else {
            sb.draw(pounce, position.x, position.y);
        }
    }

    public void dispose() {
        for (Texture texture : catArray) {
            texture.dispose();
        }
        pounce.dispose();
        ground.dispose();
        //meow.dispose();
        running.dispose();
        jumping.dispose();
    }

    public void pounce() {
        if (velocity.y == 0) {
            velocity.y = 700;
            velocity.x = 250;
            //meow.play();
            jumping.play(0.4f);
        }
    }

    public void sprint() {
        if (velocity.y == 0) {
            velocity.x = 500;
            if (!isRunning) {
                running.loop(1f);
                isRunning = true;
            }
        }
    }

    public boolean hit(Rectangle boundOther) {
        return bound.overlaps(boundOther);
    }

    public void stopSound() {
        running.stop();
        jumping.stop();
    }
}
