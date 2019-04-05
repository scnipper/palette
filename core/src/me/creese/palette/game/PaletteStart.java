package me.creese.palette.game;

import com.badlogic.gdx.Gdx;

import me.creese.palette.game.screens.Loading;
import me.creese.palette.game.screens.GameScreen;
import me.creese.palette.game.screens.MainScreen;
import me.creese.palette.game.util.AdUtil;
import me.creese.palette.game.util.P;
import me.creese.util.display.Display;

public class PaletteStart extends Display {

    private final AdUtil adutil;

    public PaletteStart(AdUtil adutil) {
        this.adutil = adutil;

    }

    @Override
    public void create() {
        addTransitObject(AdUtil.class.getName(),adutil);
        P.get().saves = Gdx.app.getPreferences("sav");
        Gdx.input.setCatchBackKey(true);
        setBackgroundColor(P.BACKGROUND_COLOR);
        addListGameViews(new Loading(this));
        showGameView(Loading.class);

    }

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
        P.get().saves.flush();
    }
}
