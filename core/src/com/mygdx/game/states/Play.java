package com.mygdx.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Fixture;
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
	private static Body createPhysicBodies(Array<Vector2> input, World world) {
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		Body body = world.createBody(bodyDef);
		for (int i = 0; i < input.size - 1; i++) {
			Vector2 point = input.get(i);
			Vector2 dir = input.get(i + 1).cpy().sub(point);

			try{
			float distance = dir.len();


				System.out.println("---->"+distance);
				if(distance<0.00)
					continue;
			//	if(distance<1.1)
			//		continue;

			float angle = dir.angle() * MathUtils.degreesToRadians;

			PolygonShape shape = new PolygonShape();

				shape.setAsBox(distance / 2, 1 / PPM, dir.cpy()
						.scl(0.5f).add(point), angle);
				body.createFixture(shape, 1.0f);
			}
			catch(Exception E){}

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
		bdef.position.set(80 / PPM, 120 / PPM);
		bdef.type = BodyType.StaticBody;
		Body body = world.createBody(bdef);
		font = new BitmapFont();

		PolygonShape shape = new PolygonShape();
		shape.setAsBox(50 / PPM, 120 / PPM);
		FixtureDef fdef = new FixtureDef();
		fdef.shape = shape;
		body.createFixture(fdef);

		//second
		bdef.position.set(500 / PPM, 120 / PPM);
		bdef.type = BodyType.StaticBody;
		Body bodys = world.createBody(bdef);

		PolygonShape shapes = new PolygonShape();
		shapes.setAsBox(50 / PPM, 120 / PPM);
		FixtureDef fdefs = new FixtureDef();
		fdefs.shape = shapes;
		bodys.createFixture(fdefs);


		//circle
		BodyDef bodyDef = new BodyDef();
// We set our body to dynamic, for something like ground which doesn't move we would set it to StaticBody
		bodyDef.type = BodyType.DynamicBody;
// Set our body's starting position in the world
		bodyDef.position.set(510 / PPM, 130 / PPM);
		Body bodycircle = world.createBody(bodyDef);

// Create a circle shape and set its radius to 6
		CircleShape circle = new CircleShape();
		circle.setRadius(10f/PPM);

// Create a fixture definition to apply our shape to
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = circle;
		fixtureDef.density = 0.5f;
		fixtureDef.friction = 0.4f;
		fixtureDef.restitution = 0.2f; // Make it bounce a little bit

// Create our fixture and attach it to the body
		Fixture fixture = bodycircle.createFixture(fixtureDef);

// Remember to dispose of any shapes after you're done with them!
// BodyDef and FixtureDef don't need disposing, but shapes do.
		circle.dispose();
		// create falling box
		/*
		bdef.position.set(160 / PPM, 200 / PPM);
		bdef.type = BodyType.DynamicBody;
		body = world.createBody(bdef);
		
		shape.setAsBox(20 / PPM, 20 / PPM);
		fdef.shape = shape;
		body.createFixture(fdef);
		*/
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
		Vector3 touch = new Vector3();
		b2dCam.unproject(touch.set(screenX, screenY, 0));
        touch.x=touch.x*PPM;
		touch.y=touch.y*PPM;
		//body.applyForce(0.1f, 0.1f, screenX, screenY, true);
		//makenewPIx();
		ar.clear();
		ar.add(new Vector2(touch.x/PPM, (touch.y)/PPM));

		//Texture pixmaptex = new Texture(pi);
		//body.applyTorque(0.4f,true);
		System.out.println("touch Down");
		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		Vector3 touch = new Vector3();
		b2dCam.unproject(touch.set(screenX, screenY, 0));
		touch.x=touch.x*PPM;
		touch.y=touch.y*PPM;
		x=(int)touch.x;y= (int) (touch.y);
		ar.add(new Vector2(x / PPM, y / PPM));
		createPhysicBodies(ar,world);
		//createbody(ar, world);

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
		Vector3 touch = new Vector3();
		b2dCam.unproject(touch.set(screenX, screenY, 0));
		touch.x=touch.x*PPM;
		touch.y=touch.y*PPM;
		System.out.println(touch.x+" "+touch.y);
		//System.out.println(Gdx.graphics.getWidth()+" "+Gdx.graphics.getHeight());
		//y=Gdx.graphics.getHeight()-screenY;
		if(Math.sqrt(Math.pow((touch.x-x),2)*Math.pow((touch.y - y),2))>50) {
			x= (int) touch.x;y= (int) (touch.y);
			count++;
			//if(count<8)
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









