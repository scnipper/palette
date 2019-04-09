package me.creese.palette.game.entity.bonus;

import com.badlogic.gdx.graphics.Color;

import me.creese.palette.game.entity.BigPixel;
import me.creese.palette.game.entity.GroupPixels;
import me.creese.palette.game.entity.PixelsControl;
import me.creese.palette.game.entity.ScoreView;
import me.creese.palette.game.entity.SquadPixel;
import me.creese.palette.game.util.FTextures;
import me.creese.palette.game.util.TexturePrepare;

public class BombBonus extends Bonus {
    public BombBonus(TexturePrepare prepare) {
        super(prepare);
        setIcon(prepare.getByName(FTextures.BOMB_ICON));

        setDescText("Закрась область 10х10");
        setFontScale(0.45f);
    }

    @Override
    public void upFinger(GroupPixels groupPixels, PixelsControl pixelsControl, BigPixel bigPixel) {

        if (!bigPixel.isVisible()) return;
        System.out.println("activate bomb bonus");
        Color pixelColor = bigPixel.getColor().cpy();
        pixelColor.a = 0.5f;
        SquadPixel squad = bigPixel.getSquad();
        BigPixel[][] gridPixels = squad.getGridPixels();
        ScoreView scoreView = squad.getRoot().getTransitObject(ScoreView.class);


        for (int i = bigPixel.getPosX() - 5; i < bigPixel.getPosX() + 5; i++) {
            for (int j = bigPixel.getPosY() - 5; j < bigPixel.getPosY() + 5; j++) {
                if (j < gridPixels.length && j >= 0) {
                    if (i < gridPixels[j].length && i >= 0) {
                        BigPixel pixel = gridPixels[j][i];


                        if (!pixel.getState().equals(BigPixel.State.PAINT) && pixel.isVisible()) {
                            pixel.setBonusAdd(true);
                            pixel.setState(BigPixel.State.PAINT);
                            scoreView.iteratePixel();
                        }

                        pixel.getSquad().redrawOnePixel(i, j);
                    }
                }
            }

        }
        deleteBonus();


    }

    @Override
    public boolean panFinger(GroupPixels groupPixels, PixelsControl pixelsControl, BigPixel bigPixel) {
        return true;
    }


    @Override
    public void doubleTapFinger(GroupPixels groupPixels, PixelsControl pixelsControl, BigPixel bigPixel) {

    }
}
