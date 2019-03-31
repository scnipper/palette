package me.creese.palette.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.util.ArrayList;

import me.creese.palette.game.entity.BigPixel;
import me.creese.palette.game.entity.GroupPixels;
import me.creese.palette.game.entity.PaletteButtons;
import me.creese.palette.game.entity.PixelsControl;
import me.creese.palette.game.entity.ScoreView;
import me.creese.palette.game.entity.SquadPixel;
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
        TexturePrepare prepare = getRoot().getTransitObject(TexturePrepare.class);
        groupPixels = new GroupPixels(pixelsControl, prepare);
        stagePixel.addActor(groupPixels);


        addActor(new ScoreView(prepare));
        Texture texture = new Texture("image_2.gif");

        long start = System.nanoTime();
        new Thread(() -> {
            generatePalette(texture);
            addPalletteButtons();
        }).start();


        System.out.println(System.nanoTime()-start);



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

        BigPixel[][] gridPixels = new BigPixel[texture.getHeight()][texture.getWidth()];




        for (int i = 0; i < texture.getHeight(); i++) {
            for (int j = 0; j < texture.getWidth(); j++) {

                int color = pixmap.getPixel(j, i);
                Color colorObj = new Color(color);
                int numColor = addOrFindColorPalette(colorObj);


                BigPixel bigPixel = new BigPixel(numColor, colorObj, j, i);

                gridPixels[i][j] = bigPixel;

            }

        }

        int xSquadCount = (int) Math.ceil(texture.getWidth() / (float)SquadPixel.WIDTH_SQUAD);
        int ySquadCount = (int) Math.ceil(texture.getHeight() / (float)SquadPixel.HEIGHT_SQUAD);


                for (int i = 0; i < ySquadCount; i++) {
                    for (int j = 0; j < xSquadCount; j++) {


                        int finalI = i;
                        int finalJ = j;
                        Gdx.app.postRunnable(() -> {
                            SquadPixel squadPixel = new SquadPixel(gridPixels, finalJ * SquadPixel.WIDTH_SQUAD, finalI * SquadPixel.HEIGHT_SQUAD);
                            groupPixels.addActor(squadPixel);
                        });

                    }
                }




        //groupPixels.setGridPixels(gridPixels);

        OrthographicCamera camera = (OrthographicCamera) stagePixel.getCamera();
        //camera.zoom = 0.2f;

        //camera.translate((texture.getWidth() * BigPixel.WIDTH_PIXEL) / 2.f, -(texture.getHeight() * BigPixel.HEIGHT_PIXEL) / 2.f, 0);

        Gdx.app.postRunnable(() -> texture.dispose());



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
