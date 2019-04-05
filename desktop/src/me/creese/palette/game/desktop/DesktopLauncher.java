package me.creese.palette.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import java.io.File;

import me.creese.palette.game.PaletteStart;
import me.creese.palette.game.util.AdUtil;
import me.creese.palette.game.util.GetImage;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 360;
		config.height = 640;
		new LwjglApplication(new PaletteStart(new AdUtil() {
			@Override
			public void requestImagePath(GetImage getImage) {

			}

			@Override
			public void showToast(String text) {

			}

		}), config);
	}
}
