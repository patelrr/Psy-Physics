package com.mygdx.game.states;

import com.badlogic.gdx.Gdx;
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
import com.badlogic.gdx.physics.box2d.QueryCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.handlers.GameStateManager;
import com.mygdx.game.main.Game;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Stack;

import static com.mygdx.game.handlers.B2DVars.PPM;
public class Create extends GameState implements InputProcessor {

	private World world;
	private Box2DDebugRenderer b2dr;
	BitmapFont font;
	SpriteBatch sb;
	Stack<Body> undo;
	ArrayList<Body> all;
	ArrayList<Texture> tall;
	private OrthographicCamera b2dCam;
	ArrayList<Vector2> texturem;
	ArrayList<Array<Vector2>> bodym;
	private Body createPhysicBodies(Array<Vector2> input, World world) {
		System.out.println("Size :"+input.size);
		if(input.size<=2)
			return null;
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		Body body = world.createBody(bodyDef);
		for (int i = 0; i < input.size - 1; i++) {
			Vector2 point = input.get(i);
			Vector2 dir = input.get(i + 1).cpy().sub(point);

			try{
			float distance = dir.len();


				System.out.println("---->"+distance);
				if(distance==0.00)
					continue;
				if(distance/2.f<0.00)
					continue;
				if(Math.abs(distance-1.1920929E-7)<0.001)
					continue;
			//	if(distance<1.1)
			//		continue;

			float angle = dir.angle() * MathUtils.degreesToRadians;

			PolygonShape shape = new PolygonShape();

				shape.setAsBox(distance / 2, 2 / PPM, dir.cpy()
						.scl(0.5f).add(point), angle);
				FixtureDef fixtureDef = new FixtureDef();
				fixtureDef.shape=shape;
				fixtureDef.density = 0.4f;
				fixtureDef.friction = 0.2f;
				fixtureDef.restitution = 0.2f; // Make it bounce a little bit

				body.createFixture(fixtureDef);
				shape.dispose();
			}

			catch(Exception E){}

		}
		undo.push(body);
		all.add(body);
		return body;
	}

	Body bodycircle;
	int dirty=0;
	Texture img;
	public Create(GameStateManager gsm) {

		super(gsm);
		img = new Texture(Gdx.files.internal("data/whiteback.jpg"));
		world = new World(new Vector2(0, -9.81f), true);
		b2dr = new Box2DDebugRenderer();
		ar=new Array<Vector2>();
		undo=new Stack<Body>();
		texturem=new ArrayList<Vector2>();
		bodym=new ArrayList<Array<Vector2>>();
		// create platform
		BodyDef bdef = new BodyDef();
		bdef.position.set(120 / PPM, 120 / PPM);
		bdef.type = BodyType.StaticBody;
		Body body = world.createBody(bdef);

		font = new BitmapFont();
		all= new ArrayList<Body>();
		tall= new ArrayList<Texture>();
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(50 / PPM, 120 / PPM);
		FixtureDef fdef = new FixtureDef();
		fdef.shape = shape;
		body.createFixture(fdef);

		//second
		bdef.position.set(680 / PPM, 120 / PPM);
		bdef.type = BodyType.StaticBody;
		Body bodys = world.createBody(bdef);
		pixmap = new Pixmap( Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight(), Pixmap.Format.RGBA8888);

		pixmap.setColor(new Color(0, 0, 0, 0));
		pixmap.fill();
		pixmap.setColor(1, 1, 0, 0.75f);
		drawLerped(new Vector2(5, 0), new Vector2(5, 10));
		drawLerped(new Vector2(10, 0), new Vector2(10, 10));
		pixmaptex=new Texture(pixmap);
		all.add(body);
		tall.add(pixmaptex);
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
		bodyDef.position.set(670 / PPM, 210 / PPM);
		bodycircle= world.createBody(bodyDef);

// Create a circle shape and set its radius to 6
		CircleShape circle = new CircleShape();
		circle.setRadius(25f/PPM);

// Create a fixture definition to apply our shape to
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = circle;
		fixtureDef.density = 0.05f;
		fixtureDef.friction = 0.1f;
		fixtureDef.restitution = 0.0f; // Make it bounce a little bit

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
		pixmap.setColor(0, 1, 0, 0.75f);
		pixmap.fillCircle(32, 32, 32);
	}

