package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Flipper servo for dumping game pieces.
 *
 * Config:
 *  - Name: "myServo"
 *  - Positional servo on a servo port.
 */
public class OuttakeServo {

    private final Servo outtakeServo;

    // Tune these on the robot (0.0 - 1.0)
    private static final double POSITION_INTAKE = 0.10; // down / receive
    private static final double POSITION_DUMP   = 0.80; // up / dump (~90°+)
    private static final double POSITION_STOW   = 0.40; // mid / safe

    public OuttakeServo(HardwareMap hardwareMap) {
        outtakeServo = hardwareMap.get(Servo.class, "myServo");
        outtakeServo.setDirection(Servo.Direction.FORWARD);
        outtakeServo.setPosition(POSITION_STOW);
    }

    public void setIntakePosition() {
        outtakeServo.setPosition(POSITION_INTAKE);
    }

    public void setDumpPosition() {
        outtakeServo.setPosition(POSITION_DUMP);
    }

    public void setStowPosition() {
        outtakeServo.setPosition(POSITION_STOW);
    }

    public void setPosition(double position) {
        outtakeServo.setPosition(position);
    }

    public double getPosition() {
        return outtakeServo.getPosition();
    }
}

