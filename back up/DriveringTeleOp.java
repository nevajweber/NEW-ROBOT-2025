package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.IMU;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;


@TeleOp(name="DriveringTeleOp", group="Linear OpMode")
public class DriveringTeleOp extends LinearOpMode {

    private ElapsedTime runtime = new ElapsedTime();

    private DcMotor FLW = null;
    private DcMotor FRW = null;
    private DcMotor BLW = null;
    private DcMotor BRW = null;

    private IntakerTeleOp intakeMotor;
    private OutputTeleOp outtakeMotor;

    private Servo leftCoverServo;
    private Servo rightCoverServo;
    private boolean isOpen = false;
    private boolean dpadPressed = false;

    private static final double MAX_POS = 1.0;
    private static final double MIN_POS = 0.0;

    private boolean isIntakeRunning = false;
    private boolean intakeButtonPressed = false;

    private boolean isOuttakeRunning = false;
    private boolean outtakeButtonPressed = false;

    private double smoothedFL, smoothedFR, smoothedBL, smoothedBR;

    private IMU imu;
    private boolean fieldCentricEnabled = true; // Field-centric is ON by default
    private boolean xButtonPressed = false;      // To prevent rapid toggling


    private double lerp(double current, double target, double factor) {
        return current + (target - current) * factor;
    }

