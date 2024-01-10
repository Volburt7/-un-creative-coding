package app.obj;

import app.Consts;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;

import java.util.List;

@Getter
public class Animal {
    private final PImage image;
    private final PVector imageRatio;
    private final int lifeSpan;
    private final int actionDelay;

    private PVector position;
    private int size; // This also describes the layer
    private int remainingLifeSpan;
    private int nextActionIn;

    public Animal(final PImage image, final PVector imageRatio, final int lifeSpan, final int actionDelay, final PVector position, final int remainingLifeSpan, final int nextActionIn) {
        this.image = image;
        this.imageRatio = imageRatio;
        this.lifeSpan = lifeSpan;
        this.actionDelay = actionDelay;
        this.remainingLifeSpan = remainingLifeSpan;
        this.nextActionIn = nextActionIn;
        updatePosition(position);
    }

    public void move() {
        updatePosition(getNewPosition())
    }

    private void updatePosition(final PVector position) {
        this.position = position;
        this.size = this.calculateSize(position.y);
    }

    private PVector getValidatedPosition(final PVector newPosition) {

    }

    private int calculateSize(final float yPosition) {
        return PApplet.ceil(PApplet.map(yPosition, Consts.BG_GRASS_Y_OFFSET, Consts.MAIN_WINDOW_HEIGHT, 6, 14));
    }

    private void resizeImage() {
        final PImage image = super.getImage();
        image.resize(this.imageWidthRatio * super.getSize(), this.imageHeightRatio * super.getSize());
    }
}
