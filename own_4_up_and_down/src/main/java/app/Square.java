package app;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import processing.core.PVector;

@Getter
@Setter
@Builder
public class Square {
    private final PVector position;
    private final boolean goUp;
    private int[][] pixels;
}
