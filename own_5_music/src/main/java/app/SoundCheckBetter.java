package app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import processing.core.PApplet;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SoundCheckBetter extends PApplet {
    private static final Logger LOG = LoggerFactory.getLogger(SoundCheckBetter.class);

    private final List<MySoundPlayer> soundPlayers = new ArrayList<>();
    private int currentSoundIndex = 0;

    @Override
    public void settings() {
        size(512, 512);
    }

    @Override
    public void setup() {
        frameRate(60);
//        for (int i = 1; i <= 24; i++) {
        for (int i = 1; i <= 1; i++) {
            final String fileName = "key" + nf(i, 2) + ".MP3";
            try {
                soundPlayers.add(new MySoundPlayer(fileName));
            } catch (UnsupportedAudioFileException | LineUnavailableException | IOException e) {
                LOG.error("Could not instantiate Soundplayer for file: '{}'", fileName, e);
                exit();
            }
        }
        if (soundPlayers.isEmpty()) {
            LOG.warn("Define some audio files");
            exit();
        }
    }

    @Override
    public void draw() {
        background(0);
        fill(255);
        if (frameCount % 60 == 0) {
            playSound();
        }
    }

    private void playSound() {
        soundPlayers.get(currentSoundIndex).play();

        currentSoundIndex += 1;
        if (soundPlayers.size() <= currentSoundIndex) {
            currentSoundIndex = 0;
        }
    }
}
