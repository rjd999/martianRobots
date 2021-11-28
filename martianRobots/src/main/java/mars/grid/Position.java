package mars.grid;

public final class Position {
    private final Coordinates coordinates;
    private final Direction   orientation;

    public Position(final Coordinates coordinates,
                    final Direction   orientation) {
        this.coordinates = coordinates;
        this.orientation = orientation;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public Direction getOrientation() {
        return orientation;
    }
}
