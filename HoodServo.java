@TeleOp
public class HoodServo extends LinearOpMode {

    Servo hoodServo;

    double hoodPos = 0.5;   //middle ish 
    double speed = 0.01;    //speed of the hood

    @Override
    public void runOpMode() {

        hoodServo = hardwareMap.get(Servo.class, "hoodServo");

        waitForStart();

        while (opModeIsActive()) {

            // left joy and Y to move hood
            double input = -gamepad1.left_stick_y;

            // adjust position
            hoodPos += input * speed;

            //range
            hoodPos = Math.max(0.0, Math.min(1.0, hoodPos));

            hoodServo.setPosition(hoodPos);

            telemetry.addData("Hood Position", hoodPos);
            telemetry.update();
        }
    }
}
