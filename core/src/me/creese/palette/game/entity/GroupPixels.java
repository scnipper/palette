package me.creese.palette.game.entity;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.SnapshotArray;

public class GroupPixels extends Group {

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        SnapshotArray<Actor> children = getChildren();

        for (int i = 0; i < children.size; i++) {
            BigPixel actor = (BigPixel) children.get(i);
            actor.drawFont(batch);
        }
    }
}
