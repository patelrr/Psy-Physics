package com.mygdx.game.States;

/**
 * Created by Dell on 18-02-2016.
 */


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

/**
 * Created by Brent on 6/25/2015.
 */
public class MenuState extends State {
    Texture background;
    Texture playBtn;
    private Music dropSound;
    private BitmapFont font;
    public MenuState(GameStateManager gsm) {
        super(gsm);
        background = new Texture(Gdx.files.internal("data/back.jpg"));
        dropSound = Gdx.audio.newMusic(Gdx.files.internal("data/audio.mp3"));
        //dropSound = Gdx.audio.newSound(Gdx.files.internal("data/audio.ogg"));
        playBtn = new Texture(Gdx.files.internal("data/logo.png"));
        font = new BitmapFont();
        font.setColor(Color.RED);
        createBasicSkin();
        dropSound.setLooping(true);
        dropSound.play();
        dropSound.setVolume(1.0f);
        //dropSound.setLooping(2, true);

    }

    @Override
    public void handleInput() {
        if(Gdx.input.justTouched()){
           // gsm.set(new PlayState(gsm));
        }
    }

    @Override
    public void update(float dt) {
        handleInput();
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.begin();


        sb.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        font.draw(sb, ((String.valueOf(Gdx.graphics.getWidth() / Gdx.graphics.getHeight()))), 200, 200);
        //draw(Texture texture, float x, float y, float width, float height)
        sb.draw(playBtn, 4 * Gdx.graphics.getWidth() / 15, 5 * Gdx.graphics.getHeight() / 8, 3 * Gdx.graphics.getWidth() / 6, 3 * Gdx.graphics.getHeight() / 8);


        sb.end();
    }
    Skin skin;
    private void createBasicSkin(){
        //Create a font
        BitmapFont font = new BitmapFont();
        skin = new Skin();
        skin.add("default", font);

        //Create a texture
        Pixmap pixmap = new Pixmap((int)Gdx.graphics.getWidth()/4,(int)Gdx.graphics.getHeight()/10, Pixmap.Format.RGB888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        skin.add("background",new Texture(pixmap));

        //Create a button style
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.newDrawable("background", Color.GRAY);
        textButtonStyle.down = skin.newDrawable("background", Color.DARK_GRAY);
        textButtonStyle.checked = skin.newDrawable("background", Color.DARK_GRAY);
        textButtonStyle.over = skin.newDrawable("background", Color.LIGHT_GRAY);
        textButtonStyle.font = skin.getFont("default");
        skin.add("default", textButtonStyle);

    }
}