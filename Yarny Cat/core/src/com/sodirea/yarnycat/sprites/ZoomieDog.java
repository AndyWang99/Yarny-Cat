package com.sodirea.yarnycat.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.sodirea.yarnycat.states.PlayState;

import java.util.Random;

public class ZoomieDog {
    private static final int GRAVITY = -30;
    private static final int MOVEMENT = -100;
    private int initialY;
    private Vector2 position;
    private Vector2 velocity;
    private Array<Texture> textures;
    private Animation dogAnim;
    private Rectangle bound;
    //private Sound bark;
    //private boolean barked;
    private Sound running;
    private boolean isRunning;
    private static final int DOG_MIN_RUNNING_VELOCITY = 50;
    private static final int DOG_MAX_ADDITIONAL_RUNNING_VELOCITY = 60;

    public ZoomieDog(int x, int y) {
        initialY = y;
        position = new Vector2(x, y);
        velocity = new Vector2(DOG_MIN_RUNNING_VELOCITY + new Random().nextInt(DOG_MAX_ADDITIONAL_RUNNING_VELOCITY) - (new Random().nextInt(DOG_MIN_RUNNING_VELOCITY + DOG_MAX_ADDITIONAL_RUNNING_VELOCITY)), 0);
        textures = new Array<Texture>();
        textures.add(new Texture("dog1.png"));
        textures.add(new Texture("dog2.png"));
        textures.add(new Texture("dog3.png"));
        textures.add(new Texture("dog4.png"));
        textures.add(new Texture("dog5.png"));
        textures.add(new Texture("dog6.png"));
        dogAnim = new Animation(textures, 0.2f);
        bound = new Rectangle(position.x, position.y + (float) (dogAnim.getActiveTexture().getHeight() * 0.21), dogAnim.getActiveTexture().getWidth(), dogAnim.getActiveTexture().getHeight() - (float) (dogAnim.getActiveTexture().getHeight() * 0.21));
        //bark = Gdx.audio.newSound(Gdx.files.internal("dogbark.wav"));
        //barked = false;
        running = Gdx.audio.newSound(Gdx.files.internal("running.wav"));
        isRunning = false;
    }

    public void update(float dt) {
        dogAnim.update(dt);
    }

    public void render(SpriteBatch sb) {
        sb.draw(dogAnim.getActiveTexture(), position.x, position.y);
    }

    public void dispose() {
        for (Texture texture : textures) {
            texture.dispose();
        }
        //bark.dispose();
        running.dispose();
    }

    public void run(float dt) {
        //if (!barked) {
        //    bark.play(1.0f);
        //    barked = true;
        //}
        if (!isRunning) {
            running.loop(0.5f);
            isRunning = true;
        }
        velocity.add(0, GRAVITY);
        velocity.scl(dt);
        position.add(MOVEMENT * dt - velocity.x, velocity.y);
        velocity.scl(1/dt);
        if(position.y < initialY) {
            position.y = initialY;
        }
        bound.setPosition(position.x, position.y + (float) (dogAnim.getActiveTexture().getHeight() * 0.21));
    }

    public Texture getTexture() {
        return dogAnim.getActiveTexture();
    }

    public Vector2 getPosition() {
        return position;
    }

    public void reposition() {
        position.x += PlayState.DOG_INTERVAL * PlayState.TOTAL_DOGS;
        position.y = 0;
        velocity.x = DOG_MIN_RUNNING_VELOCITY + new Random().nextInt(DOG_MAX_ADDITIONAL_RUNNING_VELOCITY) - (new Random().nextInt(DOG_MIN_RUNNING_VELOCITY + DOG_MAX_ADDITIONAL_RUNNING_VELOCITY));
        bound.setPosition(position.x, position.y + (float) (dogAnim.getActiveTexture().getHeight() * 0.21));
        //barked = false; // it is a "new" dog, so give them the ability to bark again
        isRunning = false;
        running.stop();
    }

    public Rectangle getBound() {
        return bound;
    }

    public void stopSound() { // for use in PlayState to stop the sound when paused or dead
        running.stop();
    }

    public void playSound() {
        running.loop(0.5f);
    }
}
