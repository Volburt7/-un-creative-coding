package app.enums;

public enum ScrollDirection {
    OUT(1),
    IN(-1);

    final int value;

    ScrollDirection(final int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }
}
