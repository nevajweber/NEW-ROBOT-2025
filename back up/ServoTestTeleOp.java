package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.CRServo;

@TeleOp(name="Basic Servo Control", group="Concept")
public class BasicServoControl extends LinearOpMode {

    private Servo myStandardServo;
    private CRServo myContinuousServo;

    @Override
    public void runOpMode() {
        // Initialize servos from hardware map
        myStandardServo = hardwareMap.get(Servo.class, "standard_servo_name");
        myContinuousServo = hardwareMap.get(CRServo.class, "cr_servo_name");

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart(); // Wait for the game to start (driver presses PLAY)

        while (opModeIsActive()) {
            // Control standard servo with gamepad buttons
            if (gamepad1.a) {
                myStandardServo.setPosition(0.0); // Move to one extreme
            } else if (gamepad1.b) {
                myStandardServo.setPosition(1.0); // Move to the other extreme
            } else if (gamepad1.x) {
                myStandardServo.setPosition(0.5); // Move to center
            }

            // Control continuous rotation servo with d-pad
            if (gamepad1.dpad_up) {
                myContinuousServo.setPower(0.5); // Spin forward
            } else if (gamepad1.dpad_down) {
                myContinuousServo.setPower(-0.5); // Spin backward
            } else {
                myContinuousServo.setPower(0.0); // Stop
            }

            telemetry.addData("Standard Servo Position", myStandardServo.getPosition());
            telemetry.addData("CR Servo Power", myContinuousServo.getPower());
            telemetry.update();
        }
    }
}
