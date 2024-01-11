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
    private final FluidManager fluidManager = new FluidManager();



    @Override
    public void settings() {
        size(1280, 720);
    }

    @Override
    public void mouseReleased(final MouseEvent mouseEvent) {
        if (mouseEvent.getButton() == LEFT) {
            fluidManager.addToList(new PuddleFluid(this.fluidManager, mouseEvent.getX(), mouseEvent.getY(), (int) random(50, 200), random(10, 50)));
        } else if (mouseEvent.getButton() == RIGHT) {
            fluidManager.addToList(new ExplosionFluid(this.fluidManager, mouseEvent.getX(), mouseEvent.getY(), (int) random(50, 200), random(10, 50)));
        }
    }

    @Override
    public void setup() {
        frameRate(60);
        colorMode(RGB);
        noStroke();
    }

    @Override
    public void draw() {
        background(20, 30, 140, 140);

        final List<Fluid> rf = new ArrayList<>();
        final List<Fluid> toAdd = new ArrayList<>();

        if(frameCount % 60 == 0) {
            fluidManager.spawnRandomPuddle(this);
        }


        for (Fluid f : fluidManager.getFluids()) {
            if (f.isActive()) {
                f.update();
                drawFluid(f);

                if (f instanceof PuddleFluid puddle && puddle.shouldRippleNow()) {
                    toAdd.add(new PuddleFluid(this.fluidManager, puddle.getX(), puddle.getY(), puddle.getInitialLifeSpan(), puddle.getInitialRadius() / 2));
                }
            } else {
                rf.add(f);
            }
        }

        rf.forEach(fluidManager.getFluids()::remove);
        toAdd.forEach(fluidManager::addToList);
    }

    void drawFluid(final Fluid f) {
        if (f instanceof ExplosionFluid fluid) {
            fill(255, 0, 0);
//            ellipse(fluid.getX(), fluid.getY(), fluid.get(), fluid.getRadius());
        } else if (f instanceof PuddleFluid fluid) {
            fill(0, 0, 255, fluid.getLifeSpan());
            ellipseMode(RADIUS);
            ellipse(fluid.getX(), fluid.getY(), fluid.getRadius(), fluid.getRadius());
        }
    }
}
