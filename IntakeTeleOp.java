package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

/**
 * Subsystem class that controls the intake motor.
 */
public class IntakeTeleOp {

    private final DcMotor intakeMotor;

    public IntakeTeleOp(HardwareMap hardwareMap) {
        intakeMotor = hardwareMap.get(DcMotor.class, "intake");
        intakeMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        intakeMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        intakeMotor.setDirection(DcMotor.Direction.FORWARD);
    }

    /** Intake in (collect). */
    public void in() {
        intakeMotor.setPower(1.0);
    }

    /** Intake out (eject). */
    public void out() {
        intakeMotor.setPower(-1.0);
    }

    /** Stop intake. */
    public void stop() {
        intakeMotor.setPower(0.0);
    }
}


