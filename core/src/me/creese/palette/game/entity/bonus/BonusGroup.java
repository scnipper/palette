package me.creese.palette.game.entity.bonus;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.SnapshotArray;

import java.util.Random;

import me.creese.palette.game.util.TexturePrepare;

public class BonusGroup extends Group {

    public static final int COUNT_BONUS = 2;
    private final TexturePrepare prepare;
    private final Random random;
    private Bonus activateBonus;
    private boolean isLock;

    public BonusGroup(TexturePrepare prepare) {
        random = new Random();
        this.prepare = prepare;
    }

    public void addRandomBonus(float x, float y) {

        if (getChildren().size == 5 || isLock) return;

        if(random.nextInt(5) == 0) {

            Bonus bonus = getRandomBonus(random.nextInt(COUNT_BONUS));
            if (bonus == null) {
                return;
            }

            bonus.setPosition(x, y);


            bonus.setTouchable(Touchable.disabled);
            addActor(bonus);

            bonus.setDrawText(true);
            bonus.addAction(Actions.sequence(Actions.delay(0.3f), Actions.moveTo(30, getYPosBonus(getChildren().size, bonus), 0.3f), Actions.run(() -> {
                bonus.setDrawCircle(true);
                bonus.setDrawText(false);
                bonus.setTouchable(Touchable.enabled);
            })));
        }

    }

    public void updatePosBonus() {
        SnapshotArray<Actor> children = getChildren();
        for (int i = 0; i < children.size; i++) {
            Bonus bonus = (Bonus) children.get(i);

            float yPosBonus = getYPosBonus(i + 1, bonus);

            if (yPosBonus != bonus.getY()) {
                bonus.addAction(Actions.moveTo(bonus.getX(), yPosBonus, 0.5f));
            }
        }

    }

    public void setLock(boolean lock) {
        isLock = lock;
    }

    private float getYPosBonus(int childNum, Bonus bonus) {
        return 300 + (childNum * (bonus.getHeight() + 20));
    }

    private Bonus getRandomBonus(int num) {
        switch (num) {
            case 0:
                return new DoubleTapBonus(prepare);
            case 1:
                return new BombBonus(prepare);
            case 2:
                return new HoldBonus(prepare);

            default:
                return null;
        }
    }

    public Bonus getActivateBonus() {
        return activateBonus;
    }

    public void setActivateBonus(Bonus activateBonus) {
        this.activateBonus = activateBonus;
    }
}
