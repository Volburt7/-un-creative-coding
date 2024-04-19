package app;

import ddf.minim.AudioPlayer;
import ddf.minim.Minim;
import processing.core.PApplet;
import processing.event.KeyEvent;

public class SoundCheck extends PApplet {

    Minim minim;
    AudioPlayer[] notes = new AudioPlayer[2];
    float[] tunings = {1.0f, 1.5f}; // Adjust tunings for each note

    @Override
    public void settings() {
        size(512, 512);
    }

    @Override
    public void setup() {
        frameRate(60);

        minim = new Minim(this);

        notes[0] = minim.loadFile("nylon-guitar-single-note.mp3");
        notes[1] = minim.loadFile("guitar-drum-single.mp3");
        notes[0].loop();
    }

    @Override
    public void draw() {
        background(0);
        fill(255);
        if (frameCount % 120 == 0) {
            System.out.println("playing");
            playNextNote();
        }
    }

    @Override
    public void keyPressed(final KeyEvent keyEvent) {
        if (keyEvent.getKeyCode() == 32) { // Space key
            playNextNote();
        }
    }

    void playNextNote() {
        int index = 0; // Play the first note
        float tuning = tunings[index]; // Get tuning for the note
        notes[index].setBalance(tuning); // Apply tuning
        notes[index].loop(); // Loop the note continuously
        println("Note " + index + " with tuning " + tuning + " played.");
    }
}
