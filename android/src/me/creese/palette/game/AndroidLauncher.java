package me.creese.palette.game;

import android.Manifest;
import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
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
    private static final int REQUEST_PERMISSIONS = 1337;
    private GetImage getImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        initialize(new PaletteStart(this), config);
    }

    @Override
    public void requestImagePath(GetImage getImage) {


        runOnUiThread(() -> {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    openFileManager(getImage);
                } else {
                    this.getImage = getImage;
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_PERMISSIONS);
                }
            } else
            openFileManager(getImage);
        });

    }
    private void hidingNavBar() {
        View decorView = getWindow().getDecorView();

        int currentApiVersion = Build.VERSION.SDK_INT;


        int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;

        if (currentApiVersion >= 19) {
            flags |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        }
        decorView.setSystemUiVisibility(flags);
    }

    private void openFileManager(GetImage getImage) {
        OpenFileDialog openFileDialog = new OpenFileDialog(AndroidLauncher.this);
        openFileDialog.setFilter("(.*/)*.+\\.(png|jpg|gif|jpeg|PNG|JPG|GIF|JPEG)$","^[^.].*[^-_.]$");
        openFileDialog.setOpenDialogListener(fileName -> {
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
                File fileImage = new File(dataDir.getAbsoluteFile() + "/" + dataDir.list().length + "_" + random.nextInt(150000));

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
        });
        openFileDialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        hidingNavBar();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,  String[] permissions,  int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_PERMISSIONS && permissions.length == 1) {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED && getImage != null) {
                openFileManager(getImage);
            }
        }

    }

    @Override
    public void showToast(String text) {
        runOnUiThread(() -> Toast.makeText(AndroidLauncher.this, text, Toast.LENGTH_SHORT).show());
    }

    @Override
    public void showDialogExit(Runnable afterOk) {
        runOnUiThread(() -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(AndroidLauncher.this);

            builder.setMessage("Прогресс будет потерян.").setTitle("Выйти в меню?").setPositiveButton("OK", (dialog, which) -> {
                dialog.dismiss();
                Gdx.app.postRunnable(afterOk);
            }).setNegativeButton("Отмена", (dialog, which) -> dialog.dismiss()).show();
        });
    }
}
