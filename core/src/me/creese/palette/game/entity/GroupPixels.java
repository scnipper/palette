package me.creese.palette.game.entity;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Group;

import me.creese.palette.game.util.FTextures;
import me.creese.palette.game.util.TexturePrepare;
import me.creese.util.display.Display;

public class GroupPixels extends Group {


    private final Display root;
    private Sprite sprite;

    public GroupPixels(Display root) {
        this.root = root;
        sprite = root.getTransitObject(TexturePrepare.class).getByName(FTextures.PIXEL_SQUARE);

    }

    public Display getRoot() {
        return root;
    }

    public Sprite getSprite() {
        return sprite;
    }
}
