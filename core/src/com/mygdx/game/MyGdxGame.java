package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

public class MyGdxGame extends ApplicationAdapter implements InputProcessor {
	SpriteBatch batch;
	Sprite sprite;
	Texture img;
	World world;
	Body body;
	Body bodyEdgeScreen;
	Box2DDebugRenderer debugRenderer;
	Matrix4 debugMatrix;
	OrthographicCamera camera;
	BitmapFont font;
    int x=0,y=0;
    Array<Vector2> ar;
	float torque = 0.0f;
	boolean drawSprite = true;

	final float PIXELS_TO_METERS = 1000f;

	@Override
	public void create() {

		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");
		sprite = new Sprite(img);
		ar=new Array<Vector2>();
		sprite.setPosition(-sprite.getWidth()/2,-sprite.getHeight()/2);

		world = new World(new Vector2(0, -1f),true);

		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.DynamicBody;
		bodyDef.position.set((sprite.getX() + sprite.getWidth()/2) /
						PIXELS_TO_METERS,
				(sprite.getY() + sprite.getHeight()/2) / PIXELS_TO_METERS);

		body = world.createBody(bodyDef);

		PolygonShape shape = new PolygonShape();
		shape.setAsBox(sprite.getWidth()/2 / PIXELS_TO_METERS, sprite.getHeight()
				/2 / PIXELS_TO_METERS);

		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;
		fixtureDef.density = 0.1f;
		fixtureDef.restitution = 0.1f;

		body.createFixture(fixtureDef);
		shape.dispose();

		BodyDef bodyDef2 = new BodyDef();
		bodyDef2.type = BodyDef.BodyType.StaticBody;
		float w = Gdx.graphics.getWidth()/PIXELS_TO_METERS;
		// Set the height to just 50 pixels above the bottom of the screen so we can see the edge in the
		// debug renderer
		float h = Gdx.graphics.getHeight()/PIXELS_TO_METERS- 50/PIXELS_TO_METERS;
		//bodyDef2.position.set(0,
//                h-10/PIXELS_TO_METERS);
		bodyDef2.position.set(0,0);
		FixtureDef fixtureDef2 = new FixtureDef();

		EdgeShape edgeShape = new EdgeShape();
		edgeShape.set(-w/2,-h/2,w/2,-h/2);
		fixtureDef2.shape = edgeShape;

		bodyEdgeScreen = world.createBody(bodyDef2);
		bodyEdgeScreen.createFixture(fixtureDef2);
		edgeShape.dispose();
		createbodys(world);
		Gdx.input.setInputProcessor(this);

		debugRenderer = new Box2DDebugRenderer();
		font = new BitmapFont();
		font.setColor(Color.BLACK);
		camera = new OrthographicCamera(Gdx.graphics.getWidth(),Gdx.graphics.
				getHeight());
	}

	private float elapsed = 0;
	@Override
	public void render() {
		camera.update();
		// Step the physics simulation forward at a rate of 60hz
		world.step(1f / 60f, 6, 2);

		body.applyTorque(torque, true);

		sprite.setPosition((body.getPosition().x * PIXELS_TO_METERS) - sprite.
						getWidth() / 2,
				(body.getPosition().y * PIXELS_TO_METERS) - sprite.getHeight() / 2)
		;
		sprite.setRotation((float) Math.toDegrees(body.getAngle()));

		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.setProjectionMatrix(camera.combined);
		debugMatrix = batch.getProjectionMatrix().cpy().scale(PIXELS_TO_METERS,
				PIXELS_TO_METERS, 0);
		batch.begin();
		/*
        try{
			Texture pixmaptex = new Texture(pi);
			Sprite sp=new Sprite(pixmaptex);
			sp.setPosition(pre.getPosition().x,pre.getPosition().y);
			batch.draw(sp,sp.getX(), sp.getY(),sp.getOriginX(),
					sp.getOriginY(),
					sp.getWidth(),sp.getHeight(),sp.getScaleX(),sp.
							getScaleY(),sp.getRotation());
		}
		catch(Exception e){}
		/*
		if(drawSprite)
			batch.draw(sprite, sprite.getX(), sprite.getY(),sprite.getOriginX(),
					sprite.getOriginY(),
					sprite.getWidth(),sprite.getHeight(),sprite.getScaleX(),sprite.
							getScaleY(),sprite.getRotation());
							*/

		font.draw(batch,
				"Restitution: " + body.getFixtureList().first().getRestitution()+" "+x+" "+y,
				-Gdx.graphics.getWidth()/2,
				Gdx.graphics.getHeight()/2 );
		batch.end();

		debugRenderer.render(world, debugMatrix);
	}