	Vector3 testPoint = new Vector3();
	QueryCallback callback = new QueryCallback() {
		@Override
		public boolean reportFixture (Fixture fixture) {
			// if the hit fixture's body is the ground body
			// we ignore it

			System.out.println("Here");
			hitBody = fixture.getBody();

			// if the hit point is inside the fixture of the body
			// we report it
			if (fixture.testPoint(testPoint.x, testPoint.y)) {
				System.out.println("Here1");
				hitBody = fixture.getBody();
				if(hitBody.equals(bodycircle))
					hitBody.applyForceToCenter(new Vector2(-5, 0), true);
				return false;
			} else
				return true;
		}
	};
	public void handleInput() {}

	public void update(float dt) {
		world.step(dt, 6, 2);
	}
	Pixmap pixmap = new Pixmap( 64, 64, Pixmap.Format.RGBA8888);
	Texture pixmaptex;
	public void render() {


		// clear screen
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        for(int i=0;i<all.size();i++){
			if(all.get(i).getPosition().y<-10){
				world.destroyBody(all.get(i));
				all.remove(i);
				tall.remove(i);
				System.out.println("Removed "+i);
			}
		}

		b2dr.render(world, b2dCam.combined);
		sb.begin();

		//sb.draw(img,0,0);

		for(int i=0;i<tall.size();i++){
			Sprite s=new Sprite(tall.get(i));
			s.setOrigin(all.get(i).getWorldCenter().x, all.get(i).getWorldCenter().y);
			//s.setPosition(all.get(i).getWorldCenter().x*PPM, all.get(i).getWorldCenter().y*PPM);
			s.setPosition(all.get(i).getPosition().x * PPM, all.get(i).getPosition().y * PPM);
			s.setRotation((float) all.get(i).getAngle()* MathUtils.radiansToDegrees);
			s.draw(sb);
/*
			sb.draw(s, s.getX(), s.getY(),s.getOriginX(),
					s.getOriginY(),
					s.getWidth(),s.getHeight(),s.getScaleX(),s.
							getScaleY(),s.getRotation());
							*/

		}

		//pixmaptex= ;

			if(dirty==1) {
				//Pixmap p=pixmap;
				//System.out.println("Hereee "+p.getHeight()+p.getFormat());
				//pixmaptex= new Texture(p);
				runtime.draw(pixmap, 0, 0);
				//pixmap.dispose();

				Sprite s=new Sprite(runtime);
				s.draw(sb);

				//pixmaptex.dispose();
				//p.dispose();

			}

		//pixmap.dispose();
		//sb.draw(pixmaptex, 0, 0);
			//System.out.println("Did");
		/*
		drawLerped(new Vector2(120,0),new Vector2(120,120));
		drawLerped(new Vector2(120,170),new Vector2(120,120));
		drawLerped(new Vector2(120,170),new Vector2(170,170));
		*/

		//pixmaptex.dispose();
		sb.end();
		// draw box2d world



	}
    Texture runtime;
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
	public void updatePoints(Array<Vector2> ar, World world){

		Array<Vector2> arr = new Array<Vector2>();
		Vector2 q = null,r = null;

		System.out.println("Array : -");
		for (int i=0 ; i<ar.size ; i++)
			System.out.println(ar.get(i).x + "|" + ar.get(i).y);

		System.out.println("New array : -");
		for (int i=0 ; i<ar.size-1 ; i++){
			Vector2 p1 = ar.get(i);
			Vector2 p2 = ar.get(i + 1);
			float x,y;
			x = (3*p1.x)/4 + (1*p2.x)/4;
			y = (3*p1.y)/4 + (1*p2.y)/4;
			q = new Vector2(x,y);
			x = (1*p1.x)/4 + (3*p2.x)/4;
			y = (1*p1.y)/4 + (3*p2.y)/4;
			r = new Vector2(x, y);
			System.out.println(q.x+"|"+q.y+" "+r.x+"|" + r.y);
			arr.add(q);
			arr.add(r);
		}
		arr.add(ar.get(ar.size-1));
		createPhysicBodies(arr, world);
	}
	void CreateJason()throws Exception {

		System.out.println(Arrays.toString(texturem.toArray()));
		JSONArray text = new JSONArray();
		JSONObject obj = new JSONObject();

		text.put(texturem);
		obj.put("Texture", text);

		JSONArray points = new JSONArray();
		HashMap hm=new HashMap();
		for (int i = 0; i < bodym.size(); i++) {
			System.out.println(bodym.get(i).toString());
		}
		for (int i = 0; i < bodym.size(); i++) {
			JSONArray pointsx = new JSONArray();
			//pointsx.put(bodym.get(i));
			hm.put(i, bodym.get(i));
			//com.mygdx.game.levels.points.put(i, bodym.get(i));
			//System.out.println(Arrays.toString(bodym.get(i).toArray()));
		}
		points.put(hm);
		JSONObject objs = new JSONObject();
		objs.put("Points", points);
		objs.put("Texture", texturem);

		// try-with-resources statement based on post comment below :)

		FileWriter file = new FileWriter("C:\\Users\\Dell\\Documents\\GitHub\\psy-physics\\core\\src\\com\\mygdx\\game\\levels\\level.json");

		file.write(objs.toString());
		file.flush();
		file.close();
		System.out.println("Successfully Copied JSON Object to File...");
		System.out.println("\nJSON Object: " + obj.toString());
		System.out.println("\nJSON Object: " + objs);

	}
	@Override
	public boolean keyTyped(char character) {

		if (character == 'w') { //it's the 'D' key
			world.destroyBody(all.get(all.size()-1));
			all.remove(all.size() - 1);
			tall.remove(tall.size()-1);
		}
		if(character=='d'){
			try {
				CreateJason();
			}
			catch (Exception e){
				System.out.println("Something went Wrong");
			}
			//Gdx.app.exit();
		}
		return true;
	}
	Body hitBody = null;
	Vector2 last;
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		Vector3 touch = new Vector3();

