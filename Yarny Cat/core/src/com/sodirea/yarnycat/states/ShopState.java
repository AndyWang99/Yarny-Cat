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

    private Array<ImageButton> skinsArray; // the active skinsArray to be displayed
    private Array<ImageButton> skinsArray100;
    private Array<ImageButton> skinsArray250;
    private ImageButton catBtn;
    private ImageButton hellcatBtn;
    private Image shopScreen;
    private Image shopScroll;
    private Texture backBtn;
    private Stage stage;
    private String activePrice = "100";
    private Array<ImageButton> priceBtns;
    private ImageButton price100;
    private ImageButton price250;
    private ScrollPane scrollPane;
    private Table btnTable; // the active btnTable to be displayed
    private Table btnTable100;
    private Table btnTable250;
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

        price100 = new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture("price100.png"))));
        price100.setPosition(cam.position.x - shopScroll.getWidth() / 2, cam.position.y - shopScroll.getHeight() / 2 - shopScroll.getHeight() / 10 + shopScroll.getHeight());
        price100.setName("100");
        price250 = new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture("price250.png"))));
        price250.setPosition(cam.position.x - shopScroll.getWidth() / 2 + price100.getWidth(), cam.position.y - shopScroll.getHeight() / 2 - shopScroll.getHeight() / 10 + shopScroll.getHeight());
        price250.setName("250");
        price250.getImage().setColor(Color.GRAY);
        priceBtns = new Array<ImageButton>();
        priceBtns.add(price100);
        priceBtns.add(price250);


        skinsArray100 = new Array<ImageButton>();
        skinsArray250 = new Array<ImageButton>();
        btnTable100 = new Table();
        btnTable250 = new Table();

        skinsArray = skinsArray100; // point to the same array as the currently selected tab
        btnTable = btnTable100;
        btnTable.align(Align.topLeft);
        scrollPane = new ScrollPane(btnTable);
        scrollPane.setBounds(cam.position.x - shopScroll.getWidth() / 2, cam.position.y - shopScroll.getHeight() / 2 - shopScroll.getHeight() / 10, shopScroll.getWidth(), shopScroll.getHeight());
        shopScroll.setPosition(cam.position.x - shopScroll.getWidth() / 2, cam.position.y - shopScroll.getHeight() / 2 - shopScroll.getHeight() / 10);
        stage.addActor(shopScreen);
        stage.addActor(shopScroll);
        stage.addActor(price100);
        stage.addActor(price250);
        stage.addActor(scrollPane);

        // add buttons for each skin here
        catBtn = new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture("cat skins/cat/cat1.png"))));
        hellcatBtn = new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture("cat skins/hellcat/hellcat1.png"))));

        initSkinBtn(catBtn, "cat");
        initSkinBtn(hellcatBtn, "hellcat");
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
                    if (catSkinName != skin.getName()) { // checking if their active skin is the same as the skin they clicked (don't update the animation if it is
                        catSkinName = skin.getName();
                        updateSkinAnimation();
                        pref.putString("catskin", catSkinName);
                        pref.flush();
                    }
                } else {
                    // if they have sufficient currency, buy it and make it the active skin. otherwise, prompt that they don't have enough money
                    if (pref.getInteger("currency", 0) >= pref.getInteger(skin.getName() + "Price")) {
                        pref.putInteger("currency", pref.getInteger("currency") - pref.getInteger(skin.getName() + "Price"));
                        pref.putBoolean(skin.getName() + "Own", true);
                        catSkinName = skin.getName();
                        updateSkinAnimation();
                        pref.putString("catskin", catSkinName);
                        pref.flush();
                    } else {
                        System.out.println("Not enough fish!"); ////////////////// change for an Image overlayed on the stage.
                    }
                }
            }
        }
        for (int i = 0; i < priceBtns.size; i++) {
            ImageButton priceBtn = priceBtns.get(i);
            if (priceBtn.isPressed()) {
                for (ImageButton activeBtn : priceBtns) {
                    if (activeBtn.getName().equals(activePrice)) {
                        activeBtn.getImage().setColor(Color.LIGHT_GRAY);
                        break;
                    }
                }
                priceBtn.getImage().setColor(Color.WHITE);
                activePrice = priceBtn.getName();

                // pointing btnTable and skinsArray to the new price that they selected
                if (priceBtn.getName() == "100") {
                    btnTable = btnTable100;
                    skinsArray = skinsArray100;
                } else if (priceBtn.getName() == "250") {
                    btnTable = btnTable250;
                    skinsArray = skinsArray250;
                }
                scrollPane.remove(); // remove the current scrollpane with old skins
                scrollPane = new ScrollPane(btnTable); // updating the scrollpane to display the new skins
                scrollPane.setBounds(cam.position.x - shopScroll.getWidth() / 2, cam.position.y - shopScroll.getHeight() / 2 - shopScroll.getHeight() / 10, shopScroll.getWidth(), shopScroll.getHeight());
                shopScroll.setPosition(cam.position.x - shopScroll.getWidth() / 2, cam.position.y - shopScroll.getHeight() / 2 - shopScroll.getHeight() / 10);
                stage.addActor(scrollPane);
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

    public void initSkinBtn(ImageButton btn, String name) { // sets name of btn, adds to (appropriate) btn table, and adds to (appropriate) array of btns
        btn.setName(name);
        if (pref.getInteger(name + "Price") == 100) {
            btnTable100.add(btn);
            skinsArray100.add(btn);
        } else if (pref.getInteger(name + "Price") == 250) {
            btnTable250.add(btn);
            skinsArray250.add(btn);
        }
    }

    public void updateSkinAnimation() { // removes all frames of current skin from array, and puts the frames of the new skin into the array. only use this method after updating catSkinName to the new chosen skin
        catArray.removeRange(0, catArray.size - 1);
        catArray.add(new Texture("cat skins/" + catSkinName + "/" + catSkinName + "1.png"));
        catArray.add(new Texture("cat skins/" + catSkinName + "/" + catSkinName + "2.png"));
        catArray.add(new Texture("cat skins/" + catSkinName + "/" + catSkinName + "3.png"));
        catArray.add(new Texture("cat skins/" + catSkinName + "/" + catSkinName + "4.png"));
        catArray.add(new Texture("cat skins/" + catSkinName + "/" + catSkinName + "5.png"));
        catArray.add(new Texture("cat skins/" + catSkinName + "/" + catSkinName + "6.png"));
        catAnim = new Animation(catArray, 0.2f);
    }
}