    @Override
    public void runOpMode() {

        // Initialize motors
        FLW = hardwareMap.get(DcMotor.class, "FLW");
        FRW = hardwareMap.get(DcMotor.class, "FRW");
        BLW = hardwareMap.get(DcMotor.class, "BLW");
        BRW = hardwareMap.get(DcMotor.class, "BRW");

        // Motor Directions
        FLW.setDirection(DcMotor.Direction.REVERSE);
        BLW.setDirection(DcMotor.Direction.REVERSE);
        FRW.setDirection(DcMotor.Direction.FORWARD);
        BRW.setDirection(DcMotor.Direction.FORWARD);

        // Set RunMode (CRITICAL for motor without encoder)
        FLW.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        FRW.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        BLW.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        BRW.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        // Set Zero Power Behavior
        FLW.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        FRW.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        BLW.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        BRW.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // Initialize other systems
        intakeMotor = new IntakerTeleOp(hardwareMap);
        outtakeMotor = new OutputTeleOp(hardwareMap);
        leftCoverServo = hardwareMap.get(Servo.class, "coverLeft");
        rightCoverServo = hardwareMap.get(Servo.class, "coverRight");
        leftCoverServo.setPosition(MIN_POS);
        rightCoverServo.setPosition(MIN_POS);

        // ===== IMU INITIALIZATION =====
        imu = hardwareMap.get(IMU.class, "imu");
        IMU.Parameters parameters = new IMU.Parameters(
                new RevHubOrientationOnRobot(
                        RevHubOrientationOnRobot.LogoFacingDirection.LEFT,
                        RevHubOrientationOnRobot.UsbFacingDirection.UP
                )
        );
        imu.initialize(parameters);
        imu.resetYaw();
        // ===============================

        telemetry.addData("Status", "Initialized");
        telemetry.addData(">", "Press Back button to reset Yaw");
        telemetry.update();

        waitForStart();
        runtime.reset();

        while (opModeIsActive()) {

            // --- Input Processing ---
            double deadzone = 0.05;
            double leftStickY = (Math.abs(gamepad1.left_stick_y) > deadzone) ? gamepad1.left_stick_y : 0;
            double leftStickX = (Math.abs(gamepad1.left_stick_x) > deadzone) ? gamepad1.left_stick_x : 0;
            double rightStickX = (Math.abs(gamepad1.right_stick_x) > deadzone) ? gamepad1.right_stick_x : 0;

            double speed = gamepad1.left_bumper ? 0.3 : 1.0;
            // Correctly process joystick inputs
            double axial = -leftStickY * speed;    // Forward/Backward
            double lateral = -leftStickX* speed;     // Strafe Left/Right
            double yaw = rightStickX* speed;        // Turn Left/Right


            // === TOGGLE FIELD-CENTRIC DRIVE ===
            if (gamepad1.x && !xButtonPressed) {
                fieldCentricEnabled = !fieldCentricEnabled; // Flip the boolean
                xButtonPressed = true;
            } else if (!gamepad1.x) {
                xButtonPressed = false;
            }


            // --- Field-Centric Calculation ---
           
            if (fieldCentricEnabled) {
                // Reset yaw (heading) to zero when the Back button is pressed
                if (gamepad1.back) {
                    imu.resetYaw();
                }

                double botHeading = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);

                // This is the standard, correct formula for rotating the joystick inputs
                // to make them relative to the field instead of the robot.
                double rotX = lateral * Math.cos(-botHeading) - axial * Math.sin(-botHeading);
                double rotY = lateral * Math.sin(-botHeading) + axial * Math.cos(-botHeading);

                // Re-assign the field-centric values to be used in the motor calculations
                axial = rotY;
                lateral = rotX;
            }
           
           
            // --- Mecanum Power Calculation ---
            double frontLeftPower = axial + lateral + yaw;
            double frontRightPower = axial - lateral - yaw;
            double backLeftPower = axial - lateral + yaw;
            double backRightPower = axial + lateral - yaw;


            // --- Normalize Motor Powers ---
            double max = Math.max(Math.abs(frontLeftPower), Math.abs(frontRightPower));
            max = Math.max(max, Math.abs(backLeftPower));
            max = Math.max(max, Math.abs(backRightPower));
            if (max > 1.0) {
                frontLeftPower /= max;
                frontRightPower /= max;
                backLeftPower /= max;
                backRightPower /= max;
            }


            // --- Apply Smoothed Power to Wheels ---
            double smoothFactor = 0.3;
            smoothedFL = lerp(smoothedFL, frontLeftPower, smoothFactor);
            smoothedFR = lerp(smoothedFR, frontRightPower, smoothFactor);
            smoothedBL = lerp(smoothedBL, backLeftPower, smoothFactor);
            smoothedBR = lerp(smoothedBR, backRightPower, smoothFactor);

            FLW.setPower(smoothedFL);
            FRW.setPower(smoothedFR);
            BLW.setPower(smoothedBL);
            BRW.setPower(smoothedBR);


            // --- Intake Control (Gamepad 1) ---
            if (gamepad1.y && !intakeButtonPressed) {
                isIntakeRunning = !isIntakeRunning;
                intakeButtonPressed = true;
            } else if (!gamepad1.y) {
                intakeButtonPressed = false;
            }
            if (isIntakeRunning) {
                intakeMotor.in();
            } else {
                intakeMotor.stop();
            }
            if (gamepad1.a) {
                intakeMotor.out();
            }


            // --- Outtake Control (Gamepad 2) ---
            if (gamepad2.x && !outtakeButtonPressed) {
                isOuttakeRunning = !isOuttakeRunning;
                outtakeButtonPressed = true;
            } else if (!gamepad2.x) {
                outtakeButtonPressed = false;
            }
            if (isOuttakeRunning) {
                outtakeMotor.out();
            } else {
                outtakeMotor.stop();
            }
            if (gamepad2.b) {
                outtakeMotor.in();
            }


            // --- Cover Servo Control (Gamepad 2 D-Pad) ---
            if ((gamepad2.dpad_up || gamepad2.dpad_down) && !dpadPressed) {
                dpadPressed = true;
                if (gamepad2.dpad_up) {
                    leftCoverServo.setPosition(MAX_POS);
                    rightCoverServo.setPosition(MIN_POS);
                    isOpen = true;
                } else {
                    leftCoverServo.setPosition(MIN_POS);
                    rightCoverServo.setPosition(MAX_POS);
                    isOpen = false;
                }
            }
            if (!gamepad2.dpad_up && !gamepad2.dpad_down) {
                dpadPressed = false;
            }


            // --- Telemetry ---
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.addData("Front Left/Right", "%4.2f, %4.2f", smoothedFL, smoothedFR);
            telemetry.addData("Back Left/Right", "%4.2f, %4.2f", smoothedBL, smoothedBR);
            telemetry.addData("Drive Mode", fieldCentricEnabled ? "Field-Centric" : "Robot-Centric");
            telemetry.addData("Heading (deg)", Math.toDegrees(imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS)));
            telemetry.addData("Cover", isOpen ? "Open" : "Closed");
            telemetry.update();
        }
    }
}
