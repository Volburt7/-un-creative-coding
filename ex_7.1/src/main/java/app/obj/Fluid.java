package app.obj;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Fluid {
    private float x, y;
    private int lifeSpan;

    public void update() {
        this.lifeSpan--;
    }

    public boolean isActive() {
        return this.lifeSpan >= 0;
    }
}
