package com.mygdx.game.States;

/**
 * Created by Dell on 18-02-2016.
 */


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.Stateso.GameStateManagero;
import com.mygdx.game.Stateso.State;

/**
 * Created by Brent on 6/25/2015.
 */
public class MenuState extends State {
    Texture background;
    Texture playBtn;
    private Music dropSound;
    private BitmapFont font;
    public MenuState(GameStateManagero gsm) {
        super(gsm);
        background = new Texture(Gdx.files.internal("data/back.jpg"));
        dropSound = Gdx.audio.newMusic(Gdx.files.internal("data/audio.mp3"));
        //dropSound = Gdx.audio.newSound(Gdx.files.internal("data/audio.ogg"));
        playBtn = new Texture(Gdx.files.internal("data/logo.png"));
        font = new BitmapFont();
        font.setColor(Color.RED);
        //createBasicSkin();
        dropSound.setLooping(true);
        dropSound.play();
        dropSound.setVolume(1.0f);
        //dropSound.setLooping(2, true);

    }

    @Override
    public void handleInput() {
        if(Gdx.input.justTouched()){
            gsm.set(new com.mygdx.game.States.PlayState(gsm));
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
}