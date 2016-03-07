package com.mygdx.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.game.main.Game;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = Game.TITLE;
		config.width = 800;
		config.height = 600;

		/*
		config.width = 1024;
		config.height = 732;
		*/

		new LwjglApplication(new Game(), config);
	}
}
