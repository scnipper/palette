package me.creese.palette.game.util;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;

public class Shapes {
    private static final String fragShader = "#ifdef GL_ES\n" + "#define LOWP lowp\n" + "precision mediump float;\n" + "#else\n" + "#define LOWP\n" + "#endif\n" + "\n" + "varying LOWP vec4 v_color;\n" + "\n" + "\n" + "void main()\n" + "{\n" + "  gl_FragColor = v_color;\n" + "}";
    private static final String vertShader = "attribute vec4 a_position;\n" + "attribute vec4 a_color;\n" + "\n" + "uniform mat4 u_projTrans;\n" + "varying vec4 v_color;\n" + "\n" + "\n" + "void main()\n" + "{\n" + "   v_color = a_color;\n" + "   gl_Position =  u_projTrans * a_position;\n" + "}";
    private final ShaderProgram shaderShape;
    private final Mesh mesh;
    private float[] vertices;
    private short[] indices;
    private int indexVert = 0;
    private int indexInd = 0;
    private short numVert;
    private Matrix4 projMatrix;
    private float smooth;
    private float color = Color.WHITE.toFloatBits();
    private float clearColor = new Color(1, 1, 1, 0).toFloatBits();
    private Vector2 tmp;

    public Shapes() {

        shaderShape = new ShaderProgram(vertShader, fragShader);
        mesh = new Mesh(Mesh.VertexDataType.VertexArray, false, 12000, 24000, new VertexAttribute(VertexAttributes.Usage.Position, 2, ShaderProgram.POSITION_ATTRIBUTE), new VertexAttribute(VertexAttributes.Usage.ColorPacked, 4, ShaderProgram.COLOR_ATTRIBUTE));
        vertices = new float[12000];
        indices = new short[(vertices.length / 2) * 3];

        smooth = 1.5f;
        tmp = new Vector2();


    }

    public void flush() {
        mesh.setVertices(vertices, 0, indexVert);
        mesh.setIndices(indices, 0, indexInd);
        shaderShape.begin();
        shaderShape.setUniformMatrix("u_projTrans", projMatrix);
        mesh.render(shaderShape, GL20.GL_TRIANGLES, 0, indexInd);
        shaderShape.end();


        indexVert = 0;
        indexInd = 0;
        numVert = 0;

    }

    public boolean check(int newVert) {
        if (vertices.length - indexVert < newVert * 3) {
            flush();
            return true;

        }
        return false;
    }

    public short vertexAdd(float x, float y, float color) {

        vertices[indexVert] = x;
        vertices[indexVert + 1] = y;
        vertices[indexVert + 2] = color;


        indexVert += 3;
        numVert++;
        return (short) (numVert - 1);
    }

    public void indicesAdd(short... ind) {
        System.arraycopy(ind, 0, indices, indexInd, ind.length);
        indexInd += ind.length;
    }

    public void line(float x1, float y1, float x2, float y2, float width) {

        Vector2 t = tmp.set(y2 - y1, x1 - x2).nor();
        width *= 0.5f;
        float tx = t.x * (width - smooth);
        float ty = t.y * (width - smooth);

        float txSmooth = t.x * width;
        float tySmooth = t.y * width;
        //if (shapeType == ShapeType.Line) {

        check(8);
        short vertex1 = vertexAdd(x1 + tx, y1 + ty, color);
        short vertex2 = vertexAdd(x1 - tx, y1 - ty, color);

        short vertex3 = vertexAdd(x2 + tx, y2 + ty, color);
        short vertex4 = vertexAdd(x2 - tx, y2 - ty, color);

        short vertex5 = vertexAdd(x1 + txSmooth, y1 + tySmooth, clearColor);
        short vertex6 = vertexAdd(x1 - txSmooth, y1 - tySmooth, clearColor);

        short vertex7 = vertexAdd(x2 + txSmooth, y2 + tySmooth, clearColor);
        short vertex8 = vertexAdd(x2 - txSmooth, y2 - tySmooth, clearColor);

        indicesAdd(vertex1, vertex2, vertex3, vertex2, vertex3, vertex4, vertex1, vertex5, vertex7, vertex1, vertex7, vertex3, vertex2, vertex6, vertex8, vertex2, vertex8, vertex4);


    }

