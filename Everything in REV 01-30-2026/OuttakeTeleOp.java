package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

/**
 * Subsystem class that controls the OUTTAKE motor.
 */
public class OuttakeTeleOp {

    private final DcMotor outtakeMotor;

    public OuttakeTeleOp(HardwareMap hardwareMap) {
        // MUST match RC configuration
        outtakeMotor = hardwareMap.get(DcMotor.class, "outtake");

        outtakeMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        outtakeMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        outtakeMotor.setDirection(DcMotor.Direction.FORWARD);
    }

    /** Move outtake mechanism one way (e.g. up). */
    public void in() {
        outtakeMotor.setPower(1.0);
    }

    /** Move outtake mechanism opposite way (e.g. down). */
    public void out() {
        outtakeMotor.setPower(-1.0);
    }

    /** Stop outtake. */
    public void stop() {
        outtakeMotor.setPower(0.0);
    }
}
