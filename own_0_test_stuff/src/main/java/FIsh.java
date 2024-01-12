import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import processing.core.PApplet;
import processing.core.PShape;
import processing.core.PVector;

import java.util.List;

import static processing.core.PApplet.sin;
import static processing.core.PConstants.*;

@Getter
@Setter
@Builder
public class FIsh {

    private final PApplet fishAgent;
    private final List<FIsh> fishList;
    private final PVector position;
    private final PVector velocity;
    private final PVector acceleration;
    private final float width;
    private final float length;
    private final float maxForce;
    private final float maxSpeed;

    private float finRotation = 0;
    private boolean finDirection = false;

    public void moveFin() {
        if (this.finRotation >= 35) {
            this.finDirection = true;
        } else if (this.finRotation <= -35) {
            this.finDirection = false;
        }

        if (this.finDirection) {
            this.finRotation -= 1;
        } else {
            this.finRotation += 1;
        }
    }

    public PShape getBody() {
        final PShape body = this.fishAgent.createShape();
        body.beginShape();
        body.fill(0, 30, 35);
        for (float rad = 0.0f; rad <= PI; rad += PI / 100) {
            float xPos = rad / TWO_PI * this.length;
            float yPos = this.width * sin(rad);
            body.vertex(xPos, yPos);
        }
        for (float rad = PI; rad >= 0.0f; rad -= PI / 100) {
            float xPos = rad / TWO_PI * this.length;
            float yPos = this.width * (-sin(rad));
            body.vertex(xPos, yPos);
        }
        body.endShape();
        return body;
    }

    public PShape getFin() {
        final PShape fin = this.fishAgent.createShape();
        fin.beginShape();
        fin.fill(0, 30, 35);
        for (float rad = PI; rad <= PI + QUARTER_PI; rad += (PI + QUARTER_PI) / 50) {
            float xPos = rad / TWO_PI * this.length - this.length / 2;
            float yPos = this.width * sin(rad);
            fin.vertex(xPos, yPos);
        }
        for (float rad = PI + QUARTER_PI; rad >= PI; rad -= (PI + QUARTER_PI) / 50) {
            float xPos = rad / TWO_PI * this.length - this.length / 2;
            float yPos = this.width * (-sin(rad));
            fin.vertex(xPos, yPos);
        }
        fin.endShape();
        return fin;
    }
}
