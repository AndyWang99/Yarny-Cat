package com.sodirea.yarnycat.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.sodirea.yarnycat.YarnyCat;
import com.sodirea.yarnycat.sprites.Cat;

import static com.sodirea.yarnycat.states.PlayState.GROUND_OFFSET;

public class MenuState extends State {
    private Texture bg;
    private Texture playBtn;
    private Texture title;
    private Texture ground;
    private Texture creditsBtn;
    private Texture shopBtn;
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
        creditsBtn = new Texture("creditsbtn.png");
        shopBtn = new Texture("shopbtn.png");
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
        Vector3 mousePos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        cam.unproject(mousePos);
        if (mousePos.x > cam.position.x - creditsBtn.getWidth() / 2 - cam.viewportWidth / 3
                && mousePos.x < cam.position.x + creditsBtn.getWidth() / 2 - cam.viewportWidth / 3
                && mousePos.y > cam.position.y - cam.viewportHeight / 6 + playBtn.getHeight() / 2 - creditsBtn.getHeight() / 2
                && mousePos.y < cam.position.y - cam.viewportHeight / 6 + playBtn.getHeight() / 2 + creditsBtn.getHeight() / 2
                && Gdx.input.justTouched()) {
            gsm.set(new CreditState(gsm));
        } else if (mousePos.x > cam.position.x - shopBtn.getWidth() / 2 + cam.viewportWidth / 3
                && mousePos.x < cam.position.x + shopBtn.getWidth() / 2 + cam.viewportWidth / 3
                && mousePos.y > cam.position.y - cam.viewportHeight / 6 + playBtn.getHeight() / 2 - shopBtn.getHeight() / 2
                && mousePos.y < cam.position.y - cam.viewportHeight / 6 + playBtn.getHeight() / 2 + shopBtn.getHeight() / 2
                && Gdx.input.justTouched()) {
            gsm.set(new ShopState(gsm));
        } else if (Gdx.input.justTouched()) {
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
        sb.draw(creditsBtn, cam.position.x - creditsBtn.getWidth() / 2 - cam.viewportWidth / 3, cam.position.y - cam.viewportHeight / 6 + playBtn.getHeight() / 2 - creditsBtn.getHeight() / 2);
        sb.draw(shopBtn, cam.position.x - shopBtn.getWidth() / 2 + cam.viewportWidth / 3, cam.position.y - cam.viewportHeight / 6 + playBtn.getHeight() / 2 - shopBtn.getHeight() / 2);
        sb.end();
    }

    @Override
    public void dispose() {
        bg.dispose();
        playBtn.dispose();
        title.dispose();
        ground.dispose();
        cat.dispose();
        creditsBtn.dispose();
        shopBtn.dispose();
    }
}
