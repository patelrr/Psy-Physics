package com.mygdx.game;

/**
 * Created by Dell on 18-02-2016.
 */

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.utils.viewport.Viewport;

public class LineDrawingTest extends GdxTest {
        Stage stage;

        public void create () {
            stage = new Stage();
            Gdx.input.setInputProcessor(stage);

            Skin skin = new Skin(Gdx.files.internal("data/uiskin.json"));

            Touchpad touchpad = new Touchpad(20, skin);
            touchpad.setBounds(15, 15, 100, 100);
            stage.addActor(touchpad);
        }

        public void render () {
            // System.out.println(meow.getValue());
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            stage.act(Gdx.graphics.getDeltaTime());
            stage.draw();
        }

        public void resize (int width, int height) {
            stage.setViewport(new Viewport() {
                @Override
                public void update(int screenWidth, int screenHeight, boolean centerCamera) {
                    super.update(screenWidth, screenHeight, centerCamera);
                }
            });
        }

        public boolean needsGL20 () {
            return false;
        }

        public void dispose () {
            stage.dispose();
        }
    }