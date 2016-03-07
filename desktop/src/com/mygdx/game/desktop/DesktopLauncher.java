package com.mygdx.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.game.main.Game;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = Game.TITLE;
		config.width = Game.V_WIDTH;
		config.height = Game.V_HEIGHT;

		/*
		config.width = 1024;
		config.height = 732;
		*/

		new LwjglApplication(new Game(), config);
	}
}
