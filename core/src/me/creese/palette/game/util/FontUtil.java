package me.creese.palette.game.util;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;

public class FontUtil {
    public static GlyphLayout drawText(Batch batch, BitmapFont font, String text, float x, float y) { {
        return drawText(batch,font,text,x,y,1, Color.WHITE);
    }}
    public static GlyphLayout drawText(Batch batch, BitmapFont font, String text, float x, float y, float scale, Color color) {
        return drawText(batch,font,text,x,y,scale,color,0,0,false);
    }
    public static GlyphLayout drawText(Batch batch, BitmapFont font, String text, float x, float y, float scale, Color color,
                                float targetWidth, int align) {
        return drawText(batch,font,text,x,y,scale,color,targetWidth,align,false);
    }
    public static GlyphLayout drawText(Batch batch, BitmapFont font, String text, float x, float y, float scale, Color color,
                                float targetWidth, int align, boolean isWrap) {
        return drawText(batch,font,text,x,y,scale,color,targetWidth,align,isWrap,0);
    }
    public static GlyphLayout drawText(Batch batch, BitmapFont font, String text, float x, float y, float scale, Color color,
                                       float targetWidth, int align, boolean isWrap, float targetHeight) {
        font.getData().setScale(scale);
        font.setColor(color);
        GlyphLayout glyphLayout;
        if(targetHeight > 0) {
            if(targetWidth > 0)
                glyphLayout = font.draw(batch, text, x, y + targetHeight / 2 + (font.getData().getFirstGlyph().height * scale) / 2, targetWidth, align, isWrap);
            else
                glyphLayout = font.draw(batch, text, x, y + targetHeight / 2 + (font.getData().getFirstGlyph().height * scale) / 2);
        }
        else {
            if(targetWidth > 0)
                glyphLayout = font.draw(batch, text, x, y, targetWidth, align, isWrap);
            else
                glyphLayout = font.draw(batch, text, x, y);
        }
        font.setColor(Color.WHITE);
        font.getData().setScale(1);
        return glyphLayout;
    }
}
