package me.creese.palette.game;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Toast;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

import me.creese.palette.game.util.AdUtil;
import me.creese.palette.game.util.GetImage;
import me.creese.palette.game.util.P;

public class AndroidLauncher extends AndroidApplication implements AdUtil {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        initialize(new PaletteStart(this), config);
    }

    @Override
    public void requestImagePath(GetImage getImage) {


        runOnUiThread(() -> {
            OpenFileDialog openFileDialog = new OpenFileDialog(AndroidLauncher.this);
            openFileDialog.setFilter("(.*/)*.+\\.(png|jpg|gif|jpeg|PNG|JPG|GIF|JPEG)$","^[^.].*[^-_.]$");
            openFileDialog.setOpenDialogListener(new OpenFileDialog.OpenDialogListener() {
                @Override
                public void OnSelectedFile(String fileName) {
                    BitmapFactory.Options opts = new BitmapFactory.Options();
                    opts.inJustDecodeBounds = true;
                    BitmapFactory.decodeFile(fileName, opts);

                    if (opts.outHeight > P.MAX_IMAGE_HEIGHT || opts.outWidth > P.MAX_IMAGE_WIDTH) {
                        showToast("Изображение должно быть не больше " + P.MAX_IMAGE_WIDTH + "x" + P.MAX_IMAGE_HEIGHT);
                    } else {
                        File dataDir = new File(getFilesDir().getAbsoluteFile() + "/images");
                        if (!dataDir.exists()) {
                            dataDir.mkdirs();
                        }

                        Random random = new Random();
                        File fileImage = new File(dataDir.getAbsoluteFile() + "/" + dataDir.list().length+"_"+random.nextInt(150000));

                        try {
                            if (fileImage.createNewFile()) {
                                FileInputStream fileInputStream = new FileInputStream(fileName);

                                FileOutputStream fileOutputStream = new FileOutputStream(fileImage);

                                int read = fileInputStream.read();
                                while (read != -1) {
                                    fileOutputStream.write(read);
                                    read = fileInputStream.read();
                                }

                                Gdx.app.postRunnable(() -> getImage.loadImage(fileImage));
                                return;


                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }


                    }


                    getImage.loadImage(null);
                }
            });
            openFileDialog.show();
        });

    }

    @Override
    public void showToast(String text) {
        runOnUiThread(() -> Toast.makeText(AndroidLauncher.this, text, Toast.LENGTH_SHORT).show());
    }
}
