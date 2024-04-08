package app;

import app.obj.Puddle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import processing.core.PApplet;
import processing.event.MouseEvent;

import java.util.ArrayList;
import java.util.List;


public class PuddleSystem extends PApplet {
    private static final Logger LOG = LoggerFactory.getLogger(PuddleSystem.class);
    private final PuddleManager puddleManager;

    private final List<Puddle> autoInitPuddles;

    public PuddleSystem() {
        this.puddleManager = new PuddleManager(this);
        autoInitPuddles = new ArrayList<>();
    }

    @Override
    public void settings() {
        size(1280, 720);
    }

    @Override
    public void mouseReleased(final MouseEvent mouseEvent) {
        if (mouseEvent.getButton() == LEFT) {
            spawnNormalPuddle(mouseEvent.getX(), mouseEvent.getY());
        } else if (mouseEvent.getButton() == RIGHT) {
            spawnPermanentPuddle(mouseEvent.getX(), mouseEvent.getY());
        }
    }

    private void spawnNormalPuddle(final int x, final int y) {
        final Puddle randomPuddle = puddleManager.createRandomPuddle(x, y);
        puddleManager.addToList(randomPuddle);
    }

    private void spawnPermanentPuddle(final int x, final int y) {
        final Puddle randomPuddle = puddleManager.createRandomPuddle(x, y);
        autoInitPuddles.add(randomPuddle);
    }

    @Override
    public void setup() {
        frameRate(60);
        colorMode(RGB);
        spawnNormalPuddle(width / 2, height / 2);
    }

    @Override
    public void draw() {
        background(20, 30, 140, 140);

        for (Puddle puddle : puddleManager.getPuddles()) {
            if (puddle.isActive()) {
                puddleManager.updatePuddle(puddle);
                drawPuddle(puddle);

                if (puddle.shouldRipple()) {
                    puddleManager.addToList(puddleManager.createNextPuddle(puddle));
                }
            } else {
                puddleManager.remove(puddle);
            }

            autoInitPuddles.forEach(initialPuddle -> {
                if (!puddleManager.positionExists(initialPuddle)) {
                    puddleManager.spawnRandomPuddle((int) initialPuddle.getX(), (int) initialPuddle.getY());
                }
            });
        }
    }

    void drawPuddle(final Puddle puddle) {
        noFill();
        stroke(0, 200, 250, puddle.getLifeSpan());
        ellipse(puddle.getX(), puddle.getY(), puddle.getRadius(), puddle.getRadius());
    }
}
