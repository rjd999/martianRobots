package mars.grid;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class DirectionTest {
    @Test
    public void testInvalidAbbreviationsReturnNull() {
        Direction direction;

        direction = Direction.fromAbbreviation("");
        assertNull(direction);

        direction = Direction.fromAbbreviation("hello");
        assertNull(direction);

        direction = Direction.fromAbbreviation("z");
        assertNull(direction);

        direction = Direction.fromAbbreviation("s");
        assertNull(direction);

        direction = Direction.fromAbbreviation("e");
        assertNull(direction);

        direction = Direction.fromAbbreviation("w");
        assertNull(direction);

        direction = Direction.fromAbbreviation("n");
        assertNull(direction);
    }

    @Test
    public void testValidAbbreviationsAreInterpretedCorrectly() {
        Direction direction;

        direction = Direction.fromAbbreviation("S");
        assertNotNull(direction);
        assertEquals(Direction.SOUTH, direction);

        direction = Direction.fromAbbreviation("E");
        assertNotNull(direction);
        assertEquals(Direction.EAST, direction);

        direction = Direction.fromAbbreviation("W");
        assertNotNull(direction);
        assertEquals(Direction.WEST, direction);

        direction = Direction.fromAbbreviation("N");
        assertNotNull(direction);
        assertEquals(Direction.NORTH, direction);
    }

    @Test
    public void testTurn90WithInvalidParametersReturnsNull() {
        Direction result = Direction.turn90(null, true);
        assertNull(result);
    }

    @Test
    public void testTurn90WithValidParametersCalculatesExpectedResults() {
        Direction result;

        result = Direction.turn90(Direction.SOUTH, false);
        assertNotNull(result);
        assertEquals(Direction.EAST, result);

        result = Direction.turn90(Direction.SOUTH, true);
        assertNotNull(result);
        assertEquals(Direction.WEST, result);

        result = Direction.turn90(Direction.EAST, false);
        assertNotNull(result);
        assertEquals(Direction.NORTH, result);

        result = Direction.turn90(Direction.EAST, true);
        assertNotNull(result);
        assertEquals(Direction.SOUTH, result);

        result = Direction.turn90(Direction.WEST, false);
        assertNotNull(result);
        assertEquals(Direction.SOUTH, result);

        result = Direction.turn90(Direction.WEST, true);
        assertNotNull(result);
        assertEquals(Direction.NORTH, result);

        result = Direction.turn90(Direction.NORTH, false);
        assertNotNull(result);
        assertEquals(Direction.WEST, result);

        result = Direction.turn90(Direction.NORTH, true);
        assertNotNull(result);
        assertEquals(Direction.EAST, result);
    }
}
