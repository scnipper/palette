package me.creese.palette.game.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.nio.ByteBuffer;
import java.util.ArrayList;

import me.creese.palette.game.entity.BigPixel;
import me.creese.palette.game.util.P;
import me.creese.util.display.Display;
import me.creese.util.display.GameView;

public class MainScreen extends GameView {
    private final ArrayList<Color> palette;

    public MainScreen(Display root) {
        super(new FitViewport(P.WIDTH, P.HEIGHT), root);


        Texture texture = new Texture("image_1.gif");

        palette = new ArrayList<>();
        texture.getTextureData().prepare();
        Pixmap pixmap = texture.getTextureData().consumePixmap();

        ByteBuffer pixels = pixmap.getPixels();

        BigPixel[][] gridPixels = new BigPixel[texture.getHeight()][texture.getWidth()];


        int posX = 0;
        int posY = 0;
        for (int i = 0; i < pixels.remaining(); i+=4) {
            int color = pixels.get(i) & 0x000000ff;


            color = (color << 8) | (pixels.get(i+1)& 0x000000ff);
            color = (color << 8) | (pixels.get(i+2)& 0x000000ff);
            color = (color << 8) | (pixels.get(i+3)& 0x000000ff);


            Color colorObj = new Color(color);
            int numColor = addOrFindColorPalette(colorObj);

            gridPixels[posY][posX] = new BigPixel(numColor,colorObj,posX,posY);
            posX++;

            if(posX == texture.getWidth()) {
                posY++;
                posX = 0;
            }
        }



    }

    private int addOrFindColorPalette(Color color) {
        int findColorNum = palette.indexOf(color);
        if (findColorNum == -1) {
            palette.add(color);
            return palette.size();
        } else {
            return findColorNum+1;
        }
    }

}
