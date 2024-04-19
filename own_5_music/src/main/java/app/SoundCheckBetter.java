package app;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import processing.core.PApplet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class SoundCheckBetter extends PApplet {
    private static final Logger LOG = LoggerFactory.getLogger(SoundCheckBetter.class);

    private final List<Player> soundPlayers = new ArrayList<>();
    private int currentSoundIndex = 0;

    @Override
    public void settings() {
        size(512, 512);
    }

    @Override
    public void setup() {
        frameRate(60);
        for (int i = 1; i <= 24; i++) {
            final String fileName = "key" + nf(i, 2) + ".mp3";
            try {
                final File file = new File(getClass().getClassLoader().getResource(fileName).getFile());
                final FileInputStream fis = new FileInputStream(file);
                soundPlayers.add(new Player(fis));
            } catch (FileNotFoundException | JavaLayerException e) {
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
            try {
                playSound();
            } catch (JavaLayerException e) {
                LOG.error("Could not play sound", e);
            }
        }
    }

    private void playSound() throws JavaLayerException {
        updateSoundIndex();
        final int copy = currentSoundIndex;
        CompletableFuture.runAsync(() -> {
            LOG.info("Playing soundfile at index '{}'", currentSoundIndex);
            try {
                soundPlayers.get(copy).play();
                LOG.info("Played sound at index '{}'", currentSoundIndex);
            } catch (JavaLayerException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void updateSoundIndex() {
        currentSoundIndex += 1;
        if (soundPlayers.size() <= currentSoundIndex) {
            currentSoundIndex = 0;
        }
    }
}
