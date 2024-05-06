package app;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import processing.core.PApplet;
import processing.core.PShape;
import processing.core.PVector;

import java.util.List;

import static processing.core.PApplet.*;

/*
 * This class is heavily copied from
 * https://processing.org/examples/flocking.html
 * */

@Getter
@Setter
@Builder
public class Fish {
    private final PApplet fishAgent;
    private final List<Fish> fishList;

    private final PVector position;
    private final PVector velocity;
    private final PVector acceleration;

    private final float width;
    private final float length;
    private final float maxForce;
    private final float maxSpeed;

    private float finRotation;
    private boolean finDirection;

    public void run() {
        this.flock();
        this.update();
        this.borders();
        this.draw();
    }

    private void flock() {
        final PVector sep = this.separate();
        final PVector ali = this.align();
        final PVector coh = this.cohesion();

        sep.mult(3f);
        ali.mult(1.5f);
        coh.mult(1.0f);

        acceleration.add(sep);
        acceleration.add(ali);
        acceleration.add(coh);
    }

    private void update() {
        velocity.add(acceleration);
        velocity.limit(this.maxSpeed);
        position.add(velocity);
        acceleration.mult(0);
        moveFin();
    }

    public void moveFin() {
        if (this.finRotation >= 45) {
            this.finDirection = true;
        } else if (this.finRotation <= -45) {
            this.finDirection = false;
        }

        if (this.finDirection) {
            this.finRotation -= velocity.mag();
        } else {
            this.finRotation += velocity.mag();
        }
    }

    public void draw() {
        this.fishAgent.fill(0, 35, 51);
        this.fishAgent.noStroke();

        this.fishAgent.pushMatrix();
        this.fishAgent.translate(position.x, position.y);
        this.fishAgent.rotate(PI + velocity.heading());
        this.fishAgent.shape(this.getBody());

        this.fishAgent.pushMatrix();
        this.fishAgent.translate(this.length / 2, 0);
        this.fishAgent.rotate(radians(this.finRotation));
        this.fishAgent.shape(this.getFin());
        this.fishAgent.popMatrix();

        this.fishAgent.popMatrix();
    }

    private PShape getBody() {
        final PShape body = this.fishAgent.createShape();
        body.beginShape();
        for (float rad = 0.0f; rad < PI; rad += PI / 100) {
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

    private PShape getFin() {
        final PShape fin = this.fishAgent.createShape();
        fin.beginShape();
        for (float rad = PI; rad < PI + QUARTER_PI; rad += (PI + QUARTER_PI) / 50) {
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

    private void borders() {
        if (this.position.x < -this.width) {
            this.position.x = this.fishAgent.width + this.width;
        }
        if (this.position.y < -this.length) {
            this.position.y = this.fishAgent.height + this.length;
        }
        if (this.position.x > this.fishAgent.width + this.width) {
            this.position.x = -this.width;
        }
        if (this.position.y > this.fishAgent.height + this.length) {
            this.position.y = -this.length;
        }
    }

    private PVector separate() {
        final float desiredDist = 25.0f;
        final PVector sum = new PVector(0, 0);
        int numFishInRange = 0;
        for (Fish f : this.fishList) {
            if(this.equals(f)) continue;
            final float dist = PVector.dist(this.position, f.getPosition());
            if (dist != 0 && dist < desiredDist) {
                final PVector diff = PVector.sub(this.position, f.getPosition());
                diff.normalize().div(dist);
                sum.add(diff);
                numFishInRange++;
            }
        }

        if (numFishInRange > 0) {
            sum.div(numFishInRange);
        }

        if (sum.mag() > 0) {
            sum.setMag(this.maxSpeed);
            sum.sub(this.velocity);
            sum.limit(this.maxForce);
        }
        return sum;
    }

    final PVector align() {
        final float neighborDist = 50;
        final PVector sum = new PVector(0, 0);
        int numFishInRange = 0;
        for (Fish f : this.fishList) {
            if(this.equals(f)) continue;
            float dist = PVector.dist(this.position, f.getPosition());
            if (dist < neighborDist) {
                sum.add(f.getVelocity());
                numFishInRange++;
            }
        }
        if (numFishInRange > 0) {
            sum.div(numFishInRange);

            // Implement Reynolds: Steering = Desired - Velocity
            sum.setMag(this.maxSpeed);
            PVector steer = PVector.sub(sum, this.velocity);
            steer.limit(this.maxForce);
            return steer;
        } else {
            return new PVector(0, 0);
        }
    }

    private PVector cohesion() {
        final float neighborDist = 50;
        final PVector sum = new PVector(0, 0);
        int numFishInRange = 0;
        for (Fish f : this.fishList) {
            if(this.equals(f)) continue;
            float dist = PVector.dist(this.position, f.getPosition());
            if (dist < neighborDist) {
                sum.add(f.getPosition());
                numFishInRange++;
            }
        }
        if (numFishInRange > 0) {
            sum.div(numFishInRange);
            return this.seek(sum);
        } else {
            return new PVector(0, 0);
        }
    }

    private PVector seek(final PVector target) {
        final PVector desired = PVector.sub(target, this.position);
        desired.setMag(this.maxSpeed);
        final PVector steer = PVector.sub(desired, this.velocity);
        steer.limit(maxForce);
        return steer;
    }
}