package me.creese.palette.game.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFontCache;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.utils.Align;

import me.creese.palette.game.screens.Loading;
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
    private float xToCamera;
    private float yToCamera;
    private BitmapFontCache fontCache;

    public BigPixel(Sprite sprite, int numColor, Color color, int posX, int posY) {
        this.numColor = numColor;
        this.color = color;
        this.posX = posX;
        this.posY = posY;
        this.sprite = sprite;
        text = String.valueOf(numColor);

        setBounds(posX * WIDTH_PIXEL, (P.HEIGHT - HEIGHT_PIXEL) - posY * HEIGHT_PIXEL, WIDTH_PIXEL, HEIGHT_PIXEL);

        addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                ((GroupPixels) getParent()).getPixelsControl().setDownPixel(BigPixel.this);
                return false;
            }

        });
        font = P.get().asset.get(Loading.FONT_ROBOTO_BOLD, BitmapFont.class);


    }


    public void setPaint(boolean paint) {
        isPaint = paint;
        if (isPaint) {
            fontCache.clear();
        }
    }

    public void drawFont(BitmapFontCache fontCache) {
        this.fontCache = fontCache;
        if (!isPaint) {
            fontCache.addText(text, getX(), getY() + getHeight() / 2 + (font.getData().getFirstGlyph().height * 0.8f) / 2, getWidth(), Align.center, false);
        }

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        OrthographicCamera camera = (OrthographicCamera) getStage().getCamera();
        float zoom = camera.zoom;
        xToCamera = ((camera.position.x - (P.WIDTH * zoom) / 2) - getX()) * -1;
        yToCamera = (camera.position.y - (P.HEIGHT * zoom) / 2) - (getY() - (P.HEIGHT * zoom));
        if (xToCamera > -WIDTH_PIXEL && xToCamera < P.WIDTH * zoom
                && yToCamera > -HEIGHT_PIXEL && yToCamera < (P.HEIGHT + HEIGHT_PIXEL*2)*zoom) {
            sprite.setPosition(getX(), getY());
            if (!isPaint) {
                sprite.setColor(Color.WHITE);
                sprite.setScale(0.95f);
                sprite.draw(batch);
            } else {
                sprite.setColor(color);
                sprite.setScale(1);
                sprite.draw(batch);
            }
        }


    }

    @Override
    public String toString() {
        return "BigPixel{" + "numColor=" + numColor + ", color=" + color + ", posX=" + posX + ", posY=" + posY + '}';
    }
}
