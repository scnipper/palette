package me.creese.palette.game.entity.buttons;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;

import me.creese.palette.game.util.FTextures;
import me.creese.palette.game.util.P;
import me.creese.palette.game.util.TexturePrepare;

public class ResForPaletteButtons {
    public final Sprite circle;
    public final Sprite strokeCircle;
    public final Sprite shine;

    public ResForPaletteButtons(TexturePrepare prepare) {
        circle = prepare.getByName(FTextures.CIRCLE_PALETTE);

        strokeCircle = prepare.getByName(FTextures.ARC_PALETTE_BUTTON);
        strokeCircle.setColor(P.BLACK_FONT_COLOR);

        Color color = Color.WHITE.cpy();

        color.a = 0.4f;
        shine = prepare.getByName(FTextures.SMALL_ARC_PALETTE);
        shine.setColor(color);
    }
}
