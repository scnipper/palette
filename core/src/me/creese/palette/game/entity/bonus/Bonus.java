package me.creese.palette.game.entity.bonus;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.utils.Align;

import me.creese.palette.game.entity.BigPixel;
import me.creese.palette.game.entity.GroupPixels;
import me.creese.palette.game.entity.PixelsControl;
import me.creese.palette.game.screens.Loading;
import me.creese.palette.game.util.FTextures;
import me.creese.palette.game.util.FontUtil;
import me.creese.palette.game.util.P;
import me.creese.palette.game.util.TexturePrepare;

/**
 * Абстрактный класс бонусов
 */
public abstract class Bonus extends Actor {

    private final Sprite circle;
    private final Sprite circleShadow;
    private final BitmapFont font;
    private Sprite icon;
    private boolean isDrawCircle;
    // текст описания
    private String descText;
    private boolean isDrawText;
    private float fontScale = 0.6f;

    public Bonus(TexturePrepare prepare) {

        circle = prepare.getByName(FTextures.CIRCLE_BONUS);
        circleShadow = prepare.getByName(FTextures.CIRCLE_BONUS_SHADOW);
        Color black = Color.BLACK.cpy();
        black.a = 0.4f;
        circleShadow.setColor(black);
        setWidth(circle.getWidth());
        setHeight(circle.getHeight());
        font = P.get().asset.get(Loading.FONT_ROBOTO_BOLD, BitmapFont.class);

        addListener(new ActorGestureListener() {
            @Override
            public void touchDown(InputEvent event, float x, float y, int pointer, int button) {
                BonusGroup parent = (BonusGroup) getParent();
                Bonus activateBonus = parent.getActivateBonus();
                if (getColor().equals(P.ACTIVATE_BONUS_COLOR)) {
                    parent.setActivateBonus(null);
                    setColor(Color.WHITE);
                } else {
                    if (activateBonus != null) {
                        activateBonus.setColor(Color.WHITE);
                    }

                    parent.setActivateBonus(Bonus.this);

                    setColor(P.ACTIVATE_BONUS_COLOR);
                }
            }
        });


    }

    /**
     * Удаляем бонус
     */
    public void deleteBonus() {
        BonusGroup parent = (BonusGroup) getParent();
        if (parent != null) {
            parent.setActivateBonus(null);
            remove();
            parent.updatePosBonus();
        }

    }

    public void setFontScale(float fontScale) {
        this.fontScale = fontScale;
    }

    public void setDrawCircle(boolean drawCircle) {
        isDrawCircle = drawCircle;
    }

    public void setDescText(String descText) {
        this.descText = descText;
    }

    public void setDrawText(boolean drawText) {
        isDrawText = drawText;
    }

    /**
     * Иконка для бонуса
     * @param icon
     */
    public void setIcon(Sprite icon) {
        this.icon = icon;
        icon.setColor(P.BLACK_FONT_COLOR);
        icon.flip(false, true);
    }

    /**
     * Палец поднят с пикселя
     * @param groupPixels
     * @param pixelsControl
     * @param bigPixel
     */
    public abstract void upFinger(GroupPixels groupPixels, PixelsControl pixelsControl, BigPixel bigPixel);

    /**
     * Палец перемещается по пикселю
     * @param groupPixels
     * @param pixelsControl
     * @param bigPixel
     * @return
     */
    public abstract boolean panFinger(GroupPixels groupPixels, PixelsControl pixelsControl, BigPixel bigPixel);

    /**
     * Двойное нажатие на пиксель
     * @param groupPixels
     * @param pixelsControl
     * @param bigPixel
     */
    public abstract void doubleTapFinger(GroupPixels groupPixels, PixelsControl pixelsControl, BigPixel bigPixel);


    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (isDrawCircle) {
            circleShadow.setPosition(getX() + 10, getY() - 20);
            circleShadow.draw(batch);
            circle.setColor(getColor());
            circle.setPosition(getX(), getY());
            circle.draw(batch);
            circle.setColor(Color.WHITE);
        }
        if (isDrawText) {
            FontUtil.drawText(batch, font, descText, getX() - getWidth() / 2, getY() + getHeight() + 100, fontScale, P.BLACK_FONT_COLOR, getWidth() * 2, Align.center, true);
        }

        icon.setPosition(getX() + getWidth() / 2 - icon.getWidth() / 2, getY() + getHeight() / 2 - icon.getHeight() / 2);
        icon.draw(batch);
    }
}
