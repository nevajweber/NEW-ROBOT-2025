package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Drive {

    public DcMotor FLW, FRW, BLW, BRW;

    public Drive(HardwareMap hardwareMap) {

        FRW = hardwareMap.get(DcMotor.class, "FRW");
        FLW = hardwareMap.get(DcMotor.class, "FLW");
        BRW = hardwareMap.get(DcMotor.class, "BRW");
        BLW = hardwareMap.get(DcMotor.class, "BLW");

        // Directions (taken from your TeleOp)
        FLW.setDirection(DcMotor.Direction.FORWARD);
        BLW.setDirection(DcMotor.Direction.REVERSE);
        FRW.setDirection(DcMotor.Direction.REVERSE);
        BRW.setDirection(DcMotor.Direction.REVERSE);

        // Brake mode
        FLW.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        BLW.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        FRW.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        BRW.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // No encoders
        FLW.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        BLW.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        FRW.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        BRW.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    // Basic drive functions
    public void stop() {
        setPower(0, 0, 0, 0);
    }

    public void setPower(double fl, double fr, double bl, double br) {
        FLW.setPower(fl);
        FRW.setPower(fr);
        BLW.setPower(bl);
        BRW.setPower(br);
    }

    // Strafe for Limelight (robot-centric)
    public void strafe(double power) {
        setPower(power, -power, -power, power);
    }

    // Forward/back
    public void drive(double power) {
        setPower(power, power, power, power);
    }

    // Turn in place
    public void turn(double power) {
        setPower(power, -power, power, -power);
    }
}
