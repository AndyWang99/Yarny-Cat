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
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.sodirea.yarnycat.YarnyCat;
import com.sodirea.yarnycat.sprites.Animation;

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
    private Array<Texture> catArray;
    private String catSkinName;
    private Animation catAnim;

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

        catSkinName = pref.getString("catskin", "cat");
        catArray = new Array<Texture>();
        catArray.add(new Texture("cat skins/" + catSkinName + "/" + catSkinName + "1.png"));
        catArray.add(new Texture("cat skins/" + catSkinName + "/" + catSkinName + "2.png"));
        catArray.add(new Texture("cat skins/" + catSkinName + "/" + catSkinName + "3.png"));
        catArray.add(new Texture("cat skins/" + catSkinName + "/" + catSkinName + "4.png"));
        catArray.add(new Texture("cat skins/" + catSkinName + "/" + catSkinName + "5.png"));
        catArray.add(new Texture("cat skins/" + catSkinName + "/" + catSkinName + "6.png"));
        catAnim = new Animation(catArray, 0.2f);

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
                // checking if they own the current skin. if they do, then switch to this current skin, i.e. animate it. if they don't, then prompt them to purchase)
                if (pref.getBoolean(skin.getName() + "Own", false)) {
                    catSkinName = skin.getName();
                    catArray.removeRange(0, catArray.size - 1);
                    catArray.add(new Texture("cat skins/" + catSkinName + "/" + catSkinName + "1.png"));
                    catArray.add(new Texture("cat skins/" + catSkinName + "/" + catSkinName + "2.png"));
                    catArray.add(new Texture("cat skins/" + catSkinName + "/" + catSkinName + "3.png"));
                    catArray.add(new Texture("cat skins/" + catSkinName + "/" + catSkinName + "4.png"));
                    catArray.add(new Texture("cat skins/" + catSkinName + "/" + catSkinName + "5.png"));
                    catArray.add(new Texture("cat skins/" + catSkinName + "/" + catSkinName + "6.png"));
                    catAnim = new Animation(catArray, 0.2f);
                    pref.putString("catskin", catSkinName);
                    pref.flush();
                } else {
                    // if they have sufficient currency, buy it and make it the active skin. otherwise, prompt that they don't have enough money
                    if (pref.getInteger("currency", 0) >= pref.getInteger(skin.getName() + "Price")) {
                        pref.putInteger("currency", pref.getInteger("currency") - pref.getInteger(skin.getName() + "Price"));
                        pref.putBoolean(skin.getName() + "Own", true);
                        catSkinName = skin.getName();
                        catArray.removeRange(0, catArray.size - 1);
                        catArray.add(new Texture("cat skins/" + catSkinName + "/" + catSkinName + "1.png"));
                        catArray.add(new Texture("cat skins/" + catSkinName + "/" + catSkinName + "2.png"));
                        catArray.add(new Texture("cat skins/" + catSkinName + "/" + catSkinName + "3.png"));
                        catArray.add(new Texture("cat skins/" + catSkinName + "/" + catSkinName + "4.png"));
                        catArray.add(new Texture("cat skins/" + catSkinName + "/" + catSkinName + "5.png"));
                        catArray.add(new Texture("cat skins/" + catSkinName + "/" + catSkinName + "6.png"));
                        catAnim = new Animation(catArray, 0.2f);
                        pref.putString("catskin", catSkinName);
                        pref.flush();
                    } else {
                        System.out.println("Not enough fish!");
                    }
                }
            }
        }
    }

    @Override
    public void update(float dt) {
        handleInput();
        catAnim.update(dt);
        for (ImageButton skin : skinsArray) {
            // if the equipped skin is the same as the current imagebutton in the array, then animate the image
            if (catSkinName.equals(skin.getName())) {
                Cell currentCell = btnTable.getCell(skin);
                ImageButton skinAnim = new ImageButton(new TextureRegionDrawable(new TextureRegion(catAnim.getActiveTexture())));
                skinAnim.setName(catSkinName);
                if(currentCell != null) {
                    currentCell.setActor(skinAnim);
                }
                skinsArray.removeValue(skin, true);
                skinsArray.add(skinAnim);
            }
            // checking if they own the current skin. if they don't, then set the button's image's colour to a gray tint (to signify that it's unpurchased)
            if (!pref.getBoolean(skin.getName() + "Own", false)) {
                skin.getImage().setColor(Color.GRAY); // maybe use light_gray instead, for more visibility?
            }
        }
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
