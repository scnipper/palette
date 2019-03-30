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
    private boolean isSelect;

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

                    isSelect = true;


                    PaletteButton selectButton = parent.getSelectButton();

                    if (selectButton != null) {
                        selectButton.setSelect(false);
                    }
                    parent.setSelectButton(PaletteButton.this);

                }


            }
        });
        font = P.get().asset.get(Loading.FONT_ROBOTO_BOLD, BitmapFont.class);


    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {

        res.circle.setPosition(getX(), getY());
        res.strokeCircle.setPosition(getX(), getY());
        res.shine.setPosition(getX() + getWidth() / 2 - res.shine.getWidth() / 2, getY() + getHeight() / 2 - res.shine.getHeight() / 2);
        res.circle.setColor(getColor());
        res.circle.draw(batch);
        res.strokeCircle.draw(batch);



        if (isSelect) {
            res.blob.setColor(getColor());
            res.blob.setPosition(getX() + 5, getY() + 121);
            res.blob.draw(batch);

            res.blobStroke.setPosition(getX() + 5, getY() + 121);
            res.blobStroke.draw(batch);

            res.smallBlob.setPosition(getX()+getWidth()/2-res.smallBlob.getWidth()/2,getY()+150);
            res.smallBlob.draw(batch);

/*            batch.end();
            shape.setProjMatrix(batch.getProjectionMatrix());
            float bX = getX()+55;
            float bY = getY()+200;
            shape.setColor(getColor());
            short vert1 = shape.vertexAdd(bX,bY,shape.getColor());
            short vert2 = shape.vertexAdd(bX+170,bY,shape.getColor());
            short vert3 = shape.vertexAdd(bX+85,bY+120+(38 *getScaleX()),shape.getColor());
            shape.indicesAdd(vert1,vert2,vert3);
            shape.flush();
            batch.begin();*/
        } else {
            res.shine.draw(batch);
        }


        FontUtil.drawText(batch, font, text, getX(), getY(), 0.7f, isSelect ? P.BLACK_FONT_COLOR :Color.WHITE, getWidth(), Align.center, false, getHeight());
    }
}
