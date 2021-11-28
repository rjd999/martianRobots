package mars.communications;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class MarsCommsTest {
    @Test(expected = Exception.class)
    public void testNullCommandFileNameThrowsException() {
        new MarsComms(null);
    }

    @Test(expected = Exception.class)
    public void testInvalidCommandFileNameThrowsException() {
        new MarsComms("xyzzy.cmd");
    }

    @Test
    public void testValidCommandFileNameThrowsNoException() {
        MarsComms comms = new MarsComms("/martianRobots-comms-test.cmd");
        assertNotNull(comms);
    }

    @Test
    public void testReadingValidFileReturnsExpectedResults() {
        String line;

        MarsComms comms = new MarsComms("/martianRobots-comms-test.cmd");
        assertNotNull(comms);

        assertTrue(comms.hasIncoming());
        line = comms.receiveMessage();
        assertNotNull(line);
        assertEquals("line 1", line);

        assertTrue(comms.hasIncoming());
        line = comms.receiveMessage();
        assertNotNull(line);
        assertEquals("line 2", line);

        assertTrue(comms.hasIncoming());
        line = comms.receiveMessage();
        assertNotNull(line);
        assertEquals("line 3", line);

        assertTrue(comms.hasIncoming());
        line = comms.receiveMessage();
        assertNotNull(line);
        assertEquals("line 4", line);

        assertTrue(comms.hasIncoming());
        line = comms.receiveMessage();
        assertNotNull(line);
        assertEquals("line 5", line);

        assertFalse(comms.hasIncoming());
        line = comms.receiveMessage();
        assertNull(line);
    }
}
