package me.creese.palette.game.util;

import com.badlogic.gdx.Preferences;

import me.creese.palette.game.entity.buttons.SelectImageBtn;

/**
 * Коды для сохранения в {@link Preferences}
 */
public class S {
    /**
     * Код картинки для сохранения состояния {@link SelectImageBtn}
     */
    public static final String IMG              = "img_";
    /**
     * Количество сессий или запусков
     */
    public static final String COUNT_LAUNCH     = "c_launch";
    /**
     * Количество закрашенных пикселей
     */
    public static final String PIXELS_PAINT     = "paint_pixels";
    /**
     * Количество ошибчных пикселей
     */
    public static final String WRONG_PIXELS_PAINT     = "wrong_paint_pixels";
    /**
     * Все время проведенное за закрашиванием пикселей
     */
    public static final String ALL_TIME         = "all_time";
}
