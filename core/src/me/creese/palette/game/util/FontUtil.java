package me.creese.palette.game.util;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class FontUtil {
    public static void drawText(Batch batch, BitmapFont font, String text, float x, float y) { {
        drawText(batch,font,text,x,y,1, Color.WHITE);
    }}
    public static void drawText(Batch batch, BitmapFont font, String text, float x, float y, float scale, Color color) {
        drawText(batch,font,text,x,y,scale,color,0,0,false);
    }
    public static void drawText(Batch batch, BitmapFont font, String text, float x, float y, float scale, Color color,
                                float targetWidth, int align) {
        drawText(batch,font,text,x,y,scale,color,targetWidth,align,false);
    }
    public static void drawText(Batch batch, BitmapFont font, String text, float x, float y, float scale, Color color,
                                float targetWidth, int align, boolean isWrap) {
        drawText(batch,font,text,x,y,scale,color,targetWidth,align,isWrap,0);
    }
    public static void drawText(Batch batch, BitmapFont font, String text, float x, float y, float scale, Color color,
                                float targetWidth, int align, boolean isWrap, float targetHeight) {
        font.getData().setScale(scale);
        font.setColor(color);
        if(targetHeight > 0) {
            if(targetWidth > 0)
            font.draw(batch, text, x, y + targetHeight / 2 + (font.getData().getFirstGlyph().height * scale) / 2, targetWidth, align, isWrap);
            else
            font.draw(batch, text, x, y + targetHeight / 2 + (font.getData().getFirstGlyph().height * scale) / 2);
        }
        else {
            if(targetWidth > 0)
            font.draw(batch, text, x, y, targetWidth, align, isWrap);
            else
            font.draw(batch, text, x, y);
        }
        font.setColor(Color.WHITE);
        font.getData().setScale(1);
    }
}
