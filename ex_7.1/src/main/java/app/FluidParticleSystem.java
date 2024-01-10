package app;

import app.obj.ExplosionFluid;
import app.obj.Fluid;
import app.obj.PuddleFluid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import processing.core.PApplet;
import processing.core.PImage;
import processing.event.MouseEvent;

import java.util.ArrayList;
import java.util.List;


public class FluidParticleSystem extends PApplet {
    private final static Logger LOG = LoggerFactory.getLogger(FluidParticleSystem.class);
    private final List<Fluid> fluids = new ArrayList<>();
    private PImage pImageOriginal;
    private PImage background;

    @Override
    public void settings() {
        size(1280, 720);
    }

    @Override
    public void mouseReleased(final MouseEvent mouseEvent) {
        if (mouseEvent.getButton() == LEFT) {
            if (round(random(0, 1)) == 0) {
                fluids.add(new ExplosionFluid(mouseEvent.getX(), mouseEvent.getY(), (int) random(50, 200), random(10, 50)));
            } else {
                fluids.add(new PuddleFluid(mouseEvent.getX(), mouseEvent.getY(), (int) random(50, 200), random(10, 50)));
            }
        }
    }

    @Override
    public void setup() {
        frameRate(60);
        pImageOriginal = loadImage("chicken.png");

        background = loadImage("background.jpg");
        background.resize(1280, 720);
    }

    @Override
    public void draw() {
        image(background, 0, 0);
        for (int i = 15; i >= 6; i--) {
            drawImage(i, (i - 6) * 75);
        }

        final List<Fluid> rf = new ArrayList<>();

        for (Fluid f : fluids) {
            if (f.isActive()) {
                f.update();
                drawFluid(f);
            } else {
                rf.add(f);
            }
        }

        rf.forEach(fluids::remove);
    }

    private void drawImage(final int size, final int y) {
        PImage copy = pImageOriginal.copy();
        copy.resize(size * 4, size * 5);
        image(copy, 0, y);
    }

    void drawFluid(Fluid f) {
        if (f instanceof ExplosionFluid) {
            fill(255, 0, 0);
            ellipse(f.getX(), f.getY(), ((ExplosionFluid) f).getRadius(), ((ExplosionFluid) f).getRadius());
        } else if (f instanceof PuddleFluid) {
            fill(0, 0, 255);
            rectMode(CENTER);
            rect(f.getX(), f.getY(), ((PuddleFluid) f).getSize(), ((PuddleFluid) f).getSize());
        }
    }
}
