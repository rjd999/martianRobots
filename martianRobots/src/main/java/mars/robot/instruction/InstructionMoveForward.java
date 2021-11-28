package mars.robot.instruction;

import org.springframework.stereotype.Component;

@Component
public class InstructionMoveForward implements InstructionMovement {
    private static final Character OP_CODE = 'F';

    @Override
    public Character getCode() {
        return OP_CODE;
    }

    @Override
    public int getDistance() {
        return 1;
    }
}
