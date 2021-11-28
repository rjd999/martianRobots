package mars.robot.instruction;

// NOTE: to define new instructions, simply implement a new class derived from Instruction and then spring will pick it up. It will then be available for use.
//        However, if it does not implement InstructionMovement or InstructionReorientation, code changes will be required in class MarsRobot to handle it.
public interface Instruction {
    Character getCode();
}
