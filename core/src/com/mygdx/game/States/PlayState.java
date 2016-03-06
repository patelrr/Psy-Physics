package com.mygdx.game.States;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.Stateso.GameStateManagero;
import com.mygdx.game.Stateso.State;

/**
 * Created by HP on 05-03-2016.
 */
public class PlayState extends State {
    Texture background = new Texture("back.jpg");
    protected PlayState(GameStateManagero gsm) {
        super(gsm);
    }

    @Override
    public void handleInput() {

    }

    @Override
    public void update(float dt) {

    }

    @Override
    public void render(SpriteBatch sb) {
        sb.begin();
        sb.draw(background,0,0, Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        sb.end();
    }
}
