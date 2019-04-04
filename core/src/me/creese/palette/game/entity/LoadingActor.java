package me.creese.palette.game.entity;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Align;

import me.creese.palette.game.screens.Loading;
import me.creese.palette.game.util.FontUtil;
import me.creese.palette.game.util.P;

public class LoadingActor extends Actor {

    private final BitmapFont font;
    private String loadingText;

    public LoadingActor() {
        font = P.get().asset.get(Loading.FONT_ROBOTO_BOLD, BitmapFont.class);
        setPercent(0);
    }

    public void setPercent(float percent) {

        int p = (int) (percent * 100);

        loadingText = "Загрузка "+ p +" %";

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {

        FontUtil.drawText(batch,font,loadingText,0,P.HEIGHT-400,0.8f,P.BLACK_FONT_COLOR,P.WIDTH, Align.center);
    }
}
