package me.creese.palette.game.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
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
import me.creese.palette.game.entity.WinMenu;
import me.creese.palette.game.entity.bonus.BonusGroup;
import me.creese.palette.game.entity.buttons.PaletteButton;
import me.creese.palette.game.entity.buttons.ResForPaletteButtons;
import me.creese.palette.game.entity.buttons.SelectImageBtn;
import me.creese.palette.game.util.AdUtil;
import me.creese.palette.game.util.MaxPaletteException;
import me.creese.palette.game.util.P;
import me.creese.palette.game.util.S;
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
    private final BonusGroup bonusGroup;
    private final ArrayList<BigPixel> history;
    private final Actor historyActor;
    private PaletteButtons paletteButtons;
    private int xSquadCount;
    private int ySquadCount;
    private boolean isStartLoading;
    private int xCurrSquad;
    private int yCurrSquad;
    private BigPixel[][] gridPixels;
    private int startIndexHistory;
    private int perIndexCount;
    private long startTime;
    private WinMenu winMenu;
    private boolean isEnd;
    private int numImage;

    public GameScreen(Display root) {
        super(new FitViewport(P.WIDTH, P.HEIGHT), root, P.rootBatch);
        palette = new ArrayList<>();
        history = new ArrayList<>();
        historyActor = new Actor();
        addActor(historyActor);
        loadingActor = new LoadingActor();

        stagePixel = new Stage(new ExtendViewport(P.WIDTH, P.HEIGHT), P.rootBatch) {
            @Override
            public void draw() {
                super.draw();
                //System.out.println(((SpriteBatch) stagePixel.getBatch()).renderCalls);
            }
        };
        TexturePrepare prepare = getRoot().getTransitObject(TexturePrepare.class);
        bonusGroup = new BonusGroup(prepare);
        pixelsControl = new PixelsControl(stagePixel,bonusGroup);
        addActor(pixelsControl);
        addActor(bonusGroup);
        addStage(stagePixel, 0);
        groupPixels = new GroupPixels(root);
        stagePixel.addActor(groupPixels);


        scoreView = new ScoreView(root);
        getRoot().addTransitObject(scoreView);

        winMenu = new WinMenu(getRoot());

    }


    /**
     * Конец игры
     */
    public void gameOver() {


        if(numImage != -1) {
            P.get().saves.putInteger(S.IMG+numImage, SelectImageBtn.OPEN);
            P.get().saves.putInteger(S.IMG+(numImage+1), SelectImageBtn.UNLOCK);
            P.get().saves.flush();
        }


        pixelsControl.setTouchable(Touchable.disabled);
        pixelsControl.animateZoomToMax();
        bonusGroup.setLock(true);
        bonusGroup.clear();
        bonusGroup.setActivateBonus(null);
        paletteButtons.clear();
        scoreView.remove();
        groupPixels.fillAllPixels(Color.WHITE);

        isEnd = true;
        winMenu.setEndTime(System.currentTimeMillis() - startTime);



        historyActor.addAction(Actions.sequence(Actions.forever(Actions.run(() -> {
            if(pixelsControl.isMaxZoom()) {
                for (int i = 0; i < perIndexCount; i++) {
                    int index = startIndexHistory + i;
                    if (index < history.size()) {
                        BigPixel bigPixel = history.get(index);
                        bigPixel.getSquad().redrawOnePixel(bigPixel.getPosX(), bigPixel.getPosY());
                    }
                }
                startIndexHistory += perIndexCount;
                if (startIndexHistory >= history.size()) {
                    historyActor.getActions().clear();

                    historyActor.addAction(Actions.sequence(Actions.delay(1.5f),Actions.run(new Runnable() {
                        @Override
                        public void run() {

                            addActor(winMenu);
                        }
                    })));

                }
            }
        }))));
    }

    /**
     * Начало игры
     * @param texture текстура которая будет преобразована в сетку
     * @param numImage
     * @throws MaxPaletteException
     */
    public void startGame(Texture texture, int numImage) throws MaxPaletteException {

        this.numImage = numImage;
        isEnd = false;
        winMenu.remove();
        perIndexCount = 5;

        int pixels = texture.getHeight() * texture.getWidth();

        if(pixels > 10000) {
            perIndexCount = 10;
        }
        if(pixels > 20000) {
            perIndexCount = 20;
        }
        if(pixels > 40000) {
            perIndexCount = 30;
        }

        if(pixels > 70000) {
            perIndexCount = 50;
        }

        startIndexHistory = 0;
        addActor(scoreView);
        bonusGroup.clear();
        bonusGroup.setActivateBonus(null);
        bonusGroup.setLock(false);
        if (paletteButtons != null) {
            paletteButtons.clearChildren();
        }
        history.clear();
        historyActor.getActions().clear();
        groupPixels.clear();
        palette.clear();
        generatePalette(texture);


        scoreView.setCurrPixels(0);
        startTime = System.currentTimeMillis();
        pixelsControl.setTouchable(Touchable.disabled);
        addActor(loadingActor);
        addPaletteButtons();
        getRoot().getGameViewForName(MainScreen.class).onBackPress();
    }

    /**
     * Добавление кнопок с палитрой
     */
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

    /**
     * Создание палитры
     * @param texture
     * @throws MaxPaletteException
     */
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

        camera.position.set(P.WIDTH/2, P.HEIGHT/2, 0);
        camera.translate((texture.getWidth() * BigPixel.WIDTH_PIXEL*camera.zoom) / 2.f,
                -(texture.getHeight() * BigPixel.HEIGHT_PIXEL *camera.zoom) / 2.f, 0);

        texture.dispose();


    }

    /**
     * Добавление цвета в палитру или если он добавлен только возвращать номер
     * @param color
     * @return
     */
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

    public ArrayList<BigPixel> getHistory() {
        return history;
    }

    public long getStartTime() {
        return startTime;
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

        if(!isEnd)
        getRoot().getTransitObject(AdUtil.class).showDialogExit(() -> getRoot().showGameView(MainScreen.class));
        else getRoot().showGameView(MainScreen.class);

    }

    public PixelsControl getPixelsControl() {
        return pixelsControl;
    }

    public PaletteButtons getPaletteButtons() {
        return paletteButtons;
    }
}
