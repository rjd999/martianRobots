package mars.robot.instruction;

import org.springframework.stereotype.Component;

@Component
public class InstructionTurnLeft implements InstructionReorientation {
    private static final Character OP_CODE = 'L';

    @Override
    public Character getCode() {
        return OP_CODE;
    }

    @Override
    public boolean turnClockwise() {
        return false;
    }
}
