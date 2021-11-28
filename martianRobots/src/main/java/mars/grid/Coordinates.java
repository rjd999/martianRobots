package mars.grid;

public final class Coordinates {
    private final int x;
    private final int y;

    public Coordinates(final String x,
                       final String y) {
        this(Integer.parseInt(x), Integer.parseInt(y));
    }

    public Coordinates(final int x,
                       final int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
