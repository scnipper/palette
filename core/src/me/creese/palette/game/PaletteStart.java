package me.creese.palette.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import me.creese.palette.game.entity.buttons.SelectImageBtn;
import me.creese.palette.game.screens.GameScreen;
import me.creese.palette.game.screens.Loading;
import me.creese.palette.game.screens.MainScreen;
import me.creese.palette.game.util.AdUtil;
import me.creese.palette.game.util.P;
import me.creese.palette.game.util.S;
import me.creese.palette.game.util.TexturePrepare;
import me.creese.util.display.Display;

public class PaletteStart extends Display {

    private final AdUtil adutil;

    public PaletteStart(AdUtil adutil) {
        this.adutil = adutil;

    }

    @Override
    public void create() {
        P.get().rootBatch = new SpriteBatch(2000) {
            @Override
            public void end() {
                super.end();
                //System.out.println("render calls" + renderCalls);
            }
        };
        addTransitObject(AdUtil.class.getName(), adutil);
        P.get().saves = Gdx.app.getPreferences("sav");
        String keyImg0 = S.IMG + "0";
        if (P.get().saves.getInteger(keyImg0) == 0) {
            P.get().saves.putInteger(keyImg0, SelectImageBtn.UNLOCK);

        }

        long launches = P.get().saves.getLong(S.COUNT_LAUNCH);
        launches++;
        P.get().saves.putLong(S.COUNT_LAUNCH, launches);


        P.get().saves.flush();
        Gdx.input.setCatchBackKey(true);
        setBackgroundColor(P.BACKGROUND_COLOR);
        addListGameViews(new Loading(this));
        showGameView(Loading.class);

    }

    /**
     * Загрузка заверешена создаем остальные экраны и показываем главный
     */
    public void loadOk() {
        addListGameViews(new MainScreen(this));
        addListGameViews(new GameScreen(this));


        showGameView(MainScreen.class);
    }

    @Override
    public void render() {
        super.render();
        //Gdx.app.log("FPS", String.valueOf(1/ Gdx.graphics.getDeltaTime()));
    }

    @Override
    public void pause() {
        super.pause();
        P.get().saves.flush();
    }

    @Override
    public void dispose() {
        super.dispose();
        GameScreen gameScreen = getGameViewForName(GameScreen.class);
        if (gameScreen != null) {
            gameScreen.getGroupPixels().clear();
        }
        P.get().asset.dispose();
        P.get().saves.flush();

        getTransitObject(TexturePrepare.class).dispose();
    }
}
