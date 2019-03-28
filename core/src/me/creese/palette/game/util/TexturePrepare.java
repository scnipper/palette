package me.creese.palette.game.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.ScreenUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class TexturePrepare extends Thread implements Disposable {


    private final OrthographicCamera camera;
    private final ArrayList<TextDist> textureDist;
    private final ArrayList<Texture> pages;
    private final FrameBuffer fbo;
    public ArrayList<Rectangle> rects;
    private int widthFbo = 256;
    private int heightFbo = 256;
    private int paddingX = 0;
    private int paddingY = 0;
    private PreAndPostDraw preAndPostDraw;
    private boolean isLoad;
    private boolean isDebugImage;

    public TexturePrepare() {
        super("Prepare-thread");
        pages = new ArrayList<>();

        textureDist = new ArrayList<>();
        fbo = new FrameBuffer(Pixmap.Format.RGBA8888, widthFbo, heightFbo, true);
        camera = new OrthographicCamera(widthFbo, heightFbo);
        camera.translate(widthFbo / 2, heightFbo / 2);
        camera.update();
    }

    private void clear() {
        Gdx.gl.glClearColor(1, 1, 1, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_STENCIL_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
    }

    public void addDraw(int id, int width, int height, Draw draw) {
        Rectangle rect = new Rectangle(0, 0, width, height);
        textureDist.add(new TextDist(draw, id, rect));
    }

    public Sprite getByName(int name) {
        int indexName = -1;

        for (int i = 0; i < textureDist.size(); i++) {
            if (textureDist.get(i).name == name) indexName = i;
        }

        if (indexName == -1) throw new GdxRuntimeException("Dont find texture");
        Rectangle rectangle = textureDist.get(indexName).rect;

        return new Sprite(pages.get(textureDist.get(indexName).page), (int) rectangle.x, (int) rectangle.y, (int) rectangle.width, (int) rectangle.height);
    }

    /*    private void calcSquares() {
            int stdSize = widthFbo * heightFbo;

            int fullSize = 0;

            for (TextDist textDist : textureDist) {
                fullSize+= textDist.rect.width * textDist.rect.height;
            }

            int countPages = (fullSize / stdSize)+1;

            for (int i = 0; i < countPages; i++) {
                pages.add(new Fbo(new FrameBuffer(Pixmap.Format.RGBA8888, widthFbo, heightFbo, false)));
            }
        }*/
    private void setsPosition() {
        rects = new ArrayList<>();

        rects.add(new Rectangle(0, 0, widthFbo, heightFbo));
        for (TextDist textDist : textureDist) {

            boolean isSetPos = false;
            for (int i = 0; i < rects.size(); i++) {
                Rectangle currRect = rects.get(i);
                textDist.rect.setPosition(currRect.x + paddingX, currRect.y + paddingY);
                if (contains(currRect, textDist.rect)) {
                    isSetPos = true;
                    // right
                    Rectangle r1 = new Rectangle(textDist.rect.x + textDist.rect.width, textDist.rect.y, currRect.width - textDist.rect.width, textDist.rect.height);
                    addRects(r1);
                    //top
                    Rectangle r2 = new Rectangle(textDist.rect.x, textDist.rect.y + textDist.rect.height, currRect.width, currRect.height - textDist.rect.height);
                    addRects(r2);
                    rects.remove(i);
                    break;

                }
            }

            if (!isSetPos) {
                throw new GdxRuntimeException("flush size");
            }
        }


    }

    private void checkRects() {
        if (rects.size() > 1) {
            for (int i = 0; i < rects.size(); i++) {
                Rectangle r1 = rects.get(i);
                for (int j = 0; j < rects.size(); j++) {
                    Rectangle r2 = rects.get(j);
                    if (r1.y + r1.height == r2.y) {
                        addRects(new Rectangle(r2.x, r1.y, r2.width, r1.height + r2.height));
                        addRects(new Rectangle(r1.x, r1.y, r1.width - r2.width, r1.height));
                        break;
                        //rects.remove(i);
                    }

                }
            }
        }
    }

    private void addRects(Rectangle rect) {
        if (rect.width > 0 && rect.height > 0) {
            rects.add(rect);
        }
    }

    public boolean contains(Rectangle r1, Rectangle r2) {
        float xmin = r2.x;
        float xmax = xmin + r2.width;

        float ymin = r2.y;
        float ymax = ymin + r2.height;

        return ((xmin >= r1.x && xmin <= r1.x + r1.width) && (xmax > r1.x && xmax <= r1.x + r1.width)) && ((ymin >= r1.y && ymin <= r1.y + r1.height) && (ymax > r1.y && ymax <= r1.y + r1.height));

    }

    public OrthographicCamera getCamera() {
        return camera;
    }

    public void setPreAndPostDraw(PreAndPostDraw preAndPostDraw) {
        this.preAndPostDraw = preAndPostDraw;
    }

    public boolean isDebugImage() {
        return isDebugImage;
    }

    public void setDebugImage(boolean debugImage) {
        isDebugImage = debugImage;
    }

    public boolean isLoad() {
        return isLoad;
    }

    public void setPaddingX(int paddingX) {
        this.paddingX = paddingX;
    }

    public void setPaddingY(int paddingY) {
        this.paddingY = paddingY;
    }

    @Override
    public void run() {
        TextDist[] arr = textureDist.toArray(new TextDist[]{});
        Arrays.sort(arr);

        textureDist.clear();
        for (TextDist a : arr) {
            textureDist.add(a);
        }

        setsPosition();
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {

                fbo.begin();


                clear();

                if (preAndPostDraw != null) {
                    preAndPostDraw.drawPre();
                }

                for (TextDist textDist : textureDist) {
                    // if(textDist.page == i)

                    textDist.draw.draw(textDist.rect.x, textDist.rect.y);
                }
                if (preAndPostDraw != null) {
                    preAndPostDraw.drawPost();
                }

            /*ShapeRenderer shapeRenderer = new ShapeRenderer();
            shapeRenderer.setProjectionMatrix(camera.combined);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            Random random = new Random();
            for (Rectangle rect : rects) {
                shapeRenderer.setColor(new Color(random.nextInt(0x7fffffff)));
                shapeRenderer.rect(rect.x,rect.y,rect.width,rect.height);
            }
            shapeRenderer.flush();*/
                Pixmap p = ScreenUtils.getFrameBufferPixmap(0, 0, fbo.getWidth(), fbo.getHeight());

                pages.add(new Texture(p));


                fbo.end();
                fbo.dispose();

                if (isDebugImage) {
                    for (int i = 0; i < pages.size(); i++) {
                        Pixmap pixmap = pages.get(i).getTextureData().consumePixmap();
                        PixmapIO.PNG png = new PixmapIO.PNG();
                        try {
                            FileHandle external = Gdx.files.external("img_" + i + ".png");
                            System.out.println(external.file().getAbsolutePath());
                            png.write(external, pixmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                p.dispose();
                isLoad = true;
            }
        });

    }

    @Override
    public void dispose() {
        for (Texture page : pages) {
            page.dispose();
        }
    }

    public interface Draw {
        void draw(float bX, float bY);
    }

    public interface PreAndPostDraw {
        void drawPre();

        void drawPost();
    }

    class TextDist implements Comparable<TextDist> {
        public Draw draw;
        public int name;
        public Rectangle rect;
        public int page = 0;


        public TextDist(Draw draw, int name, Rectangle rect) {
            this.draw = draw;
            this.name = name;
            this.rect = rect;
        }

        @Override
        public int compareTo(TextDist o) {
            float s1 = rect.width * rect.height;
            float s2 = o.rect.width * o.rect.height;
            return Float.compare(s2, s1);
        }
    }

}
