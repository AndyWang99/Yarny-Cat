package com.sodirea.yarnycat.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.sodirea.yarnycat.YarnyCat;

public class ShopState extends State {

    private Array<ImageButton> skinsArray;
    private ImageButton catBtn;
    private ImageButton hellcatBtn;
    private Image shopScreen;
    private Image shopScroll;
    private Texture backBtn;
    private Stage stage;
    private ScrollPane scrollPane;
    private Table btnTable;
    private Preferences pref;
    private Texture currencyIcon;
    private BitmapFont fingerpaint32;

    protected ShopState(GameStateManager gsm) {
        super(gsm);
        cam.setToOrtho(false, YarnyCat.WIDTH / 2 + YarnyCat.WIDTH / 3, YarnyCat.HEIGHT / 2 + YarnyCat.HEIGHT / 3);
        shopScreen = new Image(new TextureRegionDrawable(new TextureRegion(new Texture("shopscreen.png"))));
        shopScroll = new Image(new TextureRegionDrawable(new TextureRegion(new Texture("shopscroll.png"))));
        backBtn = new Texture("backbtn.png");
        pref = Gdx.app.getPreferences("My Preferences");
        currencyIcon = new Texture("fish.png");
        fingerpaint32 = new BitmapFont(Gdx.files.internal("fingerpaint32.fnt"), Gdx.files.internal("fingerpaint32.png"), false);
        fingerpaint32.setColor(new Color(0, (float) 191/255, (float) 241/255, 1));
        skinsArray = new Array<ImageButton>();

        stage = new Stage(new StretchViewport(cam.viewportWidth, cam.viewportHeight));
        Gdx.input.setInputProcessor(stage);
        stage.setDebugAll(true);///////
        btnTable = new Table();
        btnTable.align(Align.topLeft);
        scrollPane = new ScrollPane(btnTable);
        scrollPane.setBounds(cam.position.x - shopScroll.getWidth() / 2, cam.position.y - shopScroll.getHeight() / 2 - shopScroll.getHeight() / 15, shopScroll.getWidth(), shopScroll.getHeight());
        shopScroll.setPosition(cam.position.x - shopScroll.getWidth() / 2, cam.position.y - shopScroll.getHeight() / 2 - shopScroll.getHeight() / 15);
        stage.addActor(shopScreen);
        stage.addActor(shopScroll);
        stage.addActor(scrollPane);

        // add buttons for each skin here
        catBtn = new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture("cat skins/cat/cat1.png"))));
        hellcatBtn = new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture("cat skins/hellcat/hellcat1.png"))));

        // add an identifying string for the skin of each button here
        catBtn.setName("cat");
        hellcatBtn.setName("hellcat");

        // add each button to the table in the scrollpane here
        btnTable.add(catBtn);
        //btnTable.row();
        btnTable.add(hellcatBtn);

        // add each button to the overall array here (for use in checking if each button is clicked in handleInput())
        skinsArray.add(catBtn);
        skinsArray.add(hellcatBtn);
    }

    @Override
    protected void handleInput() {
        Vector3 mousePos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        cam.unproject(mousePos);
        if (mousePos.x > 3
                && mousePos.x < 3 + backBtn.getWidth()
                && mousePos.y > stage.getHeight() - backBtn.getHeight() - 40
                && mousePos.y < stage.getHeight() - 40
                && Gdx.input.justTouched()) {
            gsm.set(new MenuState(gsm));
        }
        for (ImageButton skin : skinsArray) {
            if (skin.isPressed()) {
                pref.putString("catskin", skin.getName());
                pref.flush();
            }
        }
    }

    @Override
    public void update(float dt) {
        handleInput();
        stage.act(dt);
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(cam.combined);
        stage.draw();
        sb.begin();
        //sb.draw(shopScreen,0,0);
        //sb.draw(shopScroll, cam.position.x - shopScroll.getWidth() / 2, cam.position.y - cam.viewportHeight / 3 - cam.viewportHeight / 20);
        sb.draw(backBtn, 3, stage.getHeight() - backBtn.getHeight() - 40); // +- 3 is for adjustment of the shopscreen's border
        sb.draw(currencyIcon, cam.position.x + 30, stage.getHeight() - currencyIcon.getHeight() - 40);
        fingerpaint32.draw(sb, Integer.toString(pref.getInteger("currency", 0)), cam.position.x + 35 + currencyIcon.getWidth(), stage.getHeight() - 50);
        sb.end();
    }

    @Override
    public void dispose() {
        backBtn.dispose();
        fingerpaint32.dispose();
        currencyIcon.dispose();
        stage.dispose();
    }
}