    public void rectRound(float x, float y, float width, float height, float radius) {

        float smoothTmp = smooth;
        smooth = 0;
        //center
        rect(x + radius, y + radius, width - radius * 2, height - radius * 2);
        smooth = smoothTmp;
        //bottom
        rect(x + radius, y, width - radius * 2, radius, SmoothSide.BOTTOM);
        //left
        rect(x, y + radius, radius, height - radius * 2, SmoothSide.LEFT);
        //right
        rect(x + (width - radius), y + radius, radius, height - radius * 2, SmoothSide.RIGHT);
        //top
        rect(x + radius, y + (height - radius), width - radius * 2, radius, SmoothSide.TOP);


        // left bottom corner
        arc(x + radius, y + radius, radius, 180, 90);
        //left top corner
        arc(x + radius, y + (height - radius), radius, 90, 90);
        // right top corner
        arc(x + (width - radius), y + (height - radius), radius, 0, 90);
        // right bottom corner
        arc(x + (width - radius), y + radius, radius, 270, 90);


    }

    public void rect(float x, float y, float width, float height) {
        rect(x, y, width, height, SmoothSide.ALL);
    }

    public void rect(float x, float y, float width, float height, SmoothSide smoothSide) {


        check(8);

        float x1_1 = x + smooth;
        float y1_1 = y + smooth;

        float x2_1 = x + smooth;
        float y2_1 = (y + height) - smooth;

        float x3_1 = (x + width) - smooth;
        float y3_1 = (y + height) - smooth;

        float x4_1 = (x + width) - smooth;
        float y4_1 = y + smooth;


        float x1_2 = x;
        float y1_2 = y;

        float x2_2 = x;
        float y2_2 = y + height;

        float x3_2 = x + width;
        float y3_2 = y + height;

        float x4_2 = x + width;
        float y4_2 = y;

        /*if(degrees > 0) {

            float originX = width/2;
            float originY = height/2;
            float cos = MathUtils.cosDeg(degrees);
            float sin = MathUtils.sinDeg(degrees);
            float fx = -originX;
            float fy = -originY;
            float fx2 = width - originX;
            float fy2 = height - originY;

            float worldOriginX = x + originX;
            float worldOriginY = y + originY;

            x1_1 = cos * fx - sin * fy + worldOriginX;
            y1_1 = sin * fx + cos * fy + worldOriginY;

            x2_1 = cos * fx2 - sin * fy + worldOriginX;
            y2_1 = sin * fx2 + cos * fy + worldOriginY;

            x3_1 = cos * fx2 - sin * fy2 + worldOriginX;
            y3_1 = sin * fx2 + cos * fy2 + worldOriginY;

            x4_1 = x1_1 + (x3_1 - x2_1);
            y4_1 = y3_1 - (y2_1 - y1_1);


            fx-= smooth;
            fy-= smooth;

            fx2+= smooth;
            fy2+= smooth;

            x1_2 = cos * fx - sin * fy + worldOriginX;
            y1_2 = sin * fx + cos * fy + worldOriginY;

            x2_2 = cos * fx2 - sin * fy + worldOriginX;
            y2_2 = sin * fx2 + cos * fy + worldOriginY;

            x3_2 = cos * fx2 - sin * fy2 + worldOriginX;
            y3_2 = sin * fx2 + cos * fy2 + worldOriginY;

            x4_2 = x1_2 + (x3_2 - x2_2);
            y4_2 = y3_2 - (y2_2 - y1_2);
        }*/

        short vertex1 = vertexAdd(x1_1, y1_1, color);
        short vertex2 = vertexAdd(x2_1, y2_1, color);
        short vertex3 = vertexAdd(x3_1, y3_1, color);
        short vertex4 = vertexAdd(x4_1, y4_1, color);
        short vertex5;
        short vertex6;
        short vertex7;
        short vertex8;
        if (smooth > 0) {
            switch (smoothSide) {
                case ALL:
                    vertex5 = vertexAdd(x1_2, y1_2, clearColor);
                    vertex6 = vertexAdd(x2_2, y2_2, clearColor);
                    vertex7 = vertexAdd(x3_2, y3_2, clearColor);
                    vertex8 = vertexAdd(x4_2, y4_2, clearColor);

                    indicesAdd(vertex1, vertex2, vertex3, vertex1, vertex4, vertex3, vertex5, vertex1, vertex2, vertex5, vertex2, vertex6, vertex2, vertex6, vertex3, vertex6, vertex3, vertex7, vertex7, vertex3, vertex4, vertex7, vertex4, vertex8, vertex8, vertex4, vertex1, vertex8, vertex1, vertex5);
                    break;

                case LEFT:

                    translateY(vertex1, -smooth);
                    translateY(vertex2, smooth);

                    translateX(vertex3, smooth);
                    translateY(vertex3, smooth);

                    translateX(vertex4, smooth);
                    translateY(vertex4, -smooth);

                    vertex5 = vertexAdd(x1_2, y1_2, clearColor);
                    vertex6 = vertexAdd(x2_2, y2_2, clearColor);

                    indicesAdd(vertex1, vertex2, vertex3, vertex1, vertex4, vertex3, vertex5, vertex1, vertex2, vertex5, vertex6, vertex2);
                    break;
                case RIGHT:

                    translateY(vertex3, smooth);
                    translateY(vertex4, -smooth);

                    translateX(vertex1, -smooth);
                    translateY(vertex1, -smooth);

                    translateX(vertex2, -smooth);
                    translateY(vertex2, smooth);
                    vertex5 = vertexAdd(x4_2, y4_2, clearColor);
                    vertex6 = vertexAdd(x3_2, y3_2, clearColor);

                    indicesAdd(vertex1, vertex2, vertex3, vertex1, vertex4, vertex3, vertex5, vertex4, vertex6, vertex4, vertex3, vertex6);
                    break;

                case TOP:

                    translateX(vertex2, -smooth);
                    translateX(vertex3, smooth);

                    translateX(vertex1, -smooth);
                    translateY(vertex1, -smooth);

                    translateX(vertex4, smooth);
                    translateY(vertex4, -smooth);
                    vertex5 = vertexAdd(x2_2, y2_2, clearColor);
                    vertex6 = vertexAdd(x3_2, y3_2, clearColor);

                    indicesAdd(vertex1, vertex2, vertex3, vertex1, vertex4, vertex3, vertex5, vertex2, vertex6, vertex2, vertex3, vertex6);
                    break;
                case BOTTOM:

                    translateX(vertex1, -smooth);
                    translateX(vertex4, smooth);

                    translateX(vertex2, -smooth);
                    translateY(vertex2, smooth);

                    translateX(vertex3, smooth);
                    translateY(vertex3, smooth);
                    vertex5 = vertexAdd(x1_2, y1_2, clearColor);
                    vertex6 = vertexAdd(x4_2, y4_2, clearColor);

                    indicesAdd(vertex1, vertex2, vertex3, vertex1, vertex4, vertex3, vertex5, vertex1, vertex4, vertex5, vertex4, vertex6);
                    break;
            }

        } else {
            indicesAdd(vertex1, vertex2, vertex3, vertex1, vertex4, vertex3);
        }

    }

