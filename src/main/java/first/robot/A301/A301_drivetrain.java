// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package first.robot.A301;



import org.wpilib.hardware.hal.CANBusMap;
import org.wpilib.hardware.imu.OnboardIMU;
import org.wpilib.hardware.imu.OnboardIMU.MountOrientation;

import com.revrobotics.spark.A301;


import org.wpilib.driverstation.Gamepad;

/**
 * The methods in this class are called automatically corresponding to each mode, as described in
 * the TimedRobot documentation. If you change the name of this class or the package after creating
 * this project, you must also update the Main.java file in the project.
 */
public class A301_drivetrain {
 
  private final A301 LeftFront = new A301(CANBusMap.CAN_D11);
  private final A301 RightFront = new A301(CANBusMap.CAN_D7);
  private final A301 LeftTarget = new A301(CANBusMap.CAN_D12);
  private final A301 RightTarget = new A301(CANBusMap.CAN_D9);


  private final Gamepad gamepad1 = new Gamepad(0);

  private final OnboardIMU imu = new OnboardIMU(MountOrientation.FLAT);


  public void drive(double forward, double strafe, double rotate) {


    double yaw = imu.getYawRadians();

    double newStrafe = strafe * Math.cos(-yaw) - forward * Math.sin(-yaw);
    double newForward = strafe * Math.sin(-yaw) + forward * Math.cos(-yaw);

    double leftFrontPower =  newForward + newStrafe + rotate;
    double leftTargetPower = newForward - newStrafe + rotate;
    double rightFrontPower = newForward - newStrafe - rotate;
    double rightTargetPower = newForward + newStrafe - rotate;


    double max = Math.max(1.0, Math.max(Math.abs(leftFrontPower),
            Math.max(
                Math.abs(leftTargetPower),
                Math.max(Math.abs(rightFrontPower), Math.abs(rightTargetPower)))));

    LeftFront.setThrottle(leftFrontPower / max);
    LeftTarget.setThrottle(leftTargetPower / max);
    RightFront.setThrottle(-rightFrontPower / max);
    RightTarget.setThrottle(-rightTargetPower / max);
  }

  public void driveFieldRelative(double forward, double strafe, double rotate) {
    double headingDegrees = 0.0;
    double headingRadians = Math.toRadians(headingDegrees);

    double rotX = strafe * Math.cos(headingRadians) - forward * Math.sin(headingRadians);
    double rotY = strafe * Math.sin(headingRadians) + forward * Math.cos(headingRadians);

    drive(rotY, rotX, rotate);
  }
  
  public void runOpMode() {
    double forward = gamepad1.getLeftY();
    double strafe = -gamepad1.getLeftX();
    double rotate = -gamepad1.getRightX();

    driveFieldRelative(forward, strafe, rotate);
  }

  

}