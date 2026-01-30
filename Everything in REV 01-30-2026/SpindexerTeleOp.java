package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class SpindexerTeleOp {

    private final DcMotor spindexerMotor;

    public SpindexerTeleOp(HardwareMap hardwareMap) {
        spindexerMotor = hardwareMap.get(DcMotor.class, "spindexer");
        spindexerMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        spindexerMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        spindexerMotor.setDirection(DcMotor.Direction.FORWARD);
    }

    
    public void in() {
        spindexerMotor.setPower(1.0);
    }

    public void out() {
        spindexerMotor.setPower(-1.0);
    }

  
    public void stop() {
        spindexerMotor.setPower(0.0);
    }
}

