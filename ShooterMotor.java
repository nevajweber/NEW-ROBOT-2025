package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class TeleOpToggelShooter extends LinearOpMode {

    private ShooterMotor shooter;

    private boolean shooterOn = false;
    private boolean lastB = false;

    @Override 
    public void runOpMode() {

      shooter = new ShooterMotor(hardwareMap);

      telemetry.addLine("Press B to toggle shooter NO/Off")
      telemetry.update();

      waitForStart();

      while (opModeIsActive()) {

        boolean bNow = gamepad1.b; //change if u want bc idk what where we want it to be

        if (bNow && !last) {
          shooterOn = !shooterOn;

          if (shooter.On) {
            shooter.on();
          } else {
            shooter.off();
          }
        }
        
        lastB = bNow;

        telemetry.addData("Shooter State", shooterOn ? "ON" : "OFF");
        telemetry.update();
      }
    }
}

   
