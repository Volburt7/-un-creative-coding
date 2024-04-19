package app;

// TODO: delete

// Required this thing
//    <dependencies>
//        <dependency>
//            <groupId>net.compartmental.code</groupId>
//            <artifactId>minim</artifactId>
//            <version>2.2.2</version>
//        </dependency>
//    </dependencies>

//public class SoundCheckWithBadLibrary extends PApplet {
//    private static final String SOUNDFILE_NAME = "guitar-drum-single.mp3";
//
//    private Minim minim;
//
//    @Override
//    public void settings() {
//        size(512, 512);
//    }
//
//    @Override
//    public void setup() {
//        frameRate(60);
//        minim = new Minim(this);
//    }
//
//    @Override
//    public void draw() {
//        background(0);
//        fill(255);
//        if (frameCount % 60 == 0) {
//            playSound();
//        }
//    }
//
//    @Override
//    public void keyPressed(final KeyEvent keyEvent) {
//        if (keyEvent.getKeyCode() == 32) {
//            playSound();
//        }
//    }
//
//    void playSound() {
//        minim.loadFile(SOUNDFILE_NAME).play();
//    }
//}
