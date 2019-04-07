package me.creese.palette.game.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.SnapshotArray;

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
    public void fillAllPixels(Color color) {
        SnapshotArray<Actor> children = getChildren();

        for (Actor child : children) {

            SquadPixel pixel = (SquadPixel) child;
            pixel.setFillColor(color);
            pixel.redrawAllSquad();

            pixel.setFillColor(null);

        }
    }

    public Display getRoot() {
        return root;
    }

    public Sprite getSprite() {
        return sprite;
    }
}
