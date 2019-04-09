package me.creese.palette.game.entity.bonus;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Array;

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
    private int deepRecursive;
    private BigPixel endPixel;
    private int speedDraw;


    public DoubleTapBonus(TexturePrepare prepare) {
        super(prepare);
        Sprite icon = prepare.getByName(FTextures.DOUBLE_TAP_ICON);
        icon.setScale(0.6f);
        setIcon(icon);
        setDescText("Двойное нажатие");


    }


    private void paintPixel(BigPixel bigPixel) {

        //if(bigPixel.getState().equals(BigPixel.State.PAINT)) return;

        if(!bigPixel.isVisible()) return;

        SquadPixel squad = bigPixel.getSquad();
        BigPixel[][] gridPixels = squad.getGridPixels();

        int xPlus = bigPixel.getPosX() + 1;
        int xMinus = bigPixel.getPosX() - 1;
        int yPlus = bigPixel.getPosY() + 1;
        int yMinus = bigPixel.getPosY() - 1;

        bigPixel.setState(BigPixel.State.PAINT);
        bigPixel.setBonusAdd(true);

      /*  addAction(Actions.run(() -> squad.redrawOnePixel(bigPixel.getPosX(), bigPixel.getPosY())));


        int yStart = yPlus;
        while (yStart < gridPixels.length ) {
            BigPixel pixel = gridPixels[yPlus][bigPixel.getPosX()];
            if(pixel.getState().equals(BigPixel.State.NOT_PAINT) && pixel.getNumColor() == startNumColor) {
                pixel.setState(BigPixel.State.PAINT);
                addAction(Actions.run(() -> squad.redrawOnePixel(pixel.getPosX(), pixel.getPosY())));
                yStart++;
            } else {

                break;

            }
        }
        yStart = yMinus;
        while (yStart >= 0) {
            BigPixel pixel = gridPixels[yStart][bigPixel.getPosX()];
            if(pixel.getState().equals(BigPixel.State.NOT_PAINT) && pixel.getNumColor() == startNumColor) {
                pixel.setState(BigPixel.State.PAINT);
                addAction(Actions.run(() -> squad.redrawOnePixel(pixel.getPosX(), pixel.getPosY())));
                yStart--;
            } else {

                break;
            }
        }*/


        if (getActions().size < 3000) {



                addAction(Actions.run(new Runnable() {
                    @Override
                    public void run() {

                        squad.redrawOnePixel(bigPixel.getPosX(), bigPixel.getPosY());
                        squad.getRoot().getTransitObject(ScoreView.class).iteratePixel();

                    }
                }));

        } else {
            //if (endPixel == null) {
            //endPixel = bigPixel;
            //}
            bigPixel.setState(BigPixel.State.NOT_PAINT);
            bigPixel.setBonusAdd(false);
            return;
        }


        if (xPlus < gridPixels[bigPixel.getPosY()].length) {
            BigPixel rightPixel = gridPixels[bigPixel.getPosY()][xPlus];
            if (rightPixel.getNumColor() == startNumColor && rightPixel.getState().equals(BigPixel.State.NOT_PAINT)) {
                paintPixel(rightPixel);

            }
        }
        if (xMinus >= 0) {
            BigPixel leftPixel = gridPixels[bigPixel.getPosY()][xMinus];
            if (leftPixel.getNumColor() == startNumColor && leftPixel.getState().equals(BigPixel.State.NOT_PAINT)) {
                paintPixel(leftPixel);

            }
        }
        if (yPlus < gridPixels.length) {
            BigPixel topPixel = gridPixels[yPlus][bigPixel.getPosX()];
            if (topPixel.getNumColor() == startNumColor && topPixel.getState().equals(BigPixel.State.NOT_PAINT)) {
                paintPixel(topPixel);

            }
        }
        if (yMinus >= 0) {
            BigPixel bottomPixel = gridPixels[yMinus][bigPixel.getPosX()];
            if (bottomPixel.getNumColor() == startNumColor && bottomPixel.getState().equals(BigPixel.State.NOT_PAINT)) {
                paintPixel(bottomPixel);

            }
        }


        /*if(yPlus < gridPixels.length && xPlus < gridPixels[bigPixel.getPosY()].length) {
            BigPixel topRightPixel = gridPixels[yPlus][xPlus];
            if(topRightPixel.getNumColor() == startNumColor) {
                paintPixel(topRightPixel);
            }
        }

        if(yMinus > 0 && xPlus < gridPixels[bigPixel.getPosY()].length) {
            BigPixel bottomRightPixel = gridPixels[yMinus][xPlus];
            if(bottomRightPixel.getNumColor() == startNumColor) {
                paintPixel(bottomRightPixel);
            }
        }

        if(yMinus > 0 && xMinus > 0) {
            BigPixel bottomLeftPixel = gridPixels[yMinus][xMinus];
            if(bottomLeftPixel.getNumColor() == startNumColor) {
                paintPixel(bottomLeftPixel);
            }
        }

        if(yPlus < gridPixels.length && xMinus > 0) {

            BigPixel topLeftPixel = gridPixels[yPlus][xMinus];
            if(topLeftPixel.getNumColor() == startNumColor) {
                paintPixel(topLeftPixel);
            }
        }*/
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

        startNumColor = bigPixel.getNumColor();


        paintPixel(bigPixel);

        speedDraw = 2;
        if (getActions().size > 500) speedDraw = 3;
        if (getActions().size > 1500) speedDraw = 5;
        if (getActions().size > 2500) speedDraw = 15;


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
       /*         if(endPixel != null) {
                    BigPixel tmp = endPixel;
                    endPixel = null;
                    paintPixel(tmp);
                } else*/
                deleteBonus();
            }
        } else super.act(delta);
    }
}
