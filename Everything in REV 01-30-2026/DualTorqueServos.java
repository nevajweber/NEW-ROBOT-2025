package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Controls two torque servos (e.g. arm/lift), kept synchronized.
 */
public class DualTorqueServos {

    private final Servo left;
    private final Servo right;

    // Tune these to your mechanism
    private static final double POS_LOW  = 0.65; // down
    private static final double POS_STOW = 0.90; // mid
    private static final double POS_HIGH = 0.90;// up

    public DualTorqueServos(HardwareMap hardwareMap) {
        left  = hardwareMap.get(Servo.class, "leftTorque");
        right = hardwareMap.get(Servo.class, "rightTorque");

        // If they are mirrored physically, reverse one:
        left.setDirection(Servo.Direction.FORWARD);
        right.setDirection(Servo.Direction.REVERSE);

        toStow();
    }

    public void toLow() {
        setBoth(POS_LOW);
    }

    public void toStow() {
        setBoth(POS_STOW);
    }

    public void toHigh() {
        setBoth(POS_HIGH);
    }

    public void setBoth(double pos) {
        left.setPosition(pos);
        right.setPosition(pos);
    }
}
