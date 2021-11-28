package mars.robot;

import mars.communications.MessageReceiver;
import mars.communications.MessageTransmitter;
import mars.communications.StatusReporter;
import mars.grid.Coordinates;
import mars.grid.Direction;
import mars.grid.GridUse;
import mars.grid.Position;
import mars.robot.instruction.Instruction;
import mars.robot.instruction.InstructionMovement;
import mars.robot.instruction.InstructionReorientation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Scope("prototype")
public class MarsRobot implements Robot {
    private final GridUse                     grid;
    private final MessageReceiver             receiver;
    private final StatusReporter              reporter;
    private final MessageTransmitter          transmitter;
    private final Map<Character, Instruction> instructions;

    @Autowired
    public MarsRobot(final MessageTransmitter transmitter,
                     final MessageReceiver    receiver,
                     final StatusReporter     reporter,
                     final GridUse            grid,
                     final Instruction[] instructions) {
        this.grid         = grid;
        this.receiver     = receiver;
        this.reporter     = reporter;
        this.transmitter  = transmitter;
        this.instructions = loadOperations(instructions);
    }

    @Override
    public void explore() {
        Position position = getInitialPosition();

        if (position != null)
            if (grid.setStartLocation(position)) {
                List<Instruction> instructions = getInstructions();

                if (instructions != null) {
                    boolean  lost         = false;
                    Position lastPosition = position;

                    for (final Instruction instruction : instructions) {
                        if (instruction instanceof InstructionReorientation)
                            position = grid.reorientate(((InstructionReorientation)instruction).turnClockwise());
                        else if (instruction instanceof InstructionMovement) {
                            position = grid.move(((InstructionMovement) instruction).getDistance());

                            if (position == null) {
                                lost     = true;
                                position = lastPosition;
                            }
                            else
                                lastPosition = position;
                        }
                    }

                    reportFinalPosition(position, lost);
                }
            }
    }

    private Position getInitialPosition() {
        Position position = null;

        if (receiver.hasIncoming()) {
            String   message  = receiver.receiveMessage();
            String[] elements = message.split("\\s+");

            if (elements.length != 3)
                reporter.reportFault(getClass().getSimpleName(), "invalid position message received: '" + message + "'");
            else {
                Coordinates coordinates = getInitialCoordinates(elements[0], elements[1]);
                Direction   orientation = getInitialOrientation(elements[2]);

                if ((coordinates != null) && (orientation != null))
                    position = new Position(coordinates, orientation);
            }
        }

        return position;
    }

    private Coordinates getInitialCoordinates(final String x,
                                              final String y) {
        Coordinates coordinates = null;

        try {
            coordinates = new Coordinates(x, y);
        }
        catch (final Exception exception) {
            reporter.reportFault(getClass().getSimpleName(), "invalid coordinates provided for position: x=" + x + ", y=" + y);
        }

        return coordinates;
    }

    private Direction getInitialOrientation(final String direction) {
        Direction orientation = Direction.fromAbbreviation(direction);

        if (orientation == null)
            reporter.reportFault(getClass().getSimpleName(), "invalid orientation provided for position: direction=" + direction);

        return orientation;
    }

    private List<Instruction> getInstructions() {
        List<Instruction> instructions = null;

        if (receiver.hasIncoming()) {
            String message = receiver.receiveMessage();

            instructions = new ArrayList<>();

            for (final char c : message.trim().toCharArray()) {
                Instruction instruction = this.instructions.get(c);

                if (instruction == null)
                    reporter.reportFault(getClass().getSimpleName(), "invalid instruction received: '" + c + "' - ignored");
                else
                    instructions.add(instruction);
            }
        }

        return instructions;
    }

    private void reportFinalPosition(final Position position,
                                     final boolean  isLost) {
        String message = String.format("%d %d %s %s", position.getCoordinates().getX(), position.getCoordinates().getY(), position.getOrientation().getAbbreviation(), isLost ? "LOST" : "");

        transmitter.transmitMessage(message);
    }

    private static Map<Character, Instruction> loadOperations(final Instruction[] instructions) {
        Map<Character, Instruction> operationMap = new HashMap<>();

        for (final Instruction instruction : instructions)
            operationMap.put(instruction.getCode(), instruction);

        return operationMap;
    }
}
