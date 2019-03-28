package me.creese.palette.game;

import com.badlogic.gdx.Gdx;

import me.creese.palette.game.screens.Loading;
import me.creese.palette.game.screens.MainScreen;
import me.creese.palette.game.util.P;
import me.creese.util.display.Display;

public class PaletteStart extends Display {
    public PaletteStart() {

    }

    @Override
    public void create() {
        setBackgroundColor(P.BACKGROUND_COLOR);
        addListGameViews(new Loading(this));
        showGameView(Loading.class);

    }

    public void loadOk() {
        addListGameViews(new MainScreen(this));


        showGameView(MainScreen.class);
    }

    @Override
    public void render() {
        super.render();
        Gdx.app.log("FPS", String.valueOf(1/ Gdx.graphics.getDeltaTime()));
    }
}
