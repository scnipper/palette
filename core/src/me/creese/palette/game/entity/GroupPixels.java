package me.creese.palette.game.entity;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Group;

import me.creese.palette.game.util.FTextures;
import me.creese.palette.game.util.TexturePrepare;

public class GroupPixels extends Group {

    private final PixelsControl pixelsControl;

    private final Sprite sprite;


    public GroupPixels(PixelsControl pixelsControl, TexturePrepare prepare) {
        this.pixelsControl = pixelsControl;


        this.sprite = prepare.getByName(FTextures.PIXEL_SQUARE);


    }

    public PixelsControl getPixelsControl() {
        return pixelsControl;
    }

    public Sprite getSprite() {
        return sprite;
    }


}
