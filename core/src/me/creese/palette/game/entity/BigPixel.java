package me.creese.palette.game.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFontCache;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.utils.Align;

import me.creese.palette.game.screens.Loading;
import me.creese.palette.game.util.FontUtil;
import me.creese.palette.game.util.P;

public class BigPixel extends Actor {
    public static final int WIDTH_PIXEL = 128;
    public static final int HEIGHT_PIXEL = 128;
    private final int numColor;
    private final Color color;
    private final int posX;
    private final int posY;
    private final Sprite sprite;
    private final String text;
    private final BitmapFont font;


    private boolean isPaint;
    private BitmapFontCache fontCache;
    private float xToCamera;
    private float yToCamera;

    public BigPixel(Sprite sprite, int numColor, Color color, int posX, int posY) {
        this.numColor = numColor;
        this.color = color;
        this.posX = posX;
        this.posY = posY;
        this.sprite = sprite;
        text = String.valueOf(numColor);

        setBounds(posX * WIDTH_PIXEL, P.HEIGHT - posY * HEIGHT_PIXEL, WIDTH_PIXEL, HEIGHT_PIXEL);

        addListener(new ActorGestureListener(){
            @Override
            public void touchDown(InputEvent event, float x, float y, int pointer, int button) {
                isPaint = true;
                sprite.setColor(color);
                sprite.setScale(1);
            }
        });
        font = P.get().asset.get(Loading.FONT_ROBOTO_BOLD, BitmapFont.class);


    }


    public void drawFont(Batch batch) {
        if(xToCamera > -WIDTH_PIXEL && xToCamera < P.WIDTH && yToCamera > -HEIGHT_PIXEL && yToCamera < P.HEIGHT+HEIGHT_PIXEL)
        FontUtil.drawText(batch,font,text,getX(),getY(),0.8f,P.GRAY_FONT_COLOR,getWidth(), Align.center,false,getHeight());
    }
    @Override
    public void draw(Batch batch, float parentAlpha) {
        xToCamera = ((getStage().getCamera().position.x - P.WIDTH / 2) - getX())*-1;
        yToCamera = (getStage().getCamera().position.y - P.HEIGHT / 2) - (getY()-P.HEIGHT);
        if(xToCamera > -WIDTH_PIXEL && xToCamera < P.WIDTH && yToCamera > -HEIGHT_PIXEL && yToCamera < P.HEIGHT+HEIGHT_PIXEL) {
            sprite.setPosition(getX(), getY());
            if (!isPaint) {
                /*sprite.setColor(P.BACKGROUND_COLOR);
                sprite.setScale(1);
                sprite.draw(batch);*/

                sprite.setColor(Color.WHITE);
                sprite.setScale(0.95f);
                sprite.draw(batch);
                //FontUtil.drawText(batch,font,text,getX(),getY(),0.8f,P.GRAY_FONT_COLOR,getWidth(), Align.center,false,getHeight());
            } else {
                sprite.draw(batch);
            }
        }



    }

    @Override
    public String toString() {
        return "BigPixel{" + "numColor=" + numColor + ", color=" + color + ", posX=" + posX + ", posY=" + posY + '}';
    }
}
