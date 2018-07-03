package com.sodirea.yarnycat.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class CreditState extends State {

    private Texture credits;

    protected CreditState(GameStateManager gsm) {
        super(gsm);
        credits = new Texture("credits.png");
        cam.setToOrtho(false, 600, 1000);
    }

    @Override
    protected void handleInput() {
        if (Gdx.input.justTouched()) {
            gsm.set(new MenuState(gsm));
        }
    }

    @Override
    public void update(float dt) {
        handleInput();
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(cam.combined);
        sb.begin();
        sb.draw(credits, 0, 0);
        sb.end();
    }

    @Override
    public void dispose() {
        credits.dispose();
    }
}