	@Override
	public void dispose() {
		img.dispose();
		world.dispose();
	}
	private Body createPhysicBodies(Array<Vector2> input, World world) {
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.DynamicBody;
		Body body = world.createBody(bodyDef);
		for (int i = 0; i < input.size - 1; i++) {
			Vector2 point = input.get(i);
			Vector2 dir = input.get(i + 1).cpy().sub(point);
			float distance = dir.len();
			float angle = dir.angle() * MathUtils.degreesToRadians;
			PolygonShape shape = new PolygonShape();
			shape.setAsBox(distance / 2, 3 / 1, dir.cpy()
					.scl(0.5f).add(point), angle);
			body.createFixture(shape, 1.0f);
			//shape.dispose();
		}
		return body;
	}
	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {


		if(keycode == Input.Keys.RIGHT)
			body.setLinearVelocity(1f, 0f);
		if(keycode == Input.Keys.LEFT)
			body.setLinearVelocity(-1f,0f);

		if(keycode == Input.Keys.UP)
			body.applyForceToCenter(0f,10f,true);
		if(keycode == Input.Keys.DOWN)
			body.applyForceToCenter(0f, -10f, true);

		// On brackets ( [ ] ) apply torque, either clock or counterclockwise
		if(keycode == Input.Keys.RIGHT_BRACKET)
			torque += 0.1f;
		if(keycode == Input.Keys.LEFT_BRACKET)
			torque -= 0.1f;

		// Remove the torque using backslash /
		if(keycode == Input.Keys.BACKSLASH)
			torque = 0.0f;

		// If user hits spacebar, reset everything back to normal
		if(keycode == Input.Keys.SPACE|| keycode == Input.Keys.NUM_2) {
			body.setLinearVelocity(0f, 0f);
			body.setAngularVelocity(0f);
			torque = 0f;
			sprite.setPosition(0f,0f);
			body.setTransform(0f,0f,0f);
		}

		if(keycode == Input.Keys.COMMA) {
			body.getFixtureList().first().setRestitution(body.getFixtureList().first().getRestitution()-0.1f);
		}
		if(keycode == Input.Keys.PERIOD) {
			body.getFixtureList().first().setRestitution(body.getFixtureList().first().getRestitution()+0.1f);
		}
		if(keycode == Input.Keys.ESCAPE || keycode == Input.Keys.NUM_1)
			drawSprite = !drawSprite;

		return true;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}
   Pixmap pi;
	Body pre;
   void makenewPIx(){
	   Pixmap pixmap = new Pixmap( 100, 100, Pixmap.Format.RGBA8888 );
	   pixmap.setColor(0, 1, 0, 0.75f);
	   //pixmap.fillCircle( 32, 32, 32 );
	   pi=pixmap;

	   //pixmap.dispose();
   }
	void createbodys( World world){
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.DynamicBody;
		Body body = world.createBody(bodyDef);


			EdgeShape es=new EdgeShape();
			es.set(1/PIXELS_TO_METERS, 1/PIXELS_TO_METERS, 1/PIXELS_TO_METERS, 2/PIXELS_TO_METERS);
			body.createFixture(es, 1.0f);
			return;



	}
	// On touch we apply force from the direction of the users touch.
	// This could result in the object "spinning"
	void createbody(Array<Vector2> input, World world){
		BodyDef bodyDef = new BodyDef();
		bodyDef.position.set(input.get(0).x /
						PIXELS_TO_METERS,
				(input.get(0).y / PIXELS_TO_METERS));
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
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		body.applyForce(0.1f, 0.1f, screenX, screenY, true);
		//makenewPIx();
		ar.clear();
		ar.add(new Vector2(x, y));

		//Texture pixmaptex = new Texture(pi);
		//body.applyTorque(0.4f,true);
		System.out.println("touch Down");
		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		createbody(ar, world);
		createbodys(world);
		count=0;
		//pre=b;
		System.out.println("touch Up");

		return true;

	}
	int count=0;
	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {

		if(Math.sqrt(Math.pow((screenX-x),2)*Math.pow((screenY - y),2))>10) {
			x=screenX;y=screenY;
			count++;
			if(count<8)
				ar.add(new Vector2(x / PIXELS_TO_METERS, y / PIXELS_TO_METERS));

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