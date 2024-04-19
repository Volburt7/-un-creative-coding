package app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sound.sampled.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class MySoundPlayer implements LineListener {
    private static final Logger LOG = LoggerFactory.getLogger(MySoundPlayer.class);

    private final String fileName;
    private final InputStream inputStream;
    private final AudioInputStream audioStream;
    private final Clip audioClip;

    public MySoundPlayer(final String fileName) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        LOG.info("Loading '{}'", fileName);
        this.fileName = fileName;
        this.inputStream = getClass().getClassLoader().getResourceAsStream(fileName);
        this.audioStream = AudioSystem.getAudioInputStream(Objects.requireNonNull(inputStream));
        this.audioClip = AudioSystem.getClip();
        this.audioClip.addLineListener(this);
        this.audioClip.open(audioStream);
        LOG.info("Loading complete.");
    }

    @Override
    public void update(LineEvent event) {
        if (LineEvent.Type.START == event.getType()) {
            LOG.debug("Playback started for file '{}'", fileName);
        } else if (LineEvent.Type.STOP == event.getType()) {
            LOG.debug("Playback stopped for file '{}'", fileName);
        }
    }

    public void play() {
        audioClip.setFramePosition(0);
        audioClip.start();
    }

    public void close() throws IOException {
        this.audioClip.close();
        this.audioStream.close();
        this.inputStream.close();
    }
}