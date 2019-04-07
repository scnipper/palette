package me.creese.palette.game.entity.bonus;

import me.creese.palette.game.entity.BigPixel;
import me.creese.palette.game.entity.GroupPixels;
import me.creese.palette.game.entity.PixelsControl;
import me.creese.palette.game.util.FTextures;
import me.creese.palette.game.util.TexturePrepare;

public class HoldBonus extends Bonus {
    public HoldBonus(TexturePrepare prepare) {
        super(prepare);

        setIcon(prepare.getByName(FTextures.HOLD_ICON));
        setDescText("Нажми и удерживай");
    }

    @Override
    public void upFinger(GroupPixels groupPixels, PixelsControl pixelsControl, BigPixel bigPixel) {

    }

    @Override
    public boolean panFinger(GroupPixels groupPixels, PixelsControl pixelsControl, BigPixel bigPixel) {
        System.out.println("pan pixel "+bigPixel);
        return false;
    }


    @Override
    public void doubleTapFinger(GroupPixels groupPixels, PixelsControl pixelsControl, BigPixel bigPixel) {

    }
}
