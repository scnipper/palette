package me.creese.palette.game.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

import javax.imageio.ImageIO;

import me.creese.palette.game.PaletteStart;
import me.creese.palette.game.util.AdUtil;
import me.creese.palette.game.util.GetImage;
import me.creese.palette.game.util.P;

public class DesktopLauncher implements AdUtil,FilenameFilter{



	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 360;
		config.height = 640;
		new LwjglApplication(new PaletteStart(new DesktopLauncher()), config);
	}

	@Override
	public void requestImagePath(GetImage getImage) {

		FileDialog dialog = new FileDialog((Frame)null, "Select File to Open");
		dialog.setMode(FileDialog.LOAD);

		dialog.setFilenameFilter(this::accept);
		dialog.setVisible(true);
		String directory = dialog.getDirectory();
		String name = dialog.getFile();
		if(name != null && directory != null) {
			String file = directory + name;
			Gdx.app.postRunnable(() -> {
				File path = new File(file);

				BufferedImage bimg = null;
				try {
					bimg = ImageIO.read(path);
				} catch (IOException e) {
					e.printStackTrace();
				}
				int width          = bimg.getWidth();
				int height         = bimg.getHeight();

				if(width > P.MAX_IMAGE_WIDTH || height > P.MAX_IMAGE_HEIGHT){
					showToast("Превышен размер картинки");
				} else
				getImage.loadImage(path);
			});
		}

	}

	@Override
	public void showToast(String text) {
		System.out.println(text);
	}

	@Override
	public void showDialogExit(Runnable afterOk) {
		afterOk.run();
	}

	@Override
	public boolean accept(File dir, String name) {
		File tempFile = new File(String.format("%s/%s", dir.getPath(), name));
		if (tempFile.isFile()) return tempFile.getName().matches("(.*/)*.+\\.(png|jpg|gif|jpeg|PNG|JPG|GIF|JPEG)$");
		return true;
	}
}
