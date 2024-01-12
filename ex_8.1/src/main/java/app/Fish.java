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
    private final float height;
    private final float maxForce;
    private final float maxSpeed;

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

        sep.mult(2.5f);
        ali.mult(1.0f);
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
    }

    private PVector seek(final PVector target) {
        final PVector desired = PVector.sub(target, position);
        desired.setMag(maxSpeed);
        PVector steer = PVector.sub(desired, velocity);
        steer.limit(maxForce);
        return steer;
    }

    public void draw() {
        float theta = velocity.heading() + radians(180);
        this.fishAgent.fill(0, 35, 51);
        this.fishAgent.noStroke();
        this.fishAgent.pushMatrix();
        this.fishAgent.translate(position.x, position.y);
        this.fishAgent.rotate(theta);
        this.fishAgent.shape(getFishShape());
        this.fishAgent.popMatrix();
    }

    private PShape getFishShape() {
        final PShape body = getBody();
        final PShape fin = getFin();

        final PShape fishShape = this.fishAgent.createShape(GROUP);
        fishShape.fill(0, 40, 35);
        fishShape.addChild(body);
        fishShape.addChild(fin);
        return fishShape;
    }

    private PShape getBody() {
        final PShape body = this.fishAgent.createShape();
        body.beginShape();
        for (float rad = 0.0f; rad <= PI; rad += PI / 100) {
            float xPos = rad / TWO_PI * this.height;
            float yPos = this.width * sin(rad);
            body.vertex(xPos, yPos);
        }
        for (float rad = PI; rad >= 0.0f; rad -= PI / 100) {
            float xPos = rad / TWO_PI * this.height;
            float yPos = this.width * (-sin(rad));
            body.vertex(xPos, yPos);
        }
        body.endShape();
        return body;
    }

    private PShape getFin() {
        final PShape fin = this.fishAgent.createShape();
        fin.beginShape();
        for (float rad = PI; rad <= PI + QUARTER_PI; rad += (PI + QUARTER_PI) / 50) {
            float xPos = rad / TWO_PI * this.height - PI / 8;
            float yPos = this.width * sin(rad + PI / 8);
            fin.vertex(xPos, yPos);
        }
        for (float rad = PI + QUARTER_PI; rad >= PI; rad -= (PI + QUARTER_PI) / 50) {
            float xPos = rad / TWO_PI * this.height - PI / 8;
            float yPos = this.width * (-sin(rad + PI / 8));
            fin.vertex(xPos, yPos);
        }
        fin.endShape();
        return fin;
    }


    private void borders() {
        if (this.position.x < -this.width) {
            this.position.x = this.fishAgent.width + this.width;
        }
        if (this.position.y < -this.height) {
            this.position.y = this.fishAgent.height + this.height;
        }
        if (this.position.x > this.fishAgent.width + this.width) {
            this.position.x = -this.width;
        }
        if (this.position.y > this.fishAgent.height + this.height) {
            this.position.y = -this.height;
        }
    }

    private PVector separate() {
        final float desiredDist = 25.0f;
        final PVector steer = new PVector(0, 0, 0);
        int count = 0;

        for (Fish f : this.fishList) {
            final float dist = PVector.dist(this.position, f.getPosition());
            if ((dist > 0) && (dist < desiredDist)) {
                final PVector diff = PVector.sub(this.position, f.getPosition());
                diff.normalize();
                diff.div(dist);
                steer.add(diff);
                count++;
            }
        }

        if (count > 0) {
            steer.div((float) count);
        }


        if (steer.mag() > 0) {
            steer.setMag(this.maxSpeed);
            steer.sub(this.velocity);
            steer.limit(this.maxForce);
        }
        return steer;
    }

    final PVector align() {
        final float neighborDist = 50;
        final PVector sum = new PVector(0, 0);
        int count = 0;
        for (Fish f : this.fishList) {
            float dist = PVector.dist(this.position, f.getPosition());
            if ((dist > 0) && (dist < neighborDist)) {
                sum.add(f.getVelocity());
                count++;
            }
        }
        if (count > 0) {
            sum.div((float) count);

            // Implement Reynolds: Steering = Desired - Velocity
            sum.setMag(this.maxSpeed);
            PVector steer = PVector.sub(sum, velocity);
            steer.limit(this.maxForce);
            return steer;
        } else {
            return new PVector(0, 0);
        }
    }

    private PVector cohesion() {
        final float neighborDist = 50;
        PVector sum = new PVector(0, 0);
        int count = 0;
        for (Fish f : this.fishList) {
            float dist = PVector.dist(this.position, f.getPosition());
            if ((dist > 0) && (dist < neighborDist)) {
                sum.add(f.getPosition());
                count++;
            }
        }
        if (count > 0) {
            sum.div(count);
            return this.seek(sum);
        } else {
            return new PVector(0, 0);
        }
    }
}