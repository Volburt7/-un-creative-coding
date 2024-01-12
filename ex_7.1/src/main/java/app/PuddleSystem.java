package app;

import app.obj.Puddle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import processing.core.PApplet;
import processing.event.MouseEvent;

import java.util.ArrayList;
import java.util.List;


public class PuddleSystem extends PApplet {
    private final static Logger LOG = LoggerFactory.getLogger(PuddleSystem.class);
    private final PuddleManager puddleManager;

    public PuddleSystem() {
        this.puddleManager = new PuddleManager(this);
    }

    @Override
    public void settings() {
        size(1280, 720);
    }

    @Override
    public void mouseReleased(final MouseEvent mouseEvent) {
        if (mouseEvent.getButton() == LEFT) {
            puddleManager.spawnRandomPuddle(mouseEvent.getX(), mouseEvent.getY());
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

        final List<Puddle> rf = new ArrayList<>();
        final List<Puddle> toAdd = new ArrayList<>();

        for (Puddle puddle : puddleManager.getPuddles()) {
            if (puddle.isActive()) {
                puddle.update();
                drawPuddle(puddle);

                if (puddle.shouldRipple()) {
                    toAdd.add(puddleManager.createNextPuddle(puddle));
                }
            } else {
                rf.add(puddle);
            }
        }

        rf.forEach(puddleManager.getPuddles()::remove);
        toAdd.forEach(puddleManager::addToList);
    }

    void drawPuddle(final Puddle puddle) {
        noFill();
        stroke(0, 200, 250, puddle.getLifeSpan());
        ellipse(puddle.getX(), puddle.getY(), puddle.getRadius(), puddle.getRadius());
    }
}
