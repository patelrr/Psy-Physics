package com.mygdx.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.handlers.GameStateManager;
import com.mygdx.game.main.Game;

import static com.mygdx.game.handlers.B2DVars.PPM;

public class Play extends GameState implements InputProcessor {
	
	private World world;
	private Box2DDebugRenderer b2dr;
	BitmapFont font;
	SpriteBatch sb;
	private OrthographicCamera b2dCam;
	private Body createPhysicBodies(Array<Vector2> input, World world) {
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		Body body = world.createBody(bodyDef);
		for (int i = 0; i < input.size - 1; i++) {
			Vector2 point = input.get(i);
			Vector2 dir = input.get(i + 1).cpy().sub(point);
			float distance = dir.len();
			float angle = dir.angle() * MathUtils.degreesToRadians;
			PolygonShape shape = new PolygonShape();
			shape.setAsBox(distance / 2, 3 / 2, dir.cpy()
					.scl(0.5f).add(point), angle);
			body.createFixture(shape, 1.0f);
		}
		return body;
	}
	public Play(GameStateManager gsm) {
		
		super(gsm);

		world = new World(new Vector2(0, -9.81f), true);
		b2dr = new Box2DDebugRenderer();
		ar=new Array<Vector2>();
		// create platform
		BodyDef bdef = new BodyDef();
		bdef.position.set(160 / PPM, 120 / PPM);
		bdef.type = BodyType.StaticBody;
		Body body = world.createBody(bdef);
		font = new BitmapFont();
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(50 / PPM, 5 / PPM);
		FixtureDef fdef = new FixtureDef();
		fdef.shape = shape;
		body.createFixture(fdef);
		font.setColor(Color.RED);
		// create falling box
		bdef.position.set(160 / PPM, 200 / PPM);
		bdef.type = BodyType.DynamicBody;
		body = world.createBody(bdef);
		
		shape.setAsBox(5 / PPM, 5 / PPM);
		fdef.shape = shape;
		body.createFixture(fdef);
		
		// set up box2d cam
		b2dCam = new OrthographicCamera();
		b2dCam.setToOrtho(false, Game.V_WIDTH / PPM, Game.V_HEIGHT / PPM);
		sb = new SpriteBatch();
		Gdx.input.setInputProcessor(this);
		
	}
	
	public void handleInput() {}
	
	public void update(float dt) {
		world.step(dt, 6, 2);
	}
	
	public void render() {

		// clear screen
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);



		// draw box2d world
		b2dr.render(world, b2dCam.combined);

		
	}

	@Override
	public void rendersb(SpriteBatch sb) {
		font.draw(sb, "jk,bk,", 100, 100);
	}

	public void dispose() {


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


		//body.applyForce(0.1f, 0.1f, screenX, screenY, true);
		//makenewPIx();
		ar.clear();
		ar.add(new Vector2(x/PPM, y/PPM));

		//Texture pixmaptex = new Texture(pi);
		//body.applyTorque(0.4f,true);
		System.out.println("touch Down");
		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {

		createbody(ar, world);

		count=0;
		//pre=b;
		System.out.println("touch Up");

		return true;
	}
	void createbody(Array<Vector2> input, World world){
		BodyDef bodyDef = new BodyDef();
		bodyDef.position.set(input.get(0).x /
						PPM,
				(input.get(0).y / PPM));
		bodyDef.type = BodyDef.BodyType.DynamicBody;
		Body body = world.createBody(bodyDef);
		System.out.println(input.size);
		if(input.size<2)
			return;
		if(input.size<=3){
			EdgeShape es=new EdgeShape();
			es.set(input.get(0),input.get(1));
			body.createFixture(es, 1.0f);
			return;
		}
		PolygonShape shape = new PolygonShape();
		Vector2 tmp[]=new Vector2[input.size-1];
		for(int i=0;i<input.size-1;i++){
			tmp[i]=input.get(i);
		}
		shape.set(tmp);
		body.createFixture(shape, 1.0f);
		shape.dispose();
	}
	int x=0,y=0;
	Array<Vector2> ar;
	int count=0;
	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {

		if(Math.sqrt(Math.pow((screenX-x),2)*Math.pow((screenY - y),2))>10) {
			x=screenX;y=screenY;
			count++;
			if(count<8)
				ar.add(new Vector2(x / PPM, y / PPM));

			System.out.println("touch Drr" + x + " " + y);

		}
		//pi.drawCircle(x % 100, x%100, 3);
		//pi.fillCircle(x % 100, y%100,5);

		return true;
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









