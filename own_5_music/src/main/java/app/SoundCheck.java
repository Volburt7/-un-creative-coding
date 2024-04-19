package app;

import ddf.minim.AudioPlayer;
import ddf.minim.Minim;
import processing.core.PApplet;
import processing.event.KeyEvent;


/*
* Piano Notes von https://archive.org/details/24-piano-keys
* */
public class SoundCheck extends PApplet {

    Minim minim;
    AudioPlayer[] notes = new AudioPlayer[24];
    float[] pitches = {1.0f, 1.1f, 1.2f, 1.3f, 1.4f, 1.5f, 1.6f, 1.7f};
    int currentNoteIndex = 0;

    @Override
    public void settings() {
        size(512, 512);
    }

    @Override
    public void setup() {
        frameRate(60);

        minim = new Minim(this);

        for (int i = 0; i < notes.length; i++) {
            notes[i] = minim.loadFile("piano-keys/key" + nf(i + 1, 2) + ".mp3");
        }
    }

    @Override
    public void draw() {
        background(0);
    }

    @Override
    public void keyPressed(final KeyEvent keyEvent) {
        if (keyEvent.getKeyCode() == 32) {
            nextNote();
        }
    }

    void nextNote() {
        if (currentNoteIndex > 0) {
            notes[currentNoteIndex - 1].rewind();
        }

        if (currentNoteIndex < notes.length) {
            notes[currentNoteIndex].play();
            currentNoteIndex++;
        } else {
            currentNoteIndex = 0;
        }
    }
}
