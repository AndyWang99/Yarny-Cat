package com.sodirea.yarnycat.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.sodirea.yarnycat.YarnyCat;
import com.sodirea.yarnycat.sprites.Cat;

import static com.sodirea.yarnycat.states.PlayState.GROUND_OFFSET;

public class MenuState extends State {
    private Texture bg;
    private Texture playBtn;
    private Texture title;
    private Texture ground;
    private float bgPos1;
    private float bgPos2;
    private float groundPos1;
    private float groundPos2;
    private Cat cat;

    public MenuState(GameStateManager gsm) {
        super(gsm);
        bg = new Texture("bg.png");
        playBtn = new Texture("playbtn.png");
        title = new Texture("title.png");
        ground = new Texture("ground.png");
        bgPos1 = cam.position.x - cam.viewportWidth / 2;
        bgPos2 = bgPos1 + bg.getWidth();
        groundPos1 = cam.position.x - cam.viewportWidth / 2;
        groundPos2 = groundPos1 + ground.getWidth();
        cat = new Cat(100,ground.getHeight() - GROUND_OFFSET);
        cam.setToOrtho(false, YarnyCat.WIDTH / 2 + YarnyCat.WIDTH / 3, YarnyCat.HEIGHT / 2 + YarnyCat.HEIGHT / 3);
        cam.position.x = cat.getPosition().x + cam.viewportWidth / 2 - cam.viewportWidth / 8;
        cam.update();
    }

    @Override
    public void handleInput() {
        if (Gdx.input.justTouched()) {
            gsm.set(new PlayState(gsm));
        }
    }

    @Override
    public void update(float dt) {
        handleInput();
        // repositioning the background to create infinite-like scrolling
        if (bgPos1 + bg.getWidth() < cam.position.x - cam.viewportWidth / 2) {
            bgPos1 += 2 * bg.getWidth();
        }
        if (bgPos2 + bg.getWidth() < cam.position.x - cam.viewportWidth / 2) {
            bgPos2 += 2 * bg.getWidth();
        }
        // repositioning the ground to create infinite-like scrolling
        if (groundPos1 + ground.getWidth() < cam.position.x - cam.viewportWidth / 2) {
            groundPos1 += 2 * ground.getWidth();
        }
        if (groundPos2 + ground.getWidth() < cam.position.x - cam.viewportWidth / 2) {
            groundPos2 += 2 * ground.getWidth();
        }
        cat.update(dt);
        cam.position.x = cat.getPosition().x + cam.viewportWidth / 2 - cam.viewportWidth / 8;
        cam.update();
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(cam.combined);
        sb.begin();
        sb.draw(bg, bgPos1, cam.position.y - cam.viewportHeight / 2);
        sb.draw(bg, bgPos2, cam.position.y - cam.viewportHeight / 2);
        sb.draw(playBtn, cam.position.x - playBtn.getWidth() / 2, cam.position.y - cam.viewportHeight / 6 );
        sb.draw(title, cam.position.x - title.getWidth() / 2, cam.position.y);
        sb.draw(ground, groundPos1, cam.position.y - cam.viewportHeight / 2 - GROUND_OFFSET);
        sb.draw(ground, groundPos2, cam.position.y - cam.viewportHeight / 2 - GROUND_OFFSET);
        cat.render(sb);
        sb.end();
    }

    @Override
    public void dispose() {
        bg.dispose();
        playBtn.dispose();
        title.dispose();
    }
}
