package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "FlipperTest", group = "Test")
public class FlipperTest extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        Servo flipper = hardwareMap.get(Servo.class, "myServo");

        telemetry.addLine("FlipperTest ready. A=0.1, B=0.8, X=0.4");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {
            if (gamepad1.a) flipper.setPosition(0.65);
            if (gamepad1.b) flipper.setPosition(0.80);
            if (gamepad1.x) flipper.setPosition(0.40);

            telemetry.addData("Pos", flipper.getPosition());
            telemetry.update();
        }
    }
}
