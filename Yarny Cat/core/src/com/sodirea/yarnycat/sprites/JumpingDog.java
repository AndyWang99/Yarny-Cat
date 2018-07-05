package com.sodirea.yarnycat.sprites;

import com.badlogic.gdx.Audio;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.sodirea.yarnycat.states.PlayState;

import java.util.Random;

public class JumpingDog { ///////////////////////add diff texture/recolour for this dog... maybe give more speed? jumping is underwhelmign atm
    private static final int GRAVITY = -30;
    private static final int MOVEMENT = -100;
    private int initialY;
    private Vector2 position;
    private Vector2 velocity;
    private Array<Texture> textures;
    private Texture pounce;
    private Animation dogAnim;
    private Rectangle bound;
    //private Sound bark;
    //private boolean barked;
    private Sound jumping;
    private boolean jumped;
    private static final int DOG_MIN_JUMPING_VELOCITY = 600;
    private static final int DOG_MAX_ADDITIONAL_JUMPING_VELOCITY = 600;

    public JumpingDog(int x, int y) {
        initialY = y;
        position = new Vector2(x, y);
        velocity = new Vector2(0, DOG_MIN_JUMPING_VELOCITY + new Random().nextInt(DOG_MAX_ADDITIONAL_JUMPING_VELOCITY));
        textures = new Array<Texture>();
        textures.add(new Texture("dog1.png"));
        textures.add(new Texture("dog2.png"));
        textures.add(new Texture("dog3.png"));
        textures.add(new Texture("dog4.png"));
        textures.add(new Texture("dog5.png"));
        textures.add(new Texture("dog6.png"));
        dogAnim = new Animation(textures, 0.2f);
        pounce = new Texture("dogpounce.png");
        bound = new Rectangle(position.x, position.y + (float) (dogAnim.getActiveTexture().getHeight() * 0.21), dogAnim.getActiveTexture().getWidth(), dogAnim.getActiveTexture().getHeight() - (float) (dogAnim.getActiveTexture().getHeight() * 0.21));
        //bark = Gdx.audio.newSound(Gdx.files.internal("dogbark.wav"));
        //barked = false;
        jumping = Gdx.audio.newSound(Gdx.files.internal("jumping.wav"));
        jumped = false;
    }

    public void update(float dt) {
        dogAnim.update(dt);
    }

    public void render(SpriteBatch sb) {
        if (position.y == initialY) {
            sb.draw(dogAnim.getActiveTexture(), position.x, position.y);
        } else {
            sb.draw(pounce, position.x, position.y);
        }
    }

    public void dispose() {
        for (Texture texture : textures) {
            texture.dispose();
        }
        jumping.dispose();
        //bark.dispose();
    }

    public void jump(float dt) {
        //if (!barked) {
        //    bark.play(1.0f);
        //    barked = true;
        //}
        if (!jumped) {
            jumping.play(0.2f);
            jumped = true;
        }
        velocity.add(0, GRAVITY);
        velocity.scl(dt);
        position.add(MOVEMENT * Cat.SCALING_FACTOR, velocity.y);
        velocity.scl(1/Cat.SCALING_FACTOR);
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
        velocity.y = DOG_MIN_JUMPING_VELOCITY + new Random().nextInt(DOG_MAX_ADDITIONAL_JUMPING_VELOCITY);
        bound.setPosition(position.x, position.y + (float) (dogAnim.getActiveTexture().getHeight() * 0.21));
        //barked = false; // it is a "new" dog, so give them the ability to bark again
        jumped = false;
    }

    public Rectangle getBound() {
        return bound;
    }
}
