package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.HardwareMap;

// 1. **DO NOT** extend LinearOpMode. Extend nothing, or a base Subsystem class.
public class FlipServo { 

    // Constants for Servo positions
    static final double STOW_POS    = 0.0;    // Stow/Closed position (MIN_POS)
    static final double INTAKE_POS  = 0.2;    // Middle/Safe position (Example)
    static final double DUMP_POS    = 1.0;    // Dump/Open position (MAX_POS)

    // Define the servo hardware object
    private Servo servo;

    // 2. **REQUIRED FIX:** Add a constructor that takes the HardwareMap
    public FlipServo(HardwareMap hardwareMap) {
        // Initialize the servo using the HardwareMap passed from the OpMode
        // Use the name "left" that you had in your original OpMode
        servo = hardwareMap.get(Servo.class, "left"); 
        
        // Set an initial position
        servo.setPosition(STOW_POS); 
    }

    // 3. Add methods to control the servo position
    
    public void setStowPosition() {
        servo.setPosition(STOW_POS);
    }
    
    public void setIntakePosition() {
        servo.setPosition(INTAKE_POS);
    }
    
    public void setDumpPosition() {
        servo.setPosition(DUMP_POS);
    }
    
    // Optional: for telemetry
    public double getPosition() {
        return servo.getPosition();
    }
}
