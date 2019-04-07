package me.creese.palette.game.entity.buttons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;

import me.creese.palette.game.screens.GameScreen;
import me.creese.palette.game.util.FTextures;
import me.creese.palette.game.util.MaxPaletteException;
import me.creese.palette.game.util.P;
import me.creese.palette.game.util.S;
import me.creese.palette.game.util.TexturePrepare;
import me.creese.util.display.Display;

public class SelectImageBtn extends Actor implements SelectImpl {

    public static final int LOCK = 0;
    public static final int UNLOCK = 1;
    public static final int OPEN = 2;

    private final Display root;
    private final Sprite back;
    private final Sprite closedIcon;
    private final int numImage;
    private final String pathTexture;
    private Sprite texture;
    private int state;

    public SelectImageBtn(Display root, int numImage, String pathTexture) {
        this.pathTexture = pathTexture;
        this.numImage = numImage;
        this.root = root;

        TexturePrepare prepare = root.getTransitObject(TexturePrepare.class);
        back = prepare.getByName(FTextures.SELECT_IMAGE);
        closedIcon = prepare.getByName(FTextures.CLOSED_ICON);
        closedIcon.setColor(Color.RED);
        loadTexture();


        setWidth(back.getWidth());
        setHeight(back.getHeight());

        Color color = P.BLACK_FONT_COLOR.cpy();
        color.a = 0.8f;
        setColor(color);


        int saveState = P.get().saves.getInteger(S.IMG + numImage);

        setState(saveState);
    }

    @Override
    public void selectImage() {
        if (state == OPEN || state ==UNLOCK) {


            root.showGameView(GameScreen.class);
            try {
                root.getGameViewForName(GameScreen.class).startGame(texture.getTexture(),numImage);
            } catch (MaxPaletteException maxPaletteEcxeption) {
                maxPaletteEcxeption.printStackTrace();
            }
            //((SelectImageMenu) getParent()).freeTexturesExcept(numImage);
        }
    }

    public void loadTexture() {
/*        if (texture == null) {
            texture = new Sprite();
        }*/
        if (pathTexture == null) {
            texture = new Sprite(new Texture("images/image_" + numImage + ".gif"));
        } else texture = new Sprite(new Texture(Gdx.files.absolute(pathTexture)));
        texture.setOrigin(0, 0);
        if (texture.getWidth() >= texture.getHeight()) {
            texture.setScale(back.getWidth() / texture.getWidth());
        } else {
            texture.setScale((back.getHeight() - 50) / texture.getHeight());
        }
    }

    public void setState(int state) {
        this.state = state;
        switch (state) {
            case LOCK:
                back.setColor(getColor());
                break;
            case OPEN:
                back.setColor(Color.WHITE);
        }
    }

    public Sprite getTexture() {
        return texture;
    }

    public int getNumImage() {
        return numImage;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        back.setPosition(getX(), getY());
        back.draw(batch);
        switch (state) {
            case LOCK:
                closedIcon.setPosition(getX() + getWidth() / 2 - closedIcon.getWidth() / 2, getY() + getHeight() / 2 - closedIcon.getHeight() / 2);
                closedIcon.draw(batch);
                break;
            case OPEN:
                texture.setPosition(getX() + getWidth() / 2 - (texture.getWidth() * texture.getScaleX()) / 2, getY() + getHeight() / 2 - (texture.getHeight() * texture.getScaleY()) / 2);
                texture.draw(batch);
                break;
/*            case UNLOCK:
                texture.setPosition(getX() + getWidth() / 2 - (texture.getWidth() * texture.getScaleX()) / 2, getY() + getHeight() / 2 - (texture.getHeight() * texture.getScaleY()) / 2);
                texture.draw(batch);
                back.setColor(getColor());
                back.draw(batch);
                back.setColor(Color.WHITE);*/

        }


    }

}
