package com.sodirea.yarnycat.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Align;
import com.sodirea.yarnycat.YarnyCat;
import com.sodirea.yarnycat.sprites.Cat;
import com.sodirea.yarnycat.sprites.Fish;
import com.sodirea.yarnycat.sprites.JumpingDog;
import com.sodirea.yarnycat.sprites.ZoomieDog;

public class PlayState extends State {
    private Texture bg;
    private Texture ground;
    private float bgPos1;
    private float bgPos2;
    private float groundPos1;
    private float groundPos2;
    private Cat cat;
    private ZoomieDog zoomer;
    private JumpingDog jumper;
    private boolean paused;
    private boolean dead;
    private Texture deathscreen;
    private Texture minihomebtn;
    private Texture miniplaybtn;
    private Texture pausescreen;
    private int score;
    private BitmapFont fingerpaint;
    private BitmapFont fingerpaint32;
    private BitmapFont fingerpaintBlue;
    private Preferences pref;
    private Fish fish;
    private Texture currencyIcon;
    private boolean scoreObtainable = true;
    public static final int TOTAL_DOGS = 2;
    private static final int INIT_DOG_POS = 700;
    private static final int INIT_FISH_POS = 700;
    public static final int DOG_INTERVAL = 550;
    public static final int GROUND_OFFSET = 325;

    public PlayState(GameStateManager gsm) {
        super(gsm);
        bg = new Texture("bg.png");
        ground = new Texture("ground.png");
        bgPos1 = cam.position.x - cam.viewportWidth / 2;
        bgPos2 = bgPos1 + bg.getWidth();
        groundPos1 = cam.position.x - cam.viewportWidth / 2;
        groundPos2 = groundPos1 + ground.getWidth();
        cat = new Cat(100,ground.getHeight() - GROUND_OFFSET);
        zoomer = new ZoomieDog(INIT_DOG_POS, ground.getHeight() - GROUND_OFFSET);
        jumper = new JumpingDog(INIT_DOG_POS + DOG_INTERVAL, ground.getHeight() - GROUND_OFFSET);
        paused = false;
        dead = false;
        deathscreen = new Texture("deathscreen.png");
        minihomebtn = new Texture("minihomebtn.png");
        miniplaybtn = new Texture("miniplaybtn.png");
        pausescreen = new Texture("pausescreen.png");
        score = 0;
        fingerpaint = new BitmapFont(Gdx.files.internal("fingerpaint.fnt"), Gdx.files.internal("fingerpaint.png"), false);
        fingerpaint32 = new BitmapFont(Gdx.files.internal("fingerpaint32.fnt"), Gdx.files.internal("fingerpaint32.png"), false);
        fingerpaint.setColor(Color.WHITE);
        fingerpaintBlue = new BitmapFont(Gdx.files.internal("fingerpaint32.fnt"), Gdx.files.internal("fingerpaint32.png"), false);
        fingerpaintBlue.setColor(new Color((float) 0, (float) 191/255, (float) 241/255, (float) 1));
        pref = Gdx.app.getPreferences("My Preferences");
        fish = new Fish(INIT_FISH_POS, ground.getHeight() - GROUND_OFFSET + cat.getTexture().getHeight() * 2);
        currencyIcon = new Texture("fish.png");
        cam.setToOrtho(false, YarnyCat.WIDTH / 2 + YarnyCat.WIDTH / 3, YarnyCat.HEIGHT / 2 + YarnyCat.HEIGHT / 3);
        cam.position.x = cat.getPosition().x + cam.viewportWidth / 2 - cam.viewportWidth / 8;
        cam.update();
    }

    @Override
    public void handleInput() {
        Vector3 mousePos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        cam.unproject(mousePos);
        if (!paused) {
            if (mousePos.x < cam.position.x
                    && Gdx.input.isTouched()) {
                cat.sprint();
            } else if (mousePos.x > cam.position.x
                    && Gdx.input.justTouched()) {
                cat.pounce();
            }
        } else {
            if (mousePos.x > cam.position.x - 2 * minihomebtn.getWidth()
                    && mousePos.x < cam.position.x -  minihomebtn.getWidth()
                    && mousePos.y > cam.position.y + deathscreen.getHeight() / 6
                    && mousePos.y < cam.position.y + deathscreen.getHeight() / 6 + minihomebtn.getHeight()
                    && Gdx.input.justTouched()) {
                gsm.set(new MenuState(gsm));

            } else if (mousePos.x > cam.position.x +  miniplaybtn.getWidth()
                    && mousePos.x < cam.position.x + 2 * miniplaybtn.getWidth()
                    && mousePos.y > cam.position.y + deathscreen.getHeight() / 6
                    && mousePos.y < cam.position.y + deathscreen.getHeight() / 6 + miniplaybtn.getHeight()
                    && Gdx.input.justTouched()) {
                if (dead) {
                    gsm.set(new PlayState(gsm));
                } else {
                    unpauseGame();
                }
            }
        }
    }

