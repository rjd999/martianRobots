package mars.robot.instruction;

import org.springframework.stereotype.Component;

@Component
public class InstructionTurnRight implements InstructionReorientation {
    private static final Character OP_CODE = 'R';

    @Override
    public Character getCode() {
        return OP_CODE;
    }

    @Override
    public boolean turnClockwise() {
        return true;
    }
}
