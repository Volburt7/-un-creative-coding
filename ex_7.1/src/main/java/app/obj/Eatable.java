package app.obj;

import app.Consts;
import lombok.Getter;
import lombok.Setter;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;

@Getter
@Setter
public class Eatable {
    private PVector position;
    private PImage image;
    private int size;
    private int ttl;

    public Eatable(final PImage img, final PVector position, final int ttl) {
        this.position = position;
        this.image = img;
        this.size = calculateSize(position);
        this.ttl = ttl;
    }

    private int calculateSize(final PVector position) {
        return (int) PApplet.map(position.y, Consts.BG_GRASS_Y_OFFSET, Consts.HEIGHT, 1, 10);
    }

    public void update() {

    }
}
