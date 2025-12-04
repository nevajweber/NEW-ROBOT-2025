package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

// Limelight imports
import org.firstinspires.ftc.teamcode.Limelight3A;
import org.firstinspires.ftc.teamcode.LimelightTargetDetection;

@Autonomous(name="Limelight Auto Align", group="Auto")
public class LimelightAutoAlign extends LinearOpMode {

    private Drive drive;   // uses YOUR Drive.java

    // PID constants
    private double kP = 0.03;
    private double kD = 0.001;
    private double lastError = 0;

    @Override
    public void runOpMode() throws InterruptedException {

        // Initialize YOUR drive class
        drive = new Drive(hardwareMap);

        // Initialize Limelight
        Limelight3A limelight = hardwareMap.get(Limelight3A.class, "limelight");

        telemetry.addLine("Ready!");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {

            // Ask Limelight for the best target
            LimelightTargetDetection tgt = limelight.getTarget();

            if (tgt == null) {
                telemetry.addLine("NO TARGET FOUND");
                drive.stop();
                telemetry.update();
                continue;
            }

            // Horizontal offset (left/right angle)
            double tx = tgt.x;

            // PID correction
            double error = tx;
            double derivative = error - lastError;
            double correction = (kP * error) + (kD * derivative);
            lastError = error;

            // robot-centric strafe: negative = correct direction
            double strafePower = -correction;

            // Apply with your Drive.java
            drive.strafe(strafePower);

            telemetry.addData("tx", tx);
            telemetry.addData("PID Strafe Power", strafePower);
            telemetry.update();
        }
    }
}