		b2dCam.unproject(touch.set(screenX, screenY, 0));

		testPoint.set(x, y, 0);
		b2dCam.unproject(testPoint);

		// ask the world which bodies are within the given
		// bounding box around the mouse pointer
		hitBody = null;
		world.QueryAABB(callback, testPoint.x - 0.1f, testPoint.y - 0.1f, testPoint.x + 0.1f, testPoint.y + 0.1f);

		if (hitBody!=null){
			System.out.println("Found Body");
			//hitBody.applyForceToCenter(new Vector2(-5, 0), true);
			/*
			Array<Body> bodies = new Array<Body>();
			world.getBodies(bodies);
    			for(Body b : bodies){
        			if(b.getPosition().y<-20f){
        		world.destroyBody(b);
        	}
    			}
			 */
			//world.destroyBody(hitBody);
		}
		System.out.println("Ready ::"+touch.x*PPM+" "+touch.y*PPM);
		last=new Vector2(touch.x*PPM,touch.y*PPM);
        touch.x=touch.x*PPM;
		touch.y=touch.y*PPM;
		//body.applyForce(0.1f, 0.1f, screenX, screenY, true);
		//makenewPIx();
		pixmap = new Pixmap( Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight(), Pixmap.Format.RGBA8888);
		runtime=new Texture(pixmap);
		pixmap.setColor(new Color(0, 0, 0, 0));
		pixmap.fill();
		pixmap.setColor(0, 1, 0, 0.75f);
		//pixmap.fillCircle(32, 32, 32);
		//pixmap.fillCircle( (int)touch.x, (int)touch.y, 1 );
		ar.clear();
		ar.add(new Vector2(touch.x/PPM, (touch.y)/PPM));
		dirty=1;
		//Texture pixmaptex = new Texture(pi);
		//body.applyTorque(0.4f,true);
		System.out.println("touch Down");
		return true;
	}
	int iit=0;
	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		Vector3 touch = new Vector3();
		dirty=0;
		b2dCam.unproject(touch.set(screenX, screenY, 0));
		touch.x=touch.x*PPM;
		touch.y=touch.y*PPM;
		x=(int) touch.x;
			y = (int) (touch.y);
		ar.add(new Vector2(x / PPM, y / PPM));
		createPhysicBodiesStatic(ar, world);
		bodym.add(iit++,new Array<Vector2>(ar));
		//bodym.add(new Array<Vector2>(ar));
		//updatePoints(ar, world);
		texturem.add(new Vector2((int) last.x, Gdx.graphics.getHeight() - (int) last.y));
		texturem.add(new Vector2(x, Gdx.graphics.getHeight() - y));
		drawLerped(new Vector2((int) last.x, Gdx.graphics.getHeight() - (int) last.y), new Vector2(x, Gdx.graphics.getHeight() - y));

		pixmaptex= new Texture( pixmap );
		tall.add(pixmaptex);
		pixmap.dispose();
		//createPhysicBodies(ar,world);
		//createbody(ar, world);
		//pixmap.dispose();
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
		bodyDef.type = BodyType.DynamicBody;
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
		//dirty=0;
		touch.x=touch.x*PPM;
		touch.y=touch.y*PPM;
		System.out.println(touch.x+" "+touch.y);
		System.out.println("Ready ::"+touch.x+" "+touch.y);
		texturem.add(new Vector2((int) last.x, Gdx.graphics.getHeight() - (int) last.y));
		texturem.add(new Vector2(touch.x, Gdx.graphics.getHeight() - touch.y));
			System.out.println("UReady ::"+touch.x+" "+touch.y);
			drawLerped(new Vector2((int) last.x, Gdx.graphics.getHeight() - (int) last.y), new Vector2(touch.x, Gdx.graphics.getHeight() - touch.y));
			last=new Vector2(touch.x, touch.y);


		//System.out.println(Gdx.graphics.getWidth()+" "+Gdx.graphics.getHeight());
		//y=Gdx.graphics.getHeight()-screenY;
		if(Math.sqrt(Math.pow((touch.x-x),2)+Math.pow((touch.y - y),2))>20) {
			x= (int) touch.x;y= (int) (touch.y);
			count++;

			//if(count<8)
				ar.add(new Vector2(x / PPM, y / PPM));
			System.out.println("Last" + last.x + " " + last.y);
			//pixmap.drawLine((int) last.x, Gdx.graphics.getHeight() - (int) last.y, x, Gdx.graphics.getHeight() - y);
			//drawLerped(new Vector2((int) last.x, Gdx.graphics.getHeight() - (int) last.y), new Vector2(touch.x, Gdx.graphics.getHeight() - touch.y));
			///pixmap.fillCircle(x, y,5 );
			System.out.println("touch Drr" + x + " " + y);

		}
		//pi.drawCircle(x % 100, x%100, 3);
		//pi.fillCircle(x % 100, y%100,5);

		return true;
	}
	int brushSize=2;
	private void drawDot(Vector2 spot) {
		pixmap.fillCircle((int) spot.x, (int) spot.y, brushSize);
	}

	public void draw(Vector2 spot) {
		drawDot(spot);

	}

	public void drawLerped(Vector2 from, Vector2 to) {
		float dist = to.dst(from);
			/* Calc an alpha step to put one dot roughly every 1/8 of the brush
			 * radius. 1/8 is arbitrary, but the results are fairly nice. */
		float alphaStep = brushSize / (4f * dist);

		for (float a = 0; a < 1f; a += alphaStep) {
			Vector2 lerped = from.lerp(to, a);
			drawDot(lerped);
		}

		drawDot(to);

	}
	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}
	private Body createPhysicBodiesStatic(Array<Vector2> input, World world) {
		System.out.println("Size :"+input.size);
		if(input.size<=2)
			return null;
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.StaticBody;
		Body body = world.createBody(bodyDef);
		for (int i = 0; i < input.size - 1; i++) {
			Vector2 point = input.get(i);
			Vector2 dir = input.get(i + 1).cpy().sub(point);

			try{
				float distance = dir.len();


				System.out.println("---->"+distance);
				if(distance==0.00)
					continue;
				if(distance/2.f<0.00)
					continue;
				if(Math.abs(distance-1.1920929E-7)<0.001)
					continue;
				//	if(distance<1.1)
				//		continue;

				float angle = dir.angle() * MathUtils.degreesToRadians;

				PolygonShape shape = new PolygonShape();

				shape.setAsBox(distance / 2, 2 / PPM, dir.cpy()
						.scl(0.5f).add(point), angle);
				FixtureDef fixtureDef = new FixtureDef();
				fixtureDef.shape=shape;
				fixtureDef.density = 0.4f;
				fixtureDef.friction = 0.2f;
				fixtureDef.restitution = 0.2f; // Make it bounce a little bit

				body.createFixture(fixtureDef);
				shape.dispose();
			}

			catch(Exception E){}

		}
		undo.push(body);
		all.add(body);
		return body;
	}

}