    public void circle(float x, float y, float radius) {
        circle(x, y, radius, false);
    }

    public void circle(float x, float y, float radius, boolean fromCenter) {
        int segments = Math.max(1, (int) (6 * (float) Math.cbrt(radius))) * 3;

        if (!fromCenter) {
            x += radius;
            y += radius;
        }


        float angle = 2 * MathUtils.PI / segments;
        float cos = MathUtils.cos(angle);
        float sin = MathUtils.sin(angle);
        float cx = radius - 1.5f, cy = 0;
        float cx2 = radius, cy2 = 0;
        segments += 2;
        check(segments * 2 + 3);
        short startVert = vertexAdd(x, y, color);
        vertexAdd(x + cx, y + cy, color);
        vertexAdd(x + cx2, y + cy2, clearColor);

        for (int i = 0; i < segments; i++) {

            if (check(2)) {
                startVert = vertexAdd(x, y, color);
                vertexAdd(x + cx, y + cy, color);
                vertexAdd(x + cx2, y + cy2, clearColor);
            }
            float temp = cx;
            cx = cos * cx - sin * cy;
            cy = sin * temp + cos * cy;

            temp = cx2;
            cx2 = cos * cx2 - sin * cy2;
            cy2 = sin * temp + cos * cy2;

            vertexAdd(x + cx, y + cy, color);
            vertexAdd(x + cx2, y + cy2, clearColor);

            short vertex1 = (short) (numVert - 4);
            short vertex2 = (short) (numVert - 3);
            short vertex3 = (short) (numVert - 2);
            short vertex4 = (short) (numVert - 1);
            indicesAdd(startVert, vertex1, vertex3, vertex1, vertex2, vertex4, vertex1, vertex3, vertex4);
        }

        //indicesAdd(startVert, (short) (numVert - 2), (short) (startVert + 1), (short) (numVert - 1), (short) (startVert + 2), (short) (startVert + 1), (short) (numVert - 2), (short) (numVert - 1), (short) (startVert + 1));
    }

