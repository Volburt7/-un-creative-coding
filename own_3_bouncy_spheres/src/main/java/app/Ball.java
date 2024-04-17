package app;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import processing.core.PVector;

@Getter
@Setter
@Builder
public class Ball {
    private PVector vPos;
    private PVector vDir;
    private float radius;
    private int color;
}
