package me.creese.palette.game.entity.bonus;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Array;

import java.util.LinkedList;

import me.creese.palette.game.entity.BigPixel;
import me.creese.palette.game.entity.GroupPixels;
import me.creese.palette.game.entity.PixelsControl;
import me.creese.palette.game.entity.ScoreView;
import me.creese.palette.game.entity.SquadPixel;
import me.creese.palette.game.util.FTextures;
import me.creese.palette.game.util.TexturePrepare;

public class DoubleTapBonus extends Bonus {


    private int startNumColor = -1;
    private boolean isStepActions;
    private int indexStepActions;
    private int speedDraw;
    private PixelsControl pixelsControl;


    public DoubleTapBonus(TexturePrepare prepare) {
        super(prepare);
        Sprite icon = prepare.getByName(FTextures.DOUBLE_TAP_ICON);
        icon.setScale(0.6f);
        setIcon(icon);
        setDescText("Двойное нажатие");


    }


    /**
     * Функция закрашивания пикселей
     *
     * @param bigPixel Начальный пиксель
     */
    private void paintPixel(BigPixel bigPixel) {


        if (!bigPixel.isVisible()) return;

        LinkedList<BigPixel> stack = new LinkedList<>();

        stack.push(bigPixel);
        BigPixel[][] gridPixels = bigPixel.getSquad().getGridPixels();

        boolean isFirst = true;
        while (stack.size() > 0) {


            BigPixel pixel = stack.pop();
            if (!isFirst) {
                if (pixel.getState().equals(BigPixel.State.PAINT)) continue;
            }
            isFirst = false;
            SquadPixel squad = pixel.getSquad();


            int xPlus = pixel.getPosX() + 1;
            int xMinus = pixel.getPosX() - 1;
            int yPlus = pixel.getPosY() + 1;
            int yMinus = pixel.getPosY() - 1;

            pixel.setState(BigPixel.State.PAINT);
            pixel.setBonusAdd(true);

            addAction(Actions.run(() -> {

                squad.redrawOnePixel(pixel.getPosX(), pixel.getPosY());
                squad.getRoot().getTransitObject(ScoreView.class).iteratePixel();

            }));


            if (xPlus < gridPixels[pixel.getPosY()].length) {
                BigPixel rightPixel = gridPixels[pixel.getPosY()][xPlus];
                if (rightPixel.getNumColor() == startNumColor && rightPixel.getState().equals(BigPixel.State.NOT_PAINT)) {
                    stack.push(rightPixel);

                }
            }
            if (xMinus >= 0) {
                BigPixel leftPixel = gridPixels[pixel.getPosY()][xMinus];
                if (leftPixel.getNumColor() == startNumColor && leftPixel.getState().equals(BigPixel.State.NOT_PAINT)) {
                    stack.push(leftPixel);

                }
            }
            if (yPlus < gridPixels.length) {
                BigPixel topPixel = gridPixels[yPlus][pixel.getPosX()];
                if (topPixel.getNumColor() == startNumColor && topPixel.getState().equals(BigPixel.State.NOT_PAINT)) {
                    stack.push(topPixel);

                }
            }
            if (yMinus >= 0) {
                BigPixel bottomPixel = gridPixels[yMinus][pixel.getPosX()];
                if (bottomPixel.getNumColor() == startNumColor && bottomPixel.getState().equals(BigPixel.State.NOT_PAINT)) {
                    stack.push(bottomPixel);

                }
            }

        }
    }

    @Override
    public void upFinger(GroupPixels groupPixels, PixelsControl pixelsControl, BigPixel bigPixel) {

    }

    @Override
    public boolean panFinger(GroupPixels groupPixels, PixelsControl pixelsControl, BigPixel bigPixel) {
        return true;
    }


    @Override
    public void doubleTapFinger(GroupPixels groupPixels, PixelsControl pixelsControl, BigPixel bigPixel) {

        if (bigPixel.getState().equals(BigPixel.State.WRONG_PAINT) || !bigPixel.isVisible()) return;

        this.pixelsControl = pixelsControl;
        this.pixelsControl.setLock(true);
        startNumColor = bigPixel.getNumColor();


        paintPixel(bigPixel);

        speedDraw = 2;
        if (getActions().size > 500) speedDraw = 5;
        if (getActions().size > 1500) speedDraw = 10;
        if (getActions().size > 2500) speedDraw = 30;
        if (getActions().size > 5000) speedDraw = 50;


        bigPixel.getSquad().getRoot().getTransitObject(ScoreView.class).decrementScore(1);
        isStepActions = true;

    }

    @Override
    public void act(float delta) {
        if (isStepActions) {
            Array<Action> actions = getActions();
            if (actions.size > 0) {

                for (int i = 0; i < speedDraw; i++) {
                    int next = indexStepActions + i;
                    if (next < actions.size) {
                        Action action = actions.get(next);
                        if (action.act(delta) && next < actions.size) {
                            Action current = actions.get(next);
                            int actionIndex = current == action ? next : actions.indexOf(action, true);
                            if (actionIndex != -1) {
                                actions.removeIndex(actionIndex);
                                action.setActor(null);
                                indexStepActions--;
                            }
                            indexStepActions++;
                        }
                    }
                }


            }
            if (actions.size == 0) {
                pixelsControl.setLock(false);
                deleteBonus();
            }
        } else super.act(delta);
    }
}
