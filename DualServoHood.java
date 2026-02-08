package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;


@TeleOp(name = "Dual Servo Hood", group = "Mechanisms")
public class DualServoHood extends LinearOpMode {

    Servo hoodLeft;
    Servo hoodRight;

    // ===== TUNING VALUES =====
    double leftOffset  = 0.00;   // tune if hood twists
    double rightOffset = 0.02;

    double HOOD_MIN = 0.30;      // safe minimum
    double HOOD_MAX = 0.80;      // safe maximum

    double hoodPosition = 0.50;  // start in middle

    @Override
    public void runOpMode() {

        hoodLeft  = hardwareMap.get(Servo.class, "hoodLeft");
        hoodRight = hardwareMap.get(Servo.class, "hoodRight");


        // Reverse ONE servo if mounted opposite
        //hoodRight.setDirection(Servo.Direction.REVERSE);


        setHood(hoodPosition);

        waitForStart();

        while (opModeIsActive()) {

            // Preset positions
            if (gamepad1.a) hoodPosition = 0.35; // low shot
            if (gamepad1.b) hoodPosition = 0.55; // mid shot
            if (gamepad1.y) hoodPosition = 0.75; // high shot

            // Manual fine adjustment
            if (gamepad1.dpad_up)   hoodPosition += 0.02;   // small step up
            if (gamepad1.dpad_down) hoodPosition -= 0.02;   // small step down


            hoodPosition = clip(hoodPosition);
            setHood(hoodPosition);

            telemetry.addData("Hood Position", hoodPosition);
            telemetry.update();
        }
    }

    void setHood(double pos) {
        hoodLeft.setPosition(clip(pos + leftOffset));
        hoodRight.setPosition(clip(pos + rightOffset));

    }


    double clip(double value) {
        return Math.max(HOOD_MIN, Math.min(HOOD_MAX, value));
    }
}