    public void circleLine(float x, float y, float radius, float width) {
        int segments = Math.max(1, (int) (6 * (float) Math.cbrt(radius))) * 3;


        x += radius;
        y += radius;

        float angle = 2 * MathUtils.PI / segments;
        segments += 2;
        check(segments * 4 + 4);
        float cos = MathUtils.cos(angle);
        float sin = MathUtils.sin(angle);

        //p1
        float cx1 = radius - width, cy1 = 0;
        //p2
        float cx2 = (radius - width) + smooth, cy2 = 0;
        //p3
        float cx3 = radius - smooth, cy3 = 0;
        //p4
        float cx4 = radius, cy4 = 0;
        //check(4);
        vertexAdd(x + cx1, y + cy1, clearColor);
        vertexAdd(x + cx2, y + cy2, color);
        vertexAdd(x + cx3, y + cy3, color);
        vertexAdd(x + cx4, y + cy4, clearColor);
        for (int i = 0; i < segments; i++) {

            if (check(4)) {
                vertexAdd(x + cx1, y + cy1, clearColor);
                vertexAdd(x + cx2, y + cy2, color);
                vertexAdd(x + cx3, y + cy3, color);
                vertexAdd(x + cx4, y + cy4, clearColor);
            }
            float temp = cx1;
            cx1 = cos * cx1 - sin * cy1;
            cy1 = sin * temp + cos * cy1;

            temp = cx2;
            cx2 = cos * cx2 - sin * cy2;
            cy2 = sin * temp + cos * cy2;

            temp = cx3;
            cx3 = cos * cx3 - sin * cy3;
            cy3 = sin * temp + cos * cy3;

            temp = cx4;
            cx4 = cos * cx4 - sin * cy4;
            cy4 = sin * temp + cos * cy4;

            short vertex5 = vertexAdd(x + cx1, y + cy1, clearColor);

            short vertex6 = vertexAdd(x + cx2, y + cy2, color);
            short vertex7 = vertexAdd(x + cx3, y + cy3, color);

            short vertex8 = vertexAdd(x + cx4, y + cy4, clearColor);


            short vertex1 = (short) (numVert - 8);
            short vertex2 = (short) (numVert - 7);
            short vertex3 = (short) (numVert - 6);
            short vertex4 = (short) (numVert - 5);


            indicesAdd(vertex1, vertex5, vertex6, vertex1, vertex2, vertex6,

                    vertex2, vertex6, vertex7, vertex2, vertex3, vertex7,

                    vertex3, vertex7, vertex8, vertex3, vertex4, vertex8);

        }
    }

    public void ellipse(float x, float y, float width, float height) {
        int segments = Math.max(1, (int) (12 * (float) Math.cbrt(Math.max(width * 0.5f, height * 0.5f)))) * 8;


        float angle = MathUtils.PI2 / segments;
        segments += 2;
        check(segments * 4 + 1);
        float cx = x + width / 2, cy = y + height / 2;

        short centerVertex = vertexAdd(cx, cy, color);
        for (int i = 0; i < segments; i++) {
            if (check(4)) {
                centerVertex = vertexAdd(cx, cy, color);
            }
            float cos = MathUtils.cos(i * angle);
            float sin = MathUtils.sin((i + 1) * angle);

            float tmpW = width - smooth;
            float tmpH = height - smooth;

            short vertex1 = vertexAdd(cx + (tmpW * 0.5f * cos), cy + (tmpH * 0.5f * MathUtils.sin(i * angle)), color);

            short vertex2 = vertexAdd(cx + (tmpW * 0.5f * MathUtils.cos((i + 1) * angle)), cy + (tmpH * 0.5f * sin), color);


            short vertex3 = vertexAdd(cx + (width * 0.5f * MathUtils.cos(i * angle)), cy + (height * 0.5f * MathUtils.sin(i * angle)), clearColor);

            short vertex4 = vertexAdd(cx + (width * 0.5f * MathUtils.cos((i + 1) * angle)), cy + (height * 0.5f * MathUtils.sin((i + 1) * angle)), clearColor);


            indicesAdd(vertex1, centerVertex, vertex2, vertex3, vertex1, vertex2, vertex2, vertex4, vertex3);

        }


    }

