package me.creese.palette.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
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
import me.creese.palette.game.util.P;
import me.creese.palette.game.util.TexturePrepare;
import me.creese.util.display.Display;
import me.creese.util.display.GameView;

public class MainScreen extends GameView {
    private final ArrayList<Color> palette;

    private final Stage stagePixel;
    private final GroupPixels groupPixels;
    private final PixelsControl pixelsControl;
    private final ScoreView scoreView;
    private PaletteButtons paletteButtons;

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
        pixelsControl = new PixelsControl(stagePixel);
        addActor(pixelsControl);
        addStage(stagePixel, 0);
        groupPixels = new GroupPixels(root);
        stagePixel.addActor(groupPixels);


        TexturePrepare prepare = getRoot().getTransitObject(TexturePrepare.class);
        scoreView = new ScoreView(prepare);
        getRoot().addTransitObject(scoreView);
        addActor(scoreView);
        Texture texture = new Texture("image_1.gif");

        long start = System.nanoTime();
        new Thread(() -> {
            generatePalette(texture);
            addPalletteButtons();
        }).start();


        System.out.println(System.nanoTime() - start);


    }

    private void addPalletteButtons() {
        TexturePrepare prepare = getRoot().getTransitObject(TexturePrepare.class);
        paletteButtons = new PaletteButtons();
        addActor(paletteButtons);


        ResForPaletteButtons res = new ResForPaletteButtons(prepare);
        for (int i = 0; i < palette.size(); i++) {
            Color color = palette.get(i);
            PaletteButton button = new PaletteButton(res, i + 1);
            if(i == 0) {
                button.setSelect(true);
                paletteButtons.setSelectButton(button);
            }
            button.setColor(color);
            float w = button.getWidth() + 40;
            button.setX(w * i);
            paletteButtons.addActor(button);
            if (i == palette.size() - 1)
                paletteButtons.setWidth(paletteButtons.getChildren().size * w);
        }
    }

    private void generatePalette(Texture texture) {


        scoreView.setTotalPixels(texture.getWidth()*texture.getHeight());

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

        int xSquadCount = (int) Math.ceil(texture.getWidth() / (float) SquadPixel.WIDTH_SQUAD);
        int ySquadCount = (int) Math.ceil(texture.getHeight() / (float) SquadPixel.HEIGHT_SQUAD);


        for (int i = 0; i < ySquadCount; i++) {
            for (int j = 0; j < xSquadCount; j++) {


                int finalJ = j;
                int finalI = i;
                Gdx.app.postRunnable(() -> {
                    SquadPixel squadPixel = new SquadPixel(getRoot(),gridPixels, finalJ * SquadPixel.WIDTH_SQUAD, finalI * SquadPixel.HEIGHT_SQUAD);
                    groupPixels.addActor(squadPixel);
                });

            }
        }

        pixelsControl.setRealSize(texture.getWidth()*BigPixel.WIDTH_PIXEL,texture.getHeight()*BigPixel.HEIGHT_PIXEL);


        //groupPixels.setGridPixels(gridPixels);

        OrthographicCamera camera = (OrthographicCamera) stagePixel.getCamera();
        camera.zoom = P.START_ZOOM;

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

    public PixelsControl getPixelsControl() {
        return pixelsControl;
    }

    public PaletteButtons getPaletteButtons() {
        return paletteButtons;
    }
}
