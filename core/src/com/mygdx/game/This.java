package com.mygdx.game;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.HashMap;
import java.util.Map;




public class This implements ApplicationListener, InputProcessor {
    private SpriteBatch batch;
    private BitmapFont font;
    private String message = "Touch something already!";
    private int w,h;
    private Texture pixmapTexture;
    private Pixmap pl;
    private int width;
    private int height;
    class TouchInfo {
        public float touchX = 0;
        public float touchY = 0;
        public boolean touched = false;
    }

    private Map<Integer,TouchInfo> touches = new HashMap<Integer,TouchInfo>();

    @Override
    public void create() {
        w = Gdx.graphics.getWidth();
        h = Gdx.graphics.getHeight();



        width = (int)Math.round(w);
        height = (int)Math.round(h);
        batch = new SpriteBatch();
        font = new BitmapFont();
        pl=new Pixmap( width, height, Pixmap.Format.RGB565 );
        pl.setColor(Color.RED);
        pixmapTexture = new Texture(pl, Pixmap.Format.RGB565, false);

        font.setColor(Color.RED);

        Gdx.input.setInputProcessor(this);
        for(int i = 0; i < 5; i++){
            touches.put(i, new TouchInfo());
        }
    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();

        message = "";
        for(int i = 0; i < 5; i++){
            if(touches.get(i).touched)
                message += "Finger:" + Integer.toString(i) + "touch at:" +
                        Float.toString(touches.get(i).touchX) +
                        "," +
                        Float.toString(touches.get(i).touchY) +
                        "\n";

        }
        GlyphLayout layout = new GlyphLayout();
        ///layout.setText();

        float x = w/2 - layout.width/2;
        float y = h/2 + layout.height/2;
        font.draw(batch, message, x, y);
        batch.draw(pixmapTexture,1f / 2f, h / w / 2f);
        batch.end();
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public boolean keyDown(int keycode) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if(pointer < 5){
            touches.get(pointer).touchX = screenX;
            touches.get(pointer).touchY = screenY;
            touches.get(pointer).touched = true;
        }
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if(pointer < 5){
            touches.get(pointer).touchX = 0;
            touches.get(pointer).touchY = 0;
            touches.get(pointer).touched = false;
        }
        prex=0;
        prey=0;
        return true;
    }
    int prex=0;
    int prey=0;
    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if(pointer < 5){
            touches.get(pointer).touchX = screenX;
            touches.get(pointer).touchY = screenY;
           /// pl.drawPixel(screenX,screenY);
            touches.get(pointer).touched = true;
            pl.setColor(Color.CYAN);
            if(prey==0&&prex==0)
                pl.drawPixel(screenX, screenY);
            else
                pl.drawLine(screenX,screenY,prex,prey);

            pixmapTexture = new Texture(pl, Pixmap.Format.RGB565, false);
            prex=screenX;
            prey=screenY;
        }
        return true;

    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        // TODO Auto-generated method stub
        return false;
    }
}