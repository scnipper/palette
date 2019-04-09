package me.creese.palette.game.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.utils.SnapshotArray;

import java.util.ArrayList;

import me.creese.palette.game.util.FTextures;
import me.creese.palette.game.util.TexturePrepare;
import me.creese.util.display.Display;

public class GroupPixels extends Group {


    private Sprite sprite;
    private int perFrame;
    private int idAction;

    public GroupPixels(Display root) {


        sprite = root.getTransitObject(TexturePrepare.class).getByName(FTextures.PIXEL_SQUARE);

    }

    /**
     * Добавление дополнительных пикселей с каждой стороны в режиме "Завеса тайны"
     */
    public int openMoreSecretPixels() {
        SnapshotArray<Actor> children = getChildren();
        if (children.size > 0 ) {
            ArrayList<BigPixel> addPixels = new ArrayList<>();
            SquadPixel squadPixel = (SquadPixel) children.get(0);

            BigPixel[][] gridPixels = squadPixel.getGridPixels();

            boolean isBreakY = false;


            for (int i = 0; i < gridPixels.length; i++) {
                if (isBreakY) {
                    break;
                }
                for (int j = 0; j < gridPixels[i].length; j++) {
                    BigPixel bigPixel = gridPixels[i][j];

                    if (bigPixel.isVisible()) {
                        int left = j - 1;
                        int right = j + 1;

                        int top = i - 1;
                        int bottom = i + 1;

                        if (left >= 0) {
                            BigPixel p = gridPixels[i][left];
                            if (!p.isVisible()) {
                                p.setVisible(true);
                                addPixels.add(p);

                            }
                        }
                        if (top >= 0) {
                            BigPixel p = gridPixels[top][j];
                            if (!p.isVisible()) {
                               p.setVisible(true);
                                addPixels.add(p);
                                if (left >= 0) {
                                    BigPixel p2 = gridPixels[top][left];
                                    if (!p2.isVisible()) {
                                        p2.setVisible(true);
                                        addPixels.add(p2);

                                    } else if (right < gridPixels[i].length) {
                                        BigPixel p3 = gridPixels[top][right];
                                        if (!p3.isVisible()) {
                                            p3.setVisible(true);
                                            addPixels.add(p3);

                                        }
                                    }
                                }
                            }

                        }

                        if (bottom < gridPixels.length) {
                            BigPixel p = gridPixels[bottom][j];
                            if (!p.isVisible()) {
                                p.setVisible(true);
                                addPixels.add(p);
                                isBreakY = true;

                                if (left >= 0) {
                                    BigPixel p2 = gridPixels[bottom][left];
                                    if (!p2.isVisible()) {
                                        p2.setVisible(true);
                                        addPixels.add(p2);
                                    } else if (right < gridPixels[i].length) {
                                        BigPixel p3 = gridPixels[bottom][right];
                                        if (!p3.isVisible()) {
                                            p3.setVisible(true);
                                            addPixels.add(p3);
                                        }
                                    }
                                }

                            }

                        }


                        if (right < gridPixels[i].length) {
                            BigPixel p = gridPixels[i][right];
                            if (!p.isVisible()) {
                                p.setVisible(true);
                                addPixels.add(p);
                                break;
                            }
                        }


                    }
                }
            }

            perFrame = addPixels.size()/30;

            if(perFrame <= 0) perFrame = 1;

            if(perFrame > 50) perFrame = 50;

            idAction+=1;
            int currIndex = idAction;
            RepAction forever = getForeverAction(currIndex,Actions.run(new Runnable() {
                int startIndex = 0;

                @Override
                public void run() {
                    for (int i = 0; i < perFrame; i++) {
                        int index = startIndex + i;
                        if (index < addPixels.size()) {
                            BigPixel p = addPixels.get(index);
                            p.setVisible(true);
                            p.getSquad().redrawOnePixel(p.getPosX(), p.getPosY());
                        }

                    }
                    startIndex += perFrame;


                    if (startIndex >= addPixels.size()) {
                        for (Action action : getActions()) {
                            RepAction a = (RepAction) action;
                            if(a.id == currIndex) {
                                a.finish();
                            }
                        }
                        addPixels.clear();
                    }
                }
            }));
            addAction(forever);
            return addPixels.size();
        }
        return 0;
    }

    private RepAction getForeverAction(int currIndex, Action repeatedAction) {
        RepAction action = new RepAction();
        action.id = currIndex;
        action.setCount(RepeatAction.FOREVER);
        action.setAction(repeatedAction);
        return action;
    }


    /**
     * Залить все пиксели цветом
     *
     * @param color
     */
    public void fillAllPixels(Color color) {
        SnapshotArray<Actor> children = getChildren();

        for (Actor child : children) {

            SquadPixel pixel = (SquadPixel) child;
            pixel.setFillColor(color);
            pixel.redrawAllSquad();

            pixel.setFillColor(null);

        }
    }

    public Sprite getSprite() {
        return sprite;
    }

    class RepAction extends RepeatAction {
        int id;
    }
}
