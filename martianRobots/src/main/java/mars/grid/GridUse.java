package mars.grid;

public interface GridUse {
    boolean setStartLocation(final Position position);

    Position move(final int distance);

    Position reorientate(final boolean clockwise);
}
