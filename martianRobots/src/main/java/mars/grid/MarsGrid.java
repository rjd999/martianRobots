package mars.grid;

import mars.communications.StatusReporter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MarsGrid implements GridControl,
                                 GridUse {
    private static final int MAX_GRID_INDEX = 50;

    private final StatusReporter reporter;

    private GridSquare[][] grid          = null;
    private int            gridWidth     = 0;
    private int            gridHeight    = 0;
    private Position       robotPosition = null;

    @Autowired
    public MarsGrid(final StatusReporter reporter) {
        this.reporter = reporter;
    }

    @Override
    public boolean initialise(final Coordinates upperRight) {
        boolean status = false;

        if (upperRight.getX() > MAX_GRID_INDEX)
            reporter.reportFault(getClass().getSimpleName(), "invalid X dimension (> " + MAX_GRID_INDEX + ") provided for grid size: " + upperRight.getX());
        else if (upperRight.getY() > MAX_GRID_INDEX)
            reporter.reportFault(getClass().getSimpleName(), "invalid Y dimension (> " + MAX_GRID_INDEX + ") provided for grid size: " + upperRight.getY());
        else {
            status     = true;
            gridWidth  = upperRight.getX() + 1;
            gridHeight = upperRight.getY() + 1;
            grid       = new GridSquare[gridHeight][gridWidth];

            for (int y = 0; y < gridHeight; y++)
                for (int x = 0; x < gridWidth; x++)
                    grid[y][x] = new GridSquare();
        }

        return status;
    }

    @Override
    public boolean setStartLocation(final Position position) {
        boolean status = false;

        if (position != null)
            if ((position.getCoordinates() != null) && (position.getOrientation() != null)) {
                robotPosition = position;
                status = true;
            }

        return status;
    }

    @Override
    public Position move(final int distance) {
        Position result = null;

        if (robotPosition != null) {
            GridSquare gridSquare = grid[robotPosition.getCoordinates().getY()][robotPosition.getCoordinates().getX()];

            if (gridSquare.hasScent(robotPosition.getOrientation()))
                result = robotPosition;
            else {
                int x = robotPosition.getCoordinates().getX();
                int y = robotPosition.getCoordinates().getY();

                switch (robotPosition.getOrientation()) {
                    case EAST:
                        x += distance;
                        break;

                    case WEST:
                        x -= distance;
                        break;

                    case SOUTH:
                        y -= distance;
                        break;

                    case NORTH:
                        y += distance;
                        break;
                }

                if ((x < 0) || (x >= gridWidth)) {
                    x = Math.min(Math.max(0, x), gridWidth - 1);

                    if (x != robotPosition.getCoordinates().getX())
                        gridSquare = grid[y][x];

                    gridSquare.setScent(robotPosition.getOrientation());
                }
                else if ((y < 0) || (y >= gridHeight)) {
                    y = Math.min(Math.max(0, y), gridHeight - 1);

                    if (y != robotPosition.getCoordinates().getY())
                        gridSquare = grid[y][x];

                    gridSquare.setScent(robotPosition.getOrientation());
                }
                else {
                    robotPosition = new Position(new Coordinates(x, y), robotPosition.getOrientation());
                    result        = robotPosition;
                }
            }
        }

        return result;
    }

    @Override
    public Position reorientate(final boolean clockwise) {
        Position result = null;

        if (robotPosition != null) {
            Direction orientation = Direction.turn90(robotPosition.getOrientation(), clockwise);

            robotPosition = new Position(robotPosition.getCoordinates(), orientation);
            result        = robotPosition;
        }

        return result;
    }

    private static class GridSquare {
        private final boolean[] scents = new boolean[Direction.values().length];

        public boolean hasScent(final Direction direction) {
            return scents[direction.getZeroBasedIndex()];
        }

        public void setScent(final Direction direction) {
            scents[direction.getZeroBasedIndex()] = true;
        }
    }
}
