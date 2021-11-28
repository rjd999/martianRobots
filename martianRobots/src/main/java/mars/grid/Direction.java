package mars.grid;

public enum Direction {
    // Add new directions here in clockwise order to expand directional possibilities (e.g. 'NORTHWEST', 'SOUTHEAST')
    NORTH("N"),
    EAST("E"),
    SOUTH("S"),
    WEST("W");

    private static final int directionMin = 0;
    private static final int directionMax = Direction.values().length - 1;

    private final String abbreviation;

    public String getAbbreviation() {
        return abbreviation;
    }

    private Direction(final String abbreviation) {
        this.abbreviation = abbreviation;
    }

    // NOTE: This method is reliant upon the current implementation of the java Enum method ordinal() returning the zero-based declaration-order index of an enum.
    //       Should that change, then probably just add a constructor arg taking an index number, and have the enum declarations pass a sequential index number starting from zero
    public int getZeroBasedIndex() {
        return ordinal();
    }

    public static Direction fromAbbreviation(final String abbreviation) {
        Direction result = null;

        for (final Direction direction : Direction.values())
            if (direction.abbreviation.equals(abbreviation)) {
                result = direction;
                break;
            }

        return result;
    }

    public static Direction turn90(final Direction currentDirection,
                                   final boolean   clockwise) {
        Direction result = null;

        if (currentDirection != null) {
            int indexAdd = (clockwise ? 1 : -1);
            int newIndex = currentDirection.getZeroBasedIndex() + indexAdd;

            if (newIndex < directionMin)
                newIndex = directionMax;
            else if (newIndex > directionMax)
                newIndex = directionMin;

            result = Direction.values()[newIndex];
        }

        return result;
    }
}
