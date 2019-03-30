package me.creese.palette.game.screens;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.nio.ByteBuffer;
import java.util.ArrayList;

import me.creese.palette.game.entity.BigPixel;
import me.creese.palette.game.entity.GroupPixels;
import me.creese.palette.game.entity.PaletteButtons;
import me.creese.palette.game.entity.PixelsControl;
import me.creese.palette.game.entity.ScoreView;
import me.creese.palette.game.entity.buttons.PaletteButton;
import me.creese.palette.game.entity.buttons.ResForPaletteButtons;
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
        super(new FitViewport(P.WIDTH, P.HEIGHT), root, P.rootBatch);
        palette = new ArrayList<>();


        stagePixel = new Stage(new ExtendViewport(P.WIDTH, P.HEIGHT), P.rootBatch) {
            @Override
            public void draw() {
                super.draw();
                //System.out.println(((SpriteBatch) stagePixel.getBatch()).renderCalls);
            }
        };
        PixelsControl pixelsControl = new PixelsControl(stagePixel);
        addActor(pixelsControl);
        addStage(stagePixel, 0);
        groupPixels = new GroupPixels(pixelsControl);
        stagePixel.addActor(groupPixels);
        TexturePrepare prepare = getRoot().getTransitObject(TexturePrepare.class);
        addActor(new ScoreView(prepare));
        Texture texture = new Texture("image_1.gif");

        generatePalette(texture);

        addPalletteButtons();


    }

    private void addPalletteButtons() {
        TexturePrepare prepare = getRoot().getTransitObject(TexturePrepare.class);
        PaletteButtons paletteButtons = new PaletteButtons();
        addActor(paletteButtons);

        ResForPaletteButtons res = new ResForPaletteButtons(prepare);
        for (int i = 0; i < palette.size(); i++) {
            Color color = palette.get(i);
            PaletteButton button = new PaletteButton(res, i + 1);
            button.setColor(color);
            float w = button.getWidth() + 40;
            button.setX(w * i);
            paletteButtons.addActor(button);
            if (i == palette.size() - 1)
                paletteButtons.setWidth(paletteButtons.getChildren().size * w);
        }
    }

    private void generatePalette(Texture texture) {


        texture.getTextureData().prepare();
        Pixmap pixmap = texture.getTextureData().consumePixmap();

        ByteBuffer pixels = pixmap.getPixels();

        TexturePrepare prepare = getRoot().getTransitObject(TexturePrepare.class);
        BigPixel[][] gridPixels = new BigPixel[texture.getHeight()][texture.getWidth()];


        int posX = 0;
        int posY = 0;
        sprite = prepare.getByName(FTextures.PIXEL_SQUARE);

        int remaining = pixels.remaining();
        for (int i = 0; i < remaining; i += 4) {
            int color = pixels.get(i) & 0x000000ff;


            color = (color << 8) | (pixels.get(i + 1) & 0x000000ff);
            color = (color << 8) | (pixels.get(i + 2) & 0x000000ff);
            color = (color << 8) | (pixels.get(i + 3) & 0x000000ff);


            Color colorObj = new Color(color);
            int numColor = addOrFindColorPalette(colorObj);


            BigPixel bigPixel = new BigPixel(sprite, numColor, colorObj, posX, posY);


            gridPixels[posY][posX] = bigPixel;


            groupPixels.addActor(bigPixel);


            posX++;

            if (posX == texture.getWidth()) {
                posY++;
                posX = 0;
            }

            System.out.println(i / (float) remaining);
        }

        OrthographicCamera camera = (OrthographicCamera) stagePixel.getCamera();
        camera.zoom = 0.2f;
        camera.translate((texture.getWidth() * BigPixel.WIDTH_PIXEL*0.2f) / 2.f, -(texture.getHeight() * BigPixel.HEIGHT_PIXEL*0.2f) / 2.f, 0);

        texture.dispose();


    }

    private int addOrFindColorPalette(Color color) {
        int findColorNum = palette.indexOf(color);
        if (findColorNum == -1) {
            palette.add(color);
            return palette.size();
        } else {
            return findColorNum + 1;
        }
    }

}
