package me.creese.palette.game.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFontCache;
import com.badlogic.gdx.graphics.g2d.PolygonBatch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.nio.ByteBuffer;
import java.util.ArrayList;

import me.creese.palette.game.entity.BigPixel;
import me.creese.palette.game.entity.GroupPixels;
import me.creese.palette.game.entity.PixelsControl;
import me.creese.palette.game.util.FTextures;
import me.creese.palette.game.util.P;
import me.creese.palette.game.util.TexturePrepare;
import me.creese.util.display.Display;
import me.creese.util.display.GameView;

public class MainScreen extends GameView {
    private final ArrayList<Color> palette;

    private final Stage stagePixel;
    private final GroupPixels groupPixels;
    private Sprite sprite;

    public MainScreen(Display root) {
        super(new FitViewport(P.WIDTH, P.HEIGHT), root);
        palette = new ArrayList<>();


        stagePixel = new Stage(new ExtendViewport(P.WIDTH, P.HEIGHT),new SpriteBatch(8191)){
            @Override
            public void draw() {
                super.draw();
                //System.out.println(((SpriteBatch) stagePixel.getBatch()).renderCalls);
            }
        };
        addStage(stagePixel);
        groupPixels = new GroupPixels();
        stagePixel.addActor(groupPixels);

        addActor(new PixelsControl(stagePixel));

        generatePalette();

    }

    private void generatePalette() {
        Texture texture = new Texture("image_1.gif");


        texture.getTextureData().prepare();
        Pixmap pixmap = texture.getTextureData().consumePixmap();

        ByteBuffer pixels = pixmap.getPixels();

        TexturePrepare prepare = getRoot().getTransitObject(TexturePrepare.class);
        BigPixel[][] gridPixels = new BigPixel[texture.getHeight()][texture.getWidth()];


        int posX = 0;
        int posY = 0;
        sprite = prepare.getByName(FTextures.PIXEL_SQUARE);

        for (int i = 0; i < pixels.remaining(); i+=4) {
            int color = pixels.get(i) & 0x000000ff;


            color = (color << 8) | (pixels.get(i+1)& 0x000000ff);
            color = (color << 8) | (pixels.get(i+2)& 0x000000ff);
            color = (color << 8) | (pixels.get(i+3)& 0x000000ff);


            Color colorObj = new Color(color);
            int numColor = addOrFindColorPalette(colorObj);


            BigPixel bigPixel = new BigPixel(sprite, numColor, colorObj, posX, posY);


            gridPixels[posY][posX] = bigPixel;

            groupPixels.addActor(bigPixel);
            posX++;

            if(posX == texture.getWidth()) {
                posY++;
                posX = 0;
            }
        }

        texture.dispose();
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
