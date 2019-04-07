package me.creese.palette.game.entity.buttons;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Align;

import me.creese.palette.game.screens.Loading;
import me.creese.palette.game.util.FTextures;
import me.creese.palette.game.util.FontUtil;
import me.creese.palette.game.util.P;
import me.creese.palette.game.util.TexturePrepare;
import me.creese.util.display.Display;

public class StartMenuButton extends Actor {

    private final Sprite spriteBack;
    private final BitmapFont font;
    private final String text;

    public StartMenuButton(TexturePrepare prepare,String text) {
        this.text = text;




        spriteBack = prepare.getByName(FTextures.SCORE_BACK);

        setWidth(spriteBack.getWidth());
        setHeight(spriteBack.getHeight());
        font = P.get().asset.get(Loading.FONT_ROBOTO_BOLD, BitmapFont.class);


    }

    @Override
    protected void setParent(Group parent) {
        super.setParent(parent);
        if (parent != null) {
            setX(parent.getStage().getViewport().getWorldWidth()/2-spriteBack.getWidth()/2);
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        spriteBack.setPosition(getX(),getY());
        spriteBack.draw(batch);

        FontUtil.drawText(batch,font,text,getX(),getY(),0.6f,P.BLACK_FONT_COLOR,getWidth(), Align.center,false,getHeight());
    }
}
