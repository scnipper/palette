package me.creese.palette.game.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.GLOnlyTextureData;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.utils.Align;

import me.creese.palette.game.entity.buttons.PaletteButton;
import me.creese.palette.game.screens.Loading;
import me.creese.palette.game.screens.MainScreen;
import me.creese.palette.game.util.FontUtil;
import me.creese.palette.game.util.P;
import me.creese.util.display.Display;

public class SquadPixel extends Actor {

    public static final int WIDTH_SQUAD = 32;
    public static final int HEIGHT_SQUAD = 32;
    private static final float FONT_SCALE = 0.35f;
    private final OrthographicCamera camera;
    private final BigPixel[][] gridPixels;
    private final int arrX;
    private final int arrY;
    private final BitmapFont font;
    private final Display root;
    private FrameBuffer frameBuffer;
    private Sprite sprite;
    private TextureRegion bufferTexture;
    private float downX;
    private float downY;
    private OrthographicCamera stageCamera;
    private boolean isRedrawAll;

    public SquadPixel(Display root, BigPixel[][] gridPixels, int arrX, int arrY) {
        this.root = root;
        this.gridPixels = gridPixels;
        this.arrX = arrX;
        this.arrY = arrY;
        setBounds(arrX * BigPixel.WIDTH_PIXEL, P.HEIGHT - arrY * BigPixel.HEIGHT_PIXEL, BigPixel.WIDTH_PIXEL * WIDTH_SQUAD, BigPixel.HEIGHT_PIXEL * HEIGHT_SQUAD);

        moveBy(0, -getHeight());
        setDebug(true);

        /*Gdx.app.postRunnable(() ->*/ frameBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, (int) getWidth(), (int) getHeight(), false) {
            @Override
            protected Texture createTexture(FrameBufferTextureAttachmentSpec attachmentSpec) {

                GLOnlyTextureData data = new GLOnlyTextureData(getWidth(), getHeight(), 0, GL20.GL_RGBA, GL20.GL_RGBA, GL20.GL_UNSIGNED_BYTE);

                return new Texture(data);
            }
        };
       // });

        camera = new OrthographicCamera(getWidth(), getHeight());
        camera.translate(getWidth() / 2, getHeight() / 2);
        camera.update();

        addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                root.getGameViewForName(MainScreen.class).getPixelsControl().setDownPixelSquad(SquadPixel.this);
                downX = x;
                downY = y;
                return false;
            }


        });

        font = P.get().asset.get(Loading.FONT_PIXEL_NUM, BitmapFont.class);
    }

    public void redrawAllSquad() {


        //System.out.println("redraw all");
        SpriteBatch rootBatch = P.rootBatch;
        frameBuffer.begin();

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        rootBatch.setProjectionMatrix(camera.combined);
        boolean beginAfter = false;
        if (rootBatch.isDrawing()) {
            rootBatch.end();
            beginAfter = true;
        }
        rootBatch.begin();
        for (int i = 0; i < HEIGHT_SQUAD; i++) {
            if (i + arrY >= gridPixels.length) break;
            for (int j = 0; j < WIDTH_SQUAD; j++) {
                if (j + arrX >= gridPixels[i].length) break;

                drawOnePixel(j, i, rootBatch);
            }
        }
        rootBatch.end();

        createTexture();

        if (beginAfter) rootBatch.begin();
    }

    private void createTexture() {
        Texture colorBufferTexture = frameBuffer.getColorBufferTexture();
        bufferTexture = new TextureRegion(colorBufferTexture);
        bufferTexture.flip(false, true);
        frameBuffer.end();
    }

    public void redrawOnePixel(int x, int y) {
        SpriteBatch rootBatch = P.rootBatch;
        frameBuffer.begin();

        rootBatch.setProjectionMatrix(camera.combined);
        boolean beginAfter = false;
        if (rootBatch.isDrawing()) {
            rootBatch.end();
            beginAfter = true;
        }
        rootBatch.begin();

        drawOnePixel(x - arrX, y - arrY, rootBatch);

        rootBatch.end();

        createTexture();

        if (beginAfter) rootBatch.begin();
    }

    private void drawOnePixel(int x, int y, Batch batch) {

        sprite.setPosition(x * BigPixel.WIDTH_PIXEL, ((HEIGHT_SQUAD * BigPixel.HEIGHT_PIXEL) - y * BigPixel.HEIGHT_PIXEL) - BigPixel.HEIGHT_PIXEL);
        BigPixel bigPixel = gridPixels[y + arrY][x + arrX];

        switch (bigPixel.getState()) {
            case PAINT:
                sprite.setScale(1);
                sprite.setColor(bigPixel.getColor());
                sprite.draw(batch);
                break;
            case NOT_PAINT:
                sprite.setColor(Color.WHITE);
                sprite.setScale(0.95f);
                sprite.draw(batch);

                FontUtil.drawText(batch, font, String.valueOf(bigPixel.getNumColor()), sprite.getX(), sprite.getY(), FONT_SCALE, P.GRAY_FONT_COLOR, sprite.getWidth(), Align.center, false, sprite.getHeight());

                break;
            case WRONG_PAINT:
                sprite.setScale(1);
                sprite.setColor(P.BACKGROUND_COLOR);
                sprite.draw(batch);
                sprite.setColor(Color.WHITE);
                sprite.setScale(0.95f);
                sprite.draw(batch);

                FontUtil.drawText(batch, font, String.valueOf(bigPixel.getNumColor()), sprite.getX(), sprite.getY(), FONT_SCALE, P.GRAY_FONT_COLOR, sprite.getWidth(), Align.center, false, sprite.getHeight());

                sprite.setScale(1);
                sprite.setColor(bigPixel.getWrongColor());
                sprite.draw(batch);

                break;
        }
    }

    public void touchDown() {

        PaletteButton selectButton = root.getGameViewForName(MainScreen.class).getPaletteButtons().getSelectButton();


        int indX = (int) (downX / BigPixel.WIDTH_PIXEL) + arrX;
        int indY = (int) (((HEIGHT_SQUAD * BigPixel.HEIGHT_PIXEL) - downY) / BigPixel.HEIGHT_PIXEL) + arrY;

        if (indY < gridPixels.length) {
            if (indX < gridPixels[indY].length) {
                BigPixel bigPixel = gridPixels[indY][indX];

                if (selectButton.getNum() == bigPixel.getNumColor()) {
                    root.getTransitObject(ScoreView.class).iteratePixel();
                    bigPixel.setState(BigPixel.State.PAINT);
                } else {
                    bigPixel.setState(BigPixel.State.WRONG_PAINT);
                    Color color = selectButton.getColor().cpy();
                    color.a = 0.5f;
                    bigPixel.setWrongColor(color);
                }
                redrawOnePixel(indX, indY);
            }
        }


    }

    private boolean boundsScreen() {
        float zoom = stageCamera.zoom;

        float xToCamera = (stageCamera.position.x - P.WIDTH / 2 * zoom) * -1;
        float yToCamera = (stageCamera.position.y - (P.HEIGHT * zoom) / 2) - (getY() - (P.HEIGHT * zoom));

        xToCamera += getX();
        return xToCamera > -getWidth() && xToCamera < P.WIDTH * zoom && yToCamera > 0 && yToCamera < P.HEIGHT * zoom + getHeight();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        /*if (isRedrawAll) {
            if (frameBuffer != null) {
                redrawAllSquad();
                isRedrawAll = false;
            }
        }*/
        if (bufferTexture != null) {
            if (boundsScreen()) batch.draw(bufferTexture, getX(), getY());
        }
    }

    @Override
    protected void setParent(Group parent) {
        super.setParent(parent);
        if (parent != null) {
            stageCamera = (OrthographicCamera) parent.getStage().getCamera();
            sprite = ((GroupPixels) parent).getSprite();
            //isRedrawAll = true;
            redrawAllSquad();
        }
    }
}
