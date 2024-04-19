package app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sound.sampled.*;
import java.io.IOException;
import java.io.InputStream;

public class MySoundPlayer implements LineListener {
    private static final Logger LOG = LoggerFactory.getLogger(MySoundPlayer.class);

    final InputStream inputStream;
    final AudioInputStream audioStream;
    final Clip audioClip;

    public MySoundPlayer(final String fileName) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        LOG.info("Loading '{}'", fileName);
        this.inputStream = getClass().getClassLoader().getResourceAsStream(fileName);
        this.audioStream = AudioSystem.getAudioInputStream(inputStream);

        final DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioStream.getFormat());

        this.audioClip = (Clip) AudioSystem.getLine(info);
        this.audioClip.addLineListener(this);
        this.audioClip.open(audioStream);
        LOG.info("Loading complete.");
    }

    @Override
    public void update(LineEvent event) {
        if (LineEvent.Type.START == event.getType()) {
            System.out.println("Playback started.");
        } else if (LineEvent.Type.STOP == event.getType()) {
            System.out.println("Playback completed.");
        }
    }

    // Closing would actually matter if we wanted the program to continue running but doesn't matter here...
    public void close() throws Exception {
        this.audioClip.close();
        this.audioStream.close();
        this.inputStream.close();
    }

    public void play() {
        audioClip.start();
    }
}