package org.firstinspires.ftc.teamcode;

/**
 * Generic PID controller for use in Autonomous.
 */
public class PIDController {
    private double kP, kI, kD;

    private double integralSum = 0;
    private double lastError = 0;
    private Double lastTime = null;
    private double integralMax = 1.0;

    public PIDController(double kP, double kI, double kD) {
        this.kP = kP;
        this.kI = kI;
        this.kD = kD;
    }

    public void setCoefficients(double kP, double kI, double kD) {
        this.kP = kP;
        this.kI = kI;
        this.kD = kD;
    }

    public void reset() {
        integralSum = 0;
        lastError = 0;
        lastTime = null;
    }

    /**
     * Compute PID output.
     * @param setpoint     target value
     * @param measurement  current value
     * @param currentTimeS time in seconds
     */
    public double calculate(double setpoint, double measurement, double currentTimeS) {
        double error = setpoint - measurement;

        double dt;
        if (lastTime == null) {
            dt = 0;
            lastTime = currentTimeS;
        } else {
            dt = currentTimeS - lastTime;
            if (dt <= 0) dt = 1e-6;
        }

        double pTerm = kP * error;

        integralSum += error * dt;
        if (integralSum > integralMax) integralSum = integralMax;
        if (integralSum < -integralMax) integralSum = -integralMax;
        double iTerm = kI * integralSum;

        double derivative = (error - lastError) / dt;
        double dTerm = kD * derivative;

        lastError = error;
        lastTime = currentTimeS;

        return pTerm + iTerm + dTerm;
    }
}