    public void arc(float x, float y, float radius, float start, float degrees) {

        int segments = Math.max(1, (int) (6 * (float) Math.cbrt(radius))) * 3;

        float theta = (2 * MathUtils.PI * (degrees / 360.0f)) / segments;
        float cos = MathUtils.cos(theta);
        float sin = MathUtils.sin(theta);
        float cx = (radius - smooth) * MathUtils.cos(start * MathUtils.degreesToRadians);
        float cy = (radius - smooth) * MathUtils.sin(start * MathUtils.degreesToRadians);


        float cx2 = radius * MathUtils.cos(start * MathUtils.degreesToRadians);
        float cy2 = radius * MathUtils.sin(start * MathUtils.degreesToRadians);
        //segments++;
        check(segments * 2 + 3);
        short startVert = vertexAdd(x, y, color);
        vertexAdd(x + cx, y + cy, color);
        vertexAdd(x + cx2, y + cy2, clearColor);

        for (int i = 0; i < segments; i++) {

            if (check(2)) {
                startVert = vertexAdd(x, y, color);
                vertexAdd(x + cx, y + cy, color);
                vertexAdd(x + cx2, y + cy2, clearColor);
            }
            float temp = cx;
            cx = cos * cx - sin * cy;
            cy = sin * temp + cos * cy;

            temp = cx2;
            cx2 = cos * cx2 - sin * cy2;
            cy2 = sin * temp + cos * cy2;

            vertexAdd(x + cx, y + cy, color);
            vertexAdd(x + cx2, y + cy2, clearColor);

            short vertex1 = (short) (numVert - 4);
            short vertex2 = (short) (numVert - 3);
            short vertex3 = (short) (numVert - 2);
            short vertex4 = (short) (numVert - 1);
            indicesAdd(startVert, vertex1, vertex3, vertex1, vertex2, vertex4, vertex1, vertex3, vertex4);
          /*  if(i == segments -1) {
                vertexAdd(x + cx, y + cy, color);
                vertexAdd(x + cx2, y + cy2, clearColor);

                vertex1 = (short) (numVert - 4);
                vertex2 = (short) (numVert - 3);
                vertex3 = (short) (numVert - 2);
                vertex4 = (short) (numVert - 1);
                indicesAdd(startVert, vertex1, vertex3, vertex1, vertex2, vertex4, vertex1, vertex3, vertex4);
            }*/
        }
    }

    public void triangle(float x1, float y1, float x2, float y2, float x3, float y3) {

        check(6);
        smooth = 30;
        short vertex1 = vertexAdd(x1 + smooth, y1 + smooth, color);
        short vertex2 = vertexAdd(x2 + smooth, y2 - smooth * 2, color);
        short vertex3 = vertexAdd(x3 - smooth * 2, y3 + smooth, color);

        short vertex4 = vertexAdd(x1, y1, Color.YELLOW.toFloatBits());
        short vertex5 = vertexAdd(x2, y2, Color.YELLOW.toFloatBits());
        short vertex6 = vertexAdd(x3, y3, Color.YELLOW.toFloatBits());
        Vector2 vector1 = new Vector2(x3, y3);

        Vector2 vector2 = new Vector2(x1, y1);

        System.out.println(vector1.dst(vector2));
        indicesAdd(vertex1, vertex2, vertex3, vertex1, vertex4, vertex5, vertex1, vertex5, vertex2, vertex5, vertex2, vertex3, vertex5, vertex3, vertex6, vertex6, vertex3, vertex4, vertex3, vertex4, vertex1);
    }

    /**
     * Translate vertex
     *
     * @param vertex
     * @param val
     */
    private void translateX(short vertex, float val) {
        vertices[vIndex(vertex)] += val;
    }

    private void translateY(short vertex, float val) {
        vertices[vIndex(vertex) + 1] += val;
    }

    private int vIndex(short vertex) {
        return vertex * 3;
    }

    public float getSmooth() {
        return smooth;
    }

    public void setSmooth(float smooth) {
        this.smooth = smooth;
    }

    public void setColor(Color color) {
        this.color = color.toFloatBits();
        Color tmp = color.cpy();
        tmp.a = 0;
        clearColor = tmp.toFloatBits();


    }

    public void setProjMatrix(Matrix4 projMatrix) {
        this.projMatrix = projMatrix;
    }

    public enum SmoothSide {
        ALL, LEFT, RIGHT, TOP, BOTTOM
    }
}
