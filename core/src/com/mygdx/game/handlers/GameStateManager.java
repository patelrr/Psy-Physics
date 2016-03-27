package com.mygdx.game.handlers;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.main.Game;
import com.mygdx.game.states.Create;
import com.mygdx.game.states.GameState;
import com.mygdx.game.states.MainMenu;
import com.mygdx.game.states.Play;

import java.util.Stack;

public class GameStateManager {
	
	private Game game;
	
	private Stack<GameState> gameStates;
	
	public static final int PLAY = 912837;
	public static final int MENU = 912830;
	public static final int CREATE = 912831;
	
	public GameStateManager(Game game) {
		this.game = game;
		gameStates = new Stack<GameState>();

		pushState(PLAY);
		//pushState(MENU);
		//pushState(CREATE);
	}
	
	public Game game() { return game; }
	
	public void update(float dt) {
		gameStates.peek().update(dt);
	}
	
	public void render() {
		gameStates.peek().render();
	}

	public void rendersb(SpriteBatch sb) {
		gameStates.peek().rendersb(sb);
	}
	
	private GameState getState(int state) {
		if(state == PLAY) return new Play(this);
		if(state==MENU) return new MainMenu(this);
		if(state==CREATE) return new Create(this);
		return null;
	}
	
	public void setState(int state) {
		popState();
		pushState(state);
	}
	
	public void pushState(int state) {
		gameStates.push(getState(state));
	}
	
	public void popState() {
		GameState g = gameStates.pop();
		g.dispose();
	}
	public void resize(int w,int h){
		gameStates.peek().resize(w,h);
	}
	
}















