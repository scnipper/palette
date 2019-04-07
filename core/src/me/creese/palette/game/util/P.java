package me.creese.palette.game.util;

import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class P {
    public static final float WIDTH = 1080;
    public static final float HEIGHT = 1920;

    public static final int MAX_PALETTE_SIZE = 256;

    public static final int MAX_IMAGE_WIDTH = 300;
    public static final int MAX_IMAGE_HEIGHT = 300;

    public static final Color BACKGROUND_COLOR = new Color(0xdbdbdbff);
    public static final Color GRAY_FONT_COLOR = new Color(0x969696ff);
    public static final Color BLACK_FONT_COLOR = new Color(0x2e2e2eff);
    public static final Color ACTIVATE_BONUS_COLOR = new Color(0xfdd837ff);


    public static final int COUNT_IMAGES = 11;
    public static final float START_ZOOM = 0.4f;
    private static P instance;
    public AssetManager asset;
    public SpriteBatch rootBatch;
    public Preferences saves;

    public static P get() {
        if(instance == null) {
            instance = new P();
        }
        return instance;
    }
}
