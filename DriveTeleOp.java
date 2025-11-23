package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name = "DriveTeleOp", group = "FTC")
public class DriveTeleOp extends LinearOpMode {

    // Drive motors
    private DcMotor FLW, FRW, BLW, BRW;

    // Subsystems
    private IntakeTeleOp intake;
    private OuttakeTeleOp outtakeMotor;
    private SpindexerTeleOp spindexerMotor;
    private OuttakeServo flipper;
    private DualTorqueServos arm;   // two torque servos

    @Override
    public void runOpMode() throws InterruptedException {

        // ---------- DRIVETRAIN SETUP ----------
        FRW = hardwareMap.get(DcMotor.class, "FRW");
        FLW = hardwareMap.get(DcMotor.class, "FLW");
        BRW = hardwareMap.get(DcMotor.class, "BRW");
        BLW = hardwareMap.get(DcMotor.class, "BLW");

        // Directions (standard tank)
        FLW.setDirection(DcMotor.Direction.FORWARD);
        BLW.setDirection(DcMotor.Direction.FORWARD);
        FRW.setDirection(DcMotor.Direction.REVERSE);
        BRW.setDirection(DcMotor.Direction.REVERSE);

        FLW.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        BLW.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        FRW.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        BRW.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        FLW.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        BLW.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        FRW.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        BRW.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        // ---------- SUBSYSTEMS ----------
        intake         = new IntakeTeleOp(hardwareMap);
        outtakeMotor   = new OuttakeTeleOp(hardwareMap);
        spindexerMotor = new SpindexerTeleOp(hardwareMap);
        flipper        = new OuttakeServo(hardwareMap);
        arm            = new DualTorqueServos(hardwareMap);

        telemetry.addLine("DriveTeleOp Initialized");
        telemetry.addLine("gamepad1: drive");
        telemetry.addLine("gamepad2: intake/outtake/flipper/arm");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {

            // ===== SANITY CHECK (hold A to check if motors are going same direction) =====
            if (gamepad1.a) {
                FLW.setPower(0.3);
                BLW.setPower(0.3);
                FRW.setPower(0.3);
                BRW.setPower(0.3);
                continue;
            }

            // ===== DRIVING (arcade, gamepad1) =====
            double drive = -gamepad1.left_stick_y;  // forward/back
            double turn  = gamepad1.right_stick_x;  // rotate

            double leftPower  = drive + turn;
            double rightPower = drive - turn;

            // normalize to keep within [-1,1]
            double max = Math.max(1.0, Math.max(Math.abs(leftPower), Math.abs(rightPower)));
            leftPower  /= max;
            rightPower /= max;

            FLW.setPower(leftPower);
            BLW.setPower(leftPower);
            FRW.setPower(rightPower);
            BRW.setPower(rightPower);

            // ===== INTAKE (gamepad1 Y/A) =====
            if (gamepad1.y) {
                intake.in();
            } else if (gamepad1.a) {
                intake.out();
            } else {
                intake.stop();
            }

            // ===== SPINDEXER (gamepad1: LT / LB) =====
            if (gamepad1.left_trigger > 0.1) {
                spindexerMotor.in();
            } else if (gamepad1.left_bumper) {
                spindexerMotor.out();
            } else {
                spindexerMotor.stop();
            }

            // ===== SPINDEXER (gamepad2: LT / LB) =====
            if (gamepad2.left_trigger > 0.1) {
                spindexerMotor.in();
            } else if (gamepad2.left_bumper) {
                spindexerMotor.out();
            } else {
                spindexerMotor.stop();
            }

            // ===== OUTTAKE (gamepad2 X/B) =====
            if (gamepad2.x) {
                outtakeMotor.in();   // in
            } else if (gamepad2.b) {
                outtakeMotor.out();  // out
            } else {
                outtakeMotor.stop(); // stop
            }

            // ===== FLIPPER SERVO (gamepad2 A/B/X) =====
            if (gamepad2.a) {
                flipper.setIntakePosition();  // down/receive
            } else if (gamepad2.b) {
                flipper.setDumpPosition();    // up/dump
            } else if (gamepad2.x) {
                flipper.setStowPosition();    // mid/safe
            }

            // ===== ARM (TORQUE SERVOS) (gamepad2 dpad) =====
            if (gamepad2.dpad_down) {
                arm.toLow();
            } else if (gamepad2.dpad_up) {
                arm.toHigh();
            } else if (gamepad2.dpad_left) {
                arm.toStow();
            }

            // ===== TELEMETRY =====
            telemetry.addData("Drive", "L: %.2f R: %.2f", leftPower, rightPower);
            telemetry.addData("Intake RT/LT", "%.2f / %.2f",
                    gamepad2.right_trigger, gamepad2.left_trigger);
            telemetry.addData("Outtake RB/LB", "%b / %b",
                    gamepad2.right_bumper, gamepad2.left_bumper);
            telemetry.addData("Flipper Pos", "%.2f", flipper.getPosition());
            telemetry.update();
        }
    }
}