    @Override
    public void update(float dt) {
        handleInput();
        if (!paused) {
            if (gsm.isOutOfApp()) {
                pauseGame();
            } else {
                cat.update(dt);
                zoomer.update(dt);
                jumper.update(dt);
                // checking if zoomer enters the screen, exits the screen, is jumped over by the cat
                if (zoomer.getPosition().x < cam.position.x + cam.viewportWidth / 2) {
                    zoomer.run(dt);
                }
                if (zoomer.getPosition().x + zoomer.getTexture().getWidth() < cam.position.x - cam.viewportWidth / 2) {
                    zoomer.reposition();
                    scoreObtainable = true;
                }
                if (zoomer.getPosition().x + zoomer.getTexture().getWidth() < cat.getPosition().x && scoreObtainable) {
                    score += 1;
                    scoreObtainable = false; // this is to ensure that they only get _one_ point for crossing a dog
                }
                // checking if jumper enters the screen, exits the screen, is jumped over by the cat
                if (jumper.getPosition().x < cam.position.x + cam.viewportWidth / 2) {
                    jumper.jump(dt);
                }
                if (jumper.getPosition().x + jumper.getTexture().getWidth() < cam.position.x - cam.viewportWidth / 2) {
                    jumper.reposition();
                    scoreObtainable = true; // reallow them to gather another point for the next dog
                }
                if (jumper.getPosition().x + jumper.getTexture().getWidth() < cat.getPosition().x && scoreObtainable) {
                    score += 1;
                    scoreObtainable = false;
                }
                // checking if cat hits zoomer or jumper
                if (cat.hit(zoomer.getBound()) || cat.hit(jumper.getBound())) {
                    deathScreen();
                }
                // checking if cat hits fish or if fish goes off-screen (then reposition)
                if (cat.hit(fish.getBound())) {
                    fish.playSound();
                    fish.reposition();
                    pref.putInteger("currency", pref.getInteger("currency", 0) + 1);
                    pref.flush();
                }
                if (fish.getPosition().x + fish.getTexture().getWidth() < cam.position.x - cam.viewportWidth / 2) {
                    fish.reposition();
                }
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
                cam.position.x = cat.getPosition().x + cam.viewportWidth / 2 - cam.viewportWidth / 8;
                cam.update();
            }
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(cam.combined);
        sb.begin();
        sb.draw(bg, bgPos1, cam.position.y - cam.viewportHeight / 2);
        sb.draw(bg, bgPos2, cam.position.y - cam.viewportHeight / 2);
        fingerpaint.draw(sb, Integer.toString(score), cam.position.x, cam.position.y + cam.viewportHeight / 2, 0, Align.center, false);
        sb.draw(ground, groundPos1, cam.position.y - cam.viewportHeight / 2 - GROUND_OFFSET);
        sb.draw(ground, groundPos2, cam.position.y - cam.viewportHeight / 2 - GROUND_OFFSET);
        fish.render(sb);
        zoomer.render(sb);
        jumper.render(sb);
        cat.render(sb);
        sb.draw(currencyIcon, cam.position.x - cam.viewportWidth / 2, cam.position.y + cam.viewportHeight / 2 - currencyIcon.getHeight());
        fingerpaint32.draw(sb, Integer.toString(pref.getInteger("currency", 0)), cam.position.x - cam.viewportWidth / 2 + currencyIcon.getWidth() + currencyIcon.getWidth() / 5, cam.position.y + cam.viewportHeight / 2);
        if (paused) {
            if (dead) {
                sb.draw(deathscreen, cam.position.x - deathscreen.getWidth() / 2, cam.position.y);
                fingerpaintBlue.draw(sb, Integer.toString(score), cam.position.x + cam.viewportWidth / 8, cam.position.y + cam.viewportHeight / 5 + cam.viewportHeight / 90);
                if (score > pref.getInteger("highscore", 0)) {
                    pref.putInteger("highscore", score);
                    pref.flush();
                }
                fingerpaintBlue.draw(sb, Integer.toString(pref.getInteger("highscore", 0)), cam.position.x + cam.viewportWidth / 8, cam.position.y + cam.viewportHeight / 5 - cam.viewportHeight / 40);
            } else {
                sb.draw(pausescreen, cam.position.x - pausescreen.getWidth() / 2, cam.position.y);
            }
            sb.draw(minihomebtn, cam.position.x - 2 * minihomebtn.getWidth(), cam.position.y + deathscreen.getHeight() / 6);
            sb.draw(miniplaybtn, cam.position.x + miniplaybtn.getWidth(), cam.position.y + deathscreen.getHeight() / 6);
        }
        sb.end();
    }

    @Override
    public void dispose() {
        bg.dispose();
        ground.dispose();
        cat.dispose();
        zoomer.dispose();
        jumper.dispose();
        fingerpaint.dispose();
        fingerpaint32.dispose();
        fingerpaintBlue.dispose();
        fish.dispose();
        currencyIcon.dispose();
        deathscreen.dispose();
        minihomebtn.dispose();
        miniplaybtn.dispose();
        pausescreen.dispose();
    }

    public void pauseGame() {
        zoomer.stopSound();
        cat.stopSound();
        paused = true;
    }

    public void unpauseGame() {
        zoomer.playSound();
        paused = false;
    }

    public void deathScreen() {
        pauseGame();
        dead = true;
        cat.playDeathSound();
    }
}
