@Autonomous(name="Limelight Auto Align", group="Auto")
public class LimelightAutoAlign extends LinearOpMode {

    private DcMotor FLW, FRW, BLW, BRW;  // your motors

    private double kP = 0.03;
    private double kD = 0.001;
    private double lastError = 0;

    @Override
    public void runOpMode() throws InterruptedException {

        FLW = hardwareMap.get(DcMotor.class, "frontLeft");
        FRW = hardwareMap.get(DcMotor.class, "frontRight");
        BLW = hardwareMap.get(DcMotor.class, "backLeft");
        BRW = hardwareMap.get(DcMotor.class, "backRight");

        FRW.setDirection(DcMotorSimple.Direction.REVERSE);
        BRW.setDirection(DcMotorSimple.Direction.REVERSE);

        Limelight3A limelight = hardwareMap.get(Limelight3A.class, "limelight");

        waitForStart();

        while (opModeIsActive()) {

            LimelightTargetDetection tgt = limelight.getTarget();

            if (tgt == null) {
                telemetry.addLine("NO TAG FOUND");
                stopMotors();
                continue;
            }

            // tx = horizontal offset
            double tx = tgt.x;

            double error = tx;
            double derivative = error - lastError;
            double correction = kP * error + kD * derivative;
            lastError = error;

          
            double p = -correction;

            double flp = p;
            double frp = -p;
            double blp = -p;
            double brp = p;

            setMotorPowers(flp, frp, blp, brp);

            telemetry.addData("tx", tx);
            telemetry.addData("correction", correction);
            telemetry.update();
        }
    }

    private void setMotorPowers(double flp, double frp, double blp, double brp) {
        FLW.setPower(flp);
        FRW.setPower(frp);
        BLW.setPower(blp);
        BRW.setPower(brp);
    }

    private void stopMotors() {
        setMotorPowers(0, 0, 0, 0);
    }
}
