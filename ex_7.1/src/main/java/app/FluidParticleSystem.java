package app;

import app.obj.PuddleFluid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import processing.core.PApplet;
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
    }

    @Override
    public void draw() {
        background(20, 30, 140, 140);

        final List<PuddleFluid> rf = new ArrayList<>();
        final List<PuddleFluid> toAdd = new ArrayList<>();

        if(frameCount % 180 == 0) {
            LOG.info("Size: {}", fluidManager.getPuddles().size());
            fluidManager.spawnRandomPuddle(this);
        }

        for (PuddleFluid puddle : fluidManager.getPuddles()) {
            if (puddle.isActive()) {
                puddle.update();
                drawPuddle(puddle);

                if (puddle.shouldRipple()) {
                    toAdd.add(new PuddleFluid(this.fluidManager, puddle.getX(), puddle.getY(), puddle.getInitialLifeSpan() / 2, puddle.getInitialRadius() / 2));
                }
            } else {
                rf.add(puddle);
            }
        }

        rf.forEach(fluidManager.getPuddles()::remove);
        toAdd.forEach(fluidManager::addToList);
    }

    void drawPuddle(final PuddleFluid puddle) {
        noFill();
        stroke(0, 200, 250, puddle.getLifeSpan());
        ellipse(puddle.getX(), puddle.getY(), puddle.getRadius(), puddle.getRadius());
    }
}
