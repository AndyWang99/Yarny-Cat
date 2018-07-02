package com.sodirea.yarnycat.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.sodirea.yarnycat.YarnyCat;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		new LwjglApplication(new YarnyCat(), config);
		config.width = YarnyCat.WIDTH;
		config.height = YarnyCat.HEIGHT;
		config.title = YarnyCat.TITLE;
	}
}
