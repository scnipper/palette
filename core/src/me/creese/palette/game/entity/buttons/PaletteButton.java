package me.creese.palette.game.entity.buttons;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.utils.Align;

import me.creese.palette.game.entity.PaletteButtons;
import me.creese.palette.game.screens.Loading;
import me.creese.palette.game.util.FontUtil;
import me.creese.palette.game.util.P;

public class PaletteButton extends Actor {


    private final ResForPaletteButtons res;
    private final String text;
    private final BitmapFont font;

    public PaletteButton(ResForPaletteButtons res, int num) {
        this.res = res;
        text = String.valueOf(num);
        setWidth(res.circle.getWidth());
        setHeight(res.circle.getHeight());

        addListener(new ActorGestureListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                PaletteButtons parent = (PaletteButtons) getParent();
                if (!parent.isPan()) {

                    System.out.println("down palette");
                }



            }
        });
        font = P.get().asset.get(Loading.FONT_ROBOTO_BOLD, BitmapFont.class);

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {

        res.circle.setPosition(getX(), getY());
        res.strokeCircle.setPosition(getX(), getY());
        res.shine.setPosition(getX() + getWidth() / 2 - res.shine.getWidth() / 2, getY() + getHeight() / 2 - res.shine.getHeight() / 2);
        res.circle.setColor(getColor());
        res.circle.draw(batch);
        res.strokeCircle.draw(batch);
        res.shine.draw(batch);

        FontUtil.drawText(batch,font,text,getX(),getY(),0.7f, Color.WHITE,getWidth(), Align.center,false,getHeight());
    }
}
