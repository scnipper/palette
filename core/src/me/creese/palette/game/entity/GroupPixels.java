package me.creese.palette.game.entity;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFontCache;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.SnapshotArray;

import me.creese.palette.game.screens.Loading;
import me.creese.palette.game.util.P;

public class GroupPixels extends Group {

    private final PixelsControl pixelsControl;
    private final BitmapFont font;
    private BitmapFontCache bitmapFontCache;

    public GroupPixels(PixelsControl pixelsControl) {
        this.pixelsControl = pixelsControl;

        font = P.get().asset.get(Loading.FONT_ROBOTO_BOLD, BitmapFont.class);
        font.getData().setScale(0.8f);
        bitmapFontCache = new BitmapFontCache(font);

        bitmapFontCache.setColor(P.GRAY_FONT_COLOR);

    }

    public PixelsControl getPixelsControl() {
        return pixelsControl;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        SnapshotArray<Actor> children = getChildren();

        if (bitmapFontCache.getLayouts().size == 0) {

            for (int i = 0; i < children.size; i++) {
                BigPixel actor = (BigPixel) children.get(i);
                actor.drawFont(bitmapFontCache);
            }
        }


        bitmapFontCache.draw(batch);
    }
}
