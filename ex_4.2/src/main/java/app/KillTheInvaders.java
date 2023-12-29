package app;

import processing.core.PApplet;
import processing.core.PImage;
import processing.event.MouseEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class KillTheInvaders extends PApplet {
    final List<Invader> invaders = new ArrayList<>();
    final Lock invaderLock = new ReentrantLock();
    final int INVADER_SQUARE = 48;
    final int INVADER_PIXEL = 4;
    int nextInvaderSeconds = 3;
    int currentScore = 0;

    @Override
    public void settings() {
        size(512, 1024);
    }

    @Override
    public void setup() {
        frameRate(60);
        changeCursor();
    }

    private void changeCursor() {
        final PImage customCursor = loadImage("cursor.png");
        customCursor.resize(32, 32);
        cursor(customCursor);
    }

    @Override
    public void draw() {
        background(0);
        if (frameCount % 30 * nextInvaderSeconds == 0) {
            addInvader();
            nextInvaderSeconds = (int) random(1, 5);
        }
        cleanUpMissedInvader();
        drawInvaders();
        displayScore();
    }

    @Override
    public void mouseReleased(MouseEvent event) {
        shoot(event.getX(), event.getY());
    }

    private void addInvader() {
        final Invader invader = new Invader(INVADER_SQUARE, INVADER_PIXEL, (int) random(0, width - INVADER_SQUARE), random(1, 4));
        final List<Float[]> invaderColors = getRandomColors();
        for (int w = 0; w < INVADER_SQUARE / 2; w += INVADER_PIXEL) {
            for (int h = 0; h < INVADER_SQUARE / 2; h += INVADER_PIXEL) {
                Pixel pixel = new Pixel(0, 0, 0);
                if (round(random(0, 1)) == 1) {
                    final int colorIndex = floor(random(0, invaderColors.size()));
                    if (colorIndex != invaderColors.size()) { // Prevent out-of-bounce
                        final Float[] color = invaderColors.get(colorIndex);
                        pixel = new Pixel(color[0], color[1], color[2]);
                    }
                }
                invader.setPixel(w, h, pixel);
            }
        }
        invaderLock.lock();
        invaders.add(invader);
        invaderLock.unlock();
    }

    private List<Float[]> getRandomColors() {
        final List<Float[]> colors = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            final Float[] randomColor = {random(0, 255), random(0, 255), random(0, 255)};
            colors.add(randomColor);
        }
        return colors;
    }

    private void cleanUpMissedInvader() {
        final List<Invader> toRemove = new ArrayList<>();
        for (Invader invader : invaders) {
            if (invader.getY() > height) {
                toRemove.add(invader);
            }
        }
        if (!toRemove.isEmpty()) {
            invaderLock.lock();
            invaders.removeAll(toRemove);
            currentScore--;
            invaderLock.unlock();
        }
    }

    private void drawInvaders() {
        invaderLock.lock();
        for (Invader invader : invaders) {
            final Pixel[][] invaderPixels = invader.getPixels();
            final float xPosition = invader.getX();
            final float yPosition = invader.getY();
            if (yPosition > height) {
                invaders.remove(invader);
            }
            loadPixels();
            for (int w = 0; w < invaderPixels.length; w++) {
                for (int h = 0; h < invaderPixels[w].length; h++) {
                    final Pixel p = invaderPixels[w][h];
                    final int pixelColor = color(p.getR(), p.getG(), p.getB());
                    int index = (int) (xPosition + w) + (int) (yPosition + h) * width;
                    if (index >= 0 && index < pixels.length) {
                        pixels[index] = pixelColor;
                    }
                }
            }
            updatePixels();
            invader.setY((invader.getY() + invader.getVelocity()));
        }
        invaderLock.unlock();
    }

    private void shoot(final int x, final int y) {
        List<Invader> toRemove = invaders.stream().filter(invader -> invader.isClickedOn(x, y)).toList();
        if (!toRemove.isEmpty()) {
            invaderLock.lock();
            invaders.removeAll(toRemove);
            currentScore++;
            invaderLock.unlock();
        }
    }

    private void displayScore() {
        textAlign(RIGHT, BOTTOM);
        textSize(24);
        text("Score: " + currentScore, width - 10, height - 10);
    }
}