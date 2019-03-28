package me.creese.palette.game.util;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;

public class P {
    public static final float WIDTH = 1080;
    public static final float HEIGHT = 1920;

    public static final Color BACKGROUND_COLOR = new Color(0xdbdbdbff);
    public static final Color GRAY_FONT_COLOR = new Color(0x969696ff);
    private static P instance;
    public AssetManager asset;

    public static P get() {
        if(instance == null) {
            instance = new P();
        }
        return instance;
    }
}
