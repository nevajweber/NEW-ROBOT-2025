
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous
public class DriveAutoPID extends LinearOpMode {

    private DcMotor FLW, FRW, BLW, BRW;
    private PIDController pid;
    private ElapsedTime timer = new ElapsedTime();

    // GoBilda 312/435/520 encoders etc: adjust to your actual motor.
    private static final double TICKS_PER_REV = 537.7;       // GoBilda 5202/5203 (19.2:1)
    private static final double WHEEL_DIAMETER_IN = 3.78;    // adjust to your wheel
    private static final double TICKS_PER_INCH =
            TICKS_PER_REV / (Math.PI * WHEEL_DIAMETER_IN);

    // PID values — tune on the field
    private static final double KP = 0.01;
    private static final double KI = 0.0;
    private static final double KD = 0.0005;

    @Override
    public void runOpMode() throws InterruptedException {

        // ---------- MOTOR SETUP ----------
        FRW = hardwareMap.get(DcMotor.class, "FRW");
        FLW = hardwareMap.get(DcMotor.class, "FLW");
        BRW = hardwareMap.get(DcMotor.class, "BRW");
        BLW = hardwareMap.get(DcMotor.class, "BLW");

        // Match TeleOp directions:
        FLW.setDirection(DcMotor.Direction.FORWARD);
        BLW.setDirection(DcMotor.Direction.FORWARD);
        FRW.setDirection(DcMotor.Direction.REVERSE);
        BRW.setDirection(DcMotor.Direction.REVERSE);

        FLW.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        BLW.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        FRW.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        BRW.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // Reset encoders
        FLW.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        FRW.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        BLW.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        BRW.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        // Use our own PID (RUN_WITHOUT_ENCODER)
        FLW.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        FRW.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        BLW.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        BRW.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        pid = new PIDController(KP, KI, KD);

        telemetry.addLine("PID Auto Initialized");
        telemetry.update();

        waitForStart();
        if (isStopRequested()) return;

        // Example: drive forward 24 inches with 5s timeout
        driveToDistancePID(24.0, 5.0);

        setAllPower(0);
    }

    private void driveToDistancePID(double inches, double timeoutSeconds) {
        double targetTicks = inches * TICKS_PER_INCH;
        pid.reset();
        timer.reset();

        while (opModeIsActive()) {
            double time = timer.seconds();
            if (time > timeoutSeconds) break;

            double currentTicks = getAverageEncoderPosition();
            double output = pid.calculate(targetTicks, currentTicks, time);

            // Scale PID output to motor power. Tune  divisor.
            double power = clamp(output / 500.0, -0.7, 0.7);
            setAllPower(power);

            double error = targetTicks - currentTicks;

            telemetry.addData("TargetTicks", targetTicks);
            telemetry.addData("CurrentTicks", currentTicks);
            telemetry.addData("Error", error);
            telemetry.addData("Power", power);
            telemetry.update();

            if (Math.abs(error) < 20) {
                break; // close enough
            }
        }

        setAllPower(0);
    }

    private double getAverageEncoderPosition() {
        return (FLW.getCurrentPosition()
                + FRW.getCurrentPosition()
                + BLW.getCurrentPosition()
                + BRW.getCurrentPosition()) / 4.0;
    }

    private void setAllPower(double p) {
        FLW.setPower(p);
        FRW.setPower(p);
        BLW.setPower(p);
        BRW.setPower(p);
    }

    private double clamp(double val, double min, double max) {
        return Math.max(min, Math.min(max, val));
    }
}
