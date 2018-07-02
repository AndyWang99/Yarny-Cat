package com.sodirea.yarnycat.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.sodirea.yarnycat.states.PlayState;

public class Fish {
    private Texture fish;
    private Rectangle bound;
    private Vector2 position;
    private Sound collectFish;

    public Fish(int x, int y) {
        fish = new Texture("fish.png");
        position = new Vector2(x, y);
        bound = new Rectangle(position.x, position.y, fish.getWidth(), fish.getHeight());
        collectFish = Gdx.audio.newSound(Gdx.files.internal("collectfish.wav"));
    }

    public void update(float dt) {

    }

    public void render(SpriteBatch sb) {
        sb.draw(fish, position.x, position.y);
    }

    public Texture getTexture() {
        return fish;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Rectangle getBound() {
        return bound;
    }

    public void reposition() {
        position.x += PlayState.DOG_INTERVAL;
        bound.setPosition(position.x, position.y);
    }

    public void dispose() {
        fish.dispose();
        collectFish.dispose();
    }

    public void playSound() {
        collectFish.play(0.5f);
    }
}
