package me.creese.palette.game.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.utils.SnapshotArray;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.util.ArrayList;

import me.creese.palette.game.entity.BigPixel;
import me.creese.palette.game.entity.GroupPixels;
import me.creese.palette.game.entity.LoadingActor;
import me.creese.palette.game.entity.PaletteButtons;
import me.creese.palette.game.entity.PixelsControl;
import me.creese.palette.game.entity.ScoreView;
import me.creese.palette.game.entity.SquadPixel;
import me.creese.palette.game.entity.buttons.PaletteButton;
import me.creese.palette.game.entity.buttons.ResForPaletteButtons;
import me.creese.palette.game.util.MaxPaletteException;
import me.creese.palette.game.util.P;
import me.creese.palette.game.util.TexturePrepare;
import me.creese.util.display.Display;
import me.creese.util.display.GameView;

public class GameScreen extends GameView {
    private final ArrayList<Color> palette;

    private final Stage stagePixel;
    private final GroupPixels groupPixels;
    private final PixelsControl pixelsControl;
    private final ScoreView scoreView;
    private final LoadingActor loadingActor;
    private PaletteButtons paletteButtons;
    private int xSquadCount;
    private int ySquadCount;
    private boolean isStartLoading;
    private int xCurrSquad;
    private int yCurrSquad;
    private BigPixel[][] gridPixels;

    public GameScreen(Display root) {
        super(new FitViewport(P.WIDTH, P.HEIGHT), root, P.rootBatch);
        palette = new ArrayList<>();

        loadingActor = new LoadingActor();

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


    }


    public void startGame(Texture texture) throws MaxPaletteException {

        if (paletteButtons != null) {
            paletteButtons.clearChildren();
        }
        groupPixels.clear();
        palette.clear();
        generatePalette(texture);
        pixelsControl.setTouchable(Touchable.disabled);
        addActor(loadingActor);
        addPaletteButtons();
        getRoot().getGameViewForName(MainScreen.class).onBackPress();
    }

    private void addPaletteButtons() {
        TexturePrepare prepare = getRoot().getTransitObject(TexturePrepare.class);
        paletteButtons = new PaletteButtons();
        addActor(paletteButtons);


        ResForPaletteButtons res = new ResForPaletteButtons(prepare);
        for (int i = 0; i < palette.size(); i++) {
            Color color = palette.get(i);
            PaletteButton button = new PaletteButton(res, i + 1);
            if (i == 0) {
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

    private void generatePalette(Texture texture) throws MaxPaletteException {


        scoreView.setTotalPixels(texture.getWidth() * texture.getHeight());

        texture.getTextureData().prepare();
        Pixmap pixmap = texture.getTextureData().consumePixmap();

        gridPixels = new BigPixel[texture.getHeight()][texture.getWidth()];


        for (int i = 0; i < texture.getHeight(); i++) {
            for (int j = 0; j < texture.getWidth(); j++) {

                int color = pixmap.getPixel(j, i);
                Color colorObj = new Color(color);
                int numColor = addOrFindColorPalette(colorObj);
                if(numColor > P.MAX_PALETTE_SIZE) {
                    throw new MaxPaletteException("Palette > "+P.MAX_PALETTE_SIZE);
                }

                BigPixel bigPixel = new BigPixel(numColor, colorObj, j, i);

                gridPixels[i][j] = bigPixel;

            }

        }

        xCurrSquad = 0;
        yCurrSquad = 0;
        xSquadCount = (int) Math.ceil(texture.getWidth() / (float) SquadPixel.WIDTH_SQUAD);
        ySquadCount = (int) Math.ceil(texture.getHeight() / (float) SquadPixel.HEIGHT_SQUAD);

        isStartLoading = true;

        pixelsControl.setRealSize(texture.getWidth() * BigPixel.WIDTH_PIXEL, texture.getHeight() * BigPixel.HEIGHT_PIXEL);

        OrthographicCamera camera = (OrthographicCamera) stagePixel.getCamera();
        camera.zoom = P.START_ZOOM;

        camera.translate((texture.getWidth() * BigPixel.WIDTH_PIXEL*camera.zoom) / 2.f,
                -(texture.getHeight() * BigPixel.HEIGHT_PIXEL *camera.zoom) / 2.f, 0);

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

    public GroupPixels getGroupPixels() {
        return groupPixels;
    }

    @Override
    protected void postRender() {
        if(isStartLoading) {
            SquadPixel squadPixel = new SquadPixel(getRoot(), gridPixels, xCurrSquad * SquadPixel.WIDTH_SQUAD,
                    yCurrSquad * SquadPixel.HEIGHT_SQUAD);
            groupPixels.addActor(squadPixel);

            xCurrSquad++;

            loadingActor.setPercent((yCurrSquad*xSquadCount + xSquadCount) / ((float)xSquadCount * ySquadCount));
            if(xCurrSquad == xSquadCount) {
                xCurrSquad = 0;
                yCurrSquad++;
                if(yCurrSquad == ySquadCount) {
                    isStartLoading = false;
                    loadingActor.remove();
                    pixelsControl.setTouchable(Touchable.enabled);
                }
            }
        }

    }

    @Override
    public void onBackPress() {
        getRoot().showGameView(getRoot().getPrevView());
    }

    public PixelsControl getPixelsControl() {
        return pixelsControl;
    }

    public PaletteButtons getPaletteButtons() {
        return paletteButtons;
    }
}
