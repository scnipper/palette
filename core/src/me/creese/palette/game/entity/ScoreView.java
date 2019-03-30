package me.creese.palette.game.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Align;

import me.creese.palette.game.screens.Loading;
import me.creese.palette.game.util.FTextures;
import me.creese.palette.game.util.FontUtil;
import me.creese.palette.game.util.P;
import me.creese.palette.game.util.TexturePrepare;

public class ScoreView extends Actor {

    private final Sprite sprite;
    private final Sprite shadow;
    private final BitmapFont font;

    public ScoreView(TexturePrepare prepare) {
        sprite = prepare.getByName(FTextures.SCORE_BACK);

        Color shadowColor = Color.BLACK.cpy();
        shadowColor.a = 0.05f;
        shadow = prepare.getByName(FTextures.SCORE_BACK);
        shadow.setColor(shadowColor);

        setBounds(P.WIDTH/2-sprite.getWidth()/2,P.HEIGHT-sprite.getHeight()-80,sprite.getWidth(),sprite.getHeight());
        sprite.setPosition(getX(),getY());
        shadow.setPosition(getX()+10,getY()-20);
        font = P.get().asset.get(Loading.FONT_ROBOTO_BOLD, BitmapFont.class);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        shadow.draw(batch);
        sprite.draw(batch);
        FontUtil.drawText(batch,font,"0/980px",getX(),getY(),0.6f,P.BLACK_FONT_COLOR,getWidth(), Align.center,false,getHeight());
    }
}
