package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;


import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

public class IMUMech {
    private IMU imu;


    private void init(HardwareMap hardwareMap){
        imu = hardwareMap.get(IMU.class, "imu");
//        RevHubOrientationOnRobot RevOrientation;
        RevHubOrientationOnRobot RevOrientation = new RevHubOrientationOnRobot(RevHubOrientationOnRobot.LogoFacingDirection.UP,
                RevHubOrientationOnRobot.UsbFacingDirection.FORWARD
        );

        imu.initialize(new IMU.Parameters(RevOrientation));


    }

    public double getheading(){
        return imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES);

    }



}
