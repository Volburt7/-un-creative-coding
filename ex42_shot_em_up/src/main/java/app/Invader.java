package app;

public class Invader {
    private final int squareSize;
    private final Pixel[][] pixels;
    private int pixelSize;
    private float x;
    private float y;
    private float velocity;

    public Invader(final int squareSize, final int pixelSize, final int xPosition, final float velocity) {
        this.squareSize = squareSize;
        this.pixelSize = pixelSize;
        this.pixels = new Pixel[squareSize][squareSize];
        this.x = xPosition;
        this.y = 0;
        this.velocity = velocity;
    }

    public void setPixel(final int w, final int h, final Pixel pixel) {
        for (int ww = w; ww < w + this.pixelSize; ww++) {
            for (int hh = h; hh < h + this.pixelSize; hh++) {
                final int wRight = this.getSquareSize() - ww - 1;
                final int hBottom = this.getSquareSize() - hh - 1;
                this.pixels[ww][hh] = pixel;
                this.pixels[wRight][hh] = pixel;
                this.pixels[ww][hBottom] = pixel;
                this.pixels[wRight][hBottom] = pixel;
            }
        }
    }

    public boolean isClickedOn(final int x, final int y) {
        return (this.x < x && this.x + squareSize > x && this.y < y && this.y + squareSize > y);
    }

    public int getSquareSize() {
        return squareSize;
    }

    public int getPixelSize() {
        return pixelSize;
    }

    public void setPixelSize(int pixelSize) {
        this.pixelSize = pixelSize;
    }

    public Pixel[][] getPixels() {
        return pixels;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getVelocity() {
        return velocity;
    }

    public void setVelocity(float velocity) {
        this.velocity = velocity;
    }
}