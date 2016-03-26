package com.mygdx.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.handlers.GameStateManager;
import com.mygdx.game.main.Game;
import com.mygdx.game.view.Button;

import static com.mygdx.game.handlers.B2DVars.PPM;

/**
 * Created by Dell on 24-03-2016.
 */
public class MainMenu extends GameState implements InputProcessor {
/*
        imageProvider = game.getImageProvider();
		imageProvider.load();
        backgroundImage = imageProvider.getBackgroundSpring();
        TextureRegion buttonBg = imageProvider.getButton();
        buttons = new Button [3];
        buttons[0] = new Button(buttonBg, imageProvider.getStart());
        buttons[1] = new Button(buttonBg, imageProvider.getKids());
        buttons[2] = new Button(buttonBg, imageProvider.getScores());
        helpButton = new Button(imageProvider.getHelp());

        soundButtons = new Button[2];
        soundButtons[0] = new Button(imageProvider.getSoundImage(false));
        soundButtons[1] = new Button(imageProvider.getSoundImage(true));

        camera = new OrthographicCamera();
        camera.setToOrtho(false, imageProvider.getScreenWidth(), imageProvider.getScreenHeight());
        batch = new SpriteBatch();

        logo = imageProvider.getLogo();
        logoX = (imageProvider.getScreenWidth() - logo.getRegionWidth())/2;
        logoY = (imageProvider.getScreenHeight() - logo.getRegionHeight() - 10)-50;

        int buttonMargin = 15;
        int buttonsHeight = 3*buttonMargin;
        for(int i=0; i<buttons.length; i++) {
        	buttonsHeight += buttons[i].getRegionHeight();
        }

        for(int i=buttons.length-1;i>=0;i--) {
        	int x, y;
        	x = (imageProvider.getScreenWidth() - buttons[i].getRegionWidth())/2;
        	if (i == buttons.length - 1) {
        	y = ((imageProvider.getScreenHeight() - buttonsHeight) / 2) - 10;
        	}
        	else {
        		y = ((int) buttons[i+1].getPosY()) +
        			buttons[i+1].getRegionHeight() + buttonMargin;
        	}
        	buttons[i].setPos(x, y);
        }

        float x = imageProvider.getScreenWidth() - helpButton.getRegionWidth() - 10;
        float y = 10;
        helpButton.setPos(x, y);

        soundButtons[0].setPos(10, 10);
        soundButtons[1].setPos(10, 10);

        Gdx.input.setInputProcessor(this);
 */
    private Stage stage; //** stage holds the Button **//
    private BitmapFont font;
    private TextureAtlas buttonsAtlas; //** image of buttons **//
    private Skin buttonSkin,textSkin; //** images are used as skins of the button **//
    private TextButton button;
    TextureRegion logo,play,credit;
    Table root;
    Texture background,wood;
    private TextureAtlas textatlas;
    private Viewport viewport;
    private OrthographicCamera camera;
    public Button buttons[];
    public MainMenu(final GameStateManager gsm) {
        super(gsm);
        background =new Texture(Gdx.files.internal("background.jpg"));

        buttonsAtlas = new TextureAtlas("dataa/button.pack"); //**button atlas image **//
        textatlas = new TextureAtlas("dataa/text.atlas");
        wood=new Texture(Gdx.files.internal("dataa/wood.png"));
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Game.V_WIDTH / PPM, Game.V_HEIGHT / PPM);

        root = new Table();
        root.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());


        buttonSkin = new Skin();
        buttonSkin.addRegions(buttonsAtlas); //** skins for on and off **//

        textSkin= new Skin();
        textSkin.addRegions(textatlas);
        font = new BitmapFont(Gdx.files.internal("data/arial-15.fnt"),false); //** font **//

        stage = new Stage();        //** window is stage **//
        stage.clear();
        Gdx.input.setInputProcessor(stage); //** stage is responsive **//

        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle(); //** Button properties **//
        style.up = buttonSkin.getDrawable("buttonOff");
        style.down = buttonSkin.getDrawable("buttonOn");

        style.font = font;

        button = new TextButton("START", style);
        //** Button text and style **//
        button.setHeight(Gdx.graphics.getHeight() / 3); //** Button Height **//
        button.setWidth(Gdx.graphics.getWidth() / 4); //** Button Width **//

        button.setPosition(Gdx.graphics.getWidth() / 2 - button.getWidth() / 2, Gdx.graphics.getHeight());

        button.addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.log("my app", "Pressed"); //** Usually used to start Game, etc. **//


                // TODO Auto-generated method stub


                return true;

            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.log("my app", "Rggggggeleased");

                ///and level
                gsm.setState(gsm.PLAY);

                dispose();

            }
        });



        MoveToAction moveAction = new MoveToAction();//Add dynamic movement effects to button
        moveAction.setPosition(Gdx.graphics.getWidth() / 2 - button.getWidth() / 2, Gdx.graphics.getHeight() / 2 + Gdx.graphics.getHeight() / 6);
        moveAction.setDuration(.5f);
        button.addAction(moveAction);
        stage.addActor(root);
        root.debug();



        //Image imaget = new Image(textSkin.getDrawable("backgroundtext"));
        logo=new TextureRegion(textatlas.findRegion("backgroundtext"));
        play=new TextureRegion(textatlas.findRegion("play"));
        credit=new TextureRegion(textatlas.findRegion("credit"));

        buttons = new Button [3];
        buttons[0] = new Button(play);

        buttons[0].setPos(525,200);
       // TextureRegionDrawable trd=new TextureRegionDrawable(new TextureRegion(new Texture(textSkin.get("backgroundtext"))));

       // Stack stack = new Stack();

       // stack.add(imaget);
// creating the group
        /*
        WidgetGroup group = new WidgetGroup();


        group.addActor(image);
        group.addActor(imaget);
        root.setBounds(0, 0, Gdx.graphics.getWidth(), 50);
        root.debug().left().top().add(group);
        */


        //image.setScaling(Scaling.fill);
        //root.add(imaget).height(200).fill(400,100);

        //stage.addActor(button);
        Gdx.input.setInputProcessor(this);
    }



    @Override
    public void handleInput() {

    }

    @Override
    public void update(float dt) {

    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        sb.begin();
        sb.draw(background, 0, 0);
        sb.draw(wood, 400, 0);
        buttons[0].draw(sb);
        sb.draw(credit,525,140);
        sb.draw(logo,10,350);
        //stage.draw();
        sb.end();
    }

    @Override
    public void rendersb(SpriteBatch sb) {

    }

    @Override
    public void dispose() {
        // TODO Auto-generated method stub

    }

    @Override
    public void resize(int w, int h) {

    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {

        /*
        for(int i=0;i<buttons.length;i++) {
            if (buttons[i].isPressed(touchPos)) {
                if (i == 0) {
                    gsm.setState(GameStateManager.PLAY);
                }

                else if (i == 1) {
                    game.startGameKidsMode();
                    game.gotoGameScreen(null);
                }
                else if (i == 2) {
                    game.showHighscores();
                }
                break;

            }
        }
*/
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        Vector3 touchPos = new Vector3();
        touchPos.set(screenX, screenY, 0);
        camera.unproject(touchPos);
        System.out.println(touchPos.x + " " + touchPos.y);
        //System.out.println(buttons[0].isPressed(touchPos));
        System.out.println(touchPos.x + " " + touchPos.y);
        if(buttons[0].isPressed(touchPos)){
            System.out.println("Hwre");
            gsm.pushState(GameStateManager.PLAY);

        }
        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}