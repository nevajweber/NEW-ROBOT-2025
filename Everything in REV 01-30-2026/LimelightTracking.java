package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;
@TeleOp
public class LimelightTracking extends OpMode {
    double power = 0.0;

    private Limelight3A limelight;
    private DcMotorEx turret;
    private IMU imu;


    public void init() {

        turret = hardwareMap.get(DcMotorEx.class, "turret");
        limelight = hardwareMap.get(Limelight3A.class, "limelight");
        limelight.pipelineSwitch(0);
        imu = hardwareMap.get(IMU.class, "imu");
        RevHubOrientationOnRobot revHubOrientationOnRobot = new RevHubOrientationOnRobot(RevHubOrientationOnRobot.LogoFacingDirection.RIGHT,
                RevHubOrientationOnRobot.UsbFacingDirection.UP);
        imu.initialize(new IMU.Parameters(revHubOrientationOnRobot));

        turret.setDirection(DcMotorEx.Direction.FORWARD);
        turret.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        turret.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);


    }

    public void start() {

        limelight.start();
    }

    @Override
    public void loop() {
        YawPitchRollAngles orientation = imu.getRobotYawPitchRollAngles();
        limelight.updateRobotOrientation(orientation.getYaw(AngleUnit.DEGREES));
        LLResult llResult = limelight.getLatestResult();

        if (llResult != null && llResult.isValid()) {
            double tx = llResult.getTx();
            double kP = 0.05;
            double deadband = 1;

            // Only apply power if 'B' is pressed AND we are outside the deadband
            if (gamepad1.b && Math.abs(tx) > deadband) {
                power = tx * kP;
                power = Math.max(-.8, Math.min(0.8, power));
            }

            telemetry.addData("Target Offset (tx)", tx);
            telemetry.addData("BotPose", llResult.getBotpose().toString());
        } else {
            power = 0;
        }

        turret.setPower(power);



        // 5. Global Telemetry
        telemetry.addData("Turret Power", power);
        telemetry.addData("Visible", llResult != null && llResult.isValid());
        telemetry.update();


    }
}
