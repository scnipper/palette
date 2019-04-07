package me.creese.palette.game.entity.buttons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;

import me.creese.palette.game.entity.SelectImageMenu;
import me.creese.palette.game.screens.GameScreen;
import me.creese.palette.game.screens.MainScreen;
import me.creese.palette.game.util.AdUtil;
import me.creese.palette.game.util.FTextures;
import me.creese.palette.game.util.MaxPaletteException;
import me.creese.palette.game.util.P;
import me.creese.palette.game.util.TexturePrepare;
import me.creese.util.display.Display;

public class LoadImageBtn extends Actor implements SelectImpl{
    private final Sprite back;
    private final Sprite plusIcon;
    private final Display root;

    public LoadImageBtn(Display root) {
        this.root = root;
        TexturePrepare prepare = root.getTransitObject(TexturePrepare.class);
        back = prepare.getByName(FTextures.SELECT_IMAGE);
        plusIcon = prepare.getByName(FTextures.PLUS_ICON);
        plusIcon.setColor(P.BLACK_FONT_COLOR);
        setWidth(back.getWidth());
        setHeight(back.getHeight());

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {

        back.setPosition(getX(),getY());
        plusIcon.setPosition(getX()+getWidth()/2-plusIcon.getWidth()/2,getY()+getHeight()/2-plusIcon.getHeight()/2);


        back.draw(batch);
        plusIcon.draw(batch);
    }

    @Override
    public void selectImage() {
        root.getTransitObject(AdUtil.class).requestImagePath(path -> {

            if(path != null) {

                Texture texture = new Texture(Gdx.files.absolute(path.getAbsolutePath()));
                root.showGameView(GameScreen.class);
                try {
                    root.getGameViewForName(GameScreen.class).startGame(texture, -1);

                } catch (MaxPaletteException maxPaletteEcxeption) {
                    root.getTransitObject(AdUtil.class).showToast("Превышен максимальный размер палитры > "+P.MAX_PALETTE_SIZE);

                    root.showGameView(MainScreen.class);
                    texture.dispose();
                    path.delete();
                    return;
                }

                SelectImageMenu parent = (SelectImageMenu) getParent();
                parent.clearChildren();
                parent.freeTexturesExcept(-1);
            }
        });
    }
}
