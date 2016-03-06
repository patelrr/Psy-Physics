package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.Stateso.GameStateManagero;
import com.mygdx.game.Stateso.MenuState;

/**
 * Created by Dell on 18-02-2016.
 */
public class Main extends ApplicationAdapter {
    public static final int WIDTH = 480;
    public static final int HEIGHT = 800;
    public static final float SCALE = 0.5f;
    public static final String TITLE = "FlappyBird";
    private int w,h;
    private SpriteBatch spriteBatch;
    private GameStateManagero gameStateManagero;
    private Sound dropSound;
    @Override
    public void create () {
        spriteBatch = new SpriteBatch();
        gameStateManagero = new GameStateManagero();
        gameStateManagero.push(new MenuState(gameStateManagero));
        dropSound = Gdx.audio.newSound(Gdx.files.internal("data/audio.ogg"));
        dropSound.setLooping(2, true);
        dropSound.play();
        Gdx.gl.glClearColor(0, 0, 0, 0);
    }

    @Override
    public void render () {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        gameStateManagero.update(Gdx.graphics.getDeltaTime());
        gameStateManagero.render(spriteBatch);
    }

}