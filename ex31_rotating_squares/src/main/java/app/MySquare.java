package app;

import java.util.List;

public class MySquare {
    private float rotation;
    private float sizeScale;
    private List<Float> color;

    public MySquare(final float rotation, final float sizeScale, final List<Float> color) {
        this.rotation = rotation;
        this.sizeScale = sizeScale;
        this.color = color;
    }

    public MySquare(final float sizeScale, final List<Float> color) {
        this.sizeScale = sizeScale;
        this.color = color;
    }

    public float getRotation() {
        return rotation;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    public float getSizeScale() {
        return sizeScale;
    }

    public void setSizeScale(float sizeScale) {
        this.sizeScale = sizeScale;
    }

    public List<Float> getColor() {
        return color;
    }

    public void setColor(List<Float> color) {
        this.color = color;
    }
}
