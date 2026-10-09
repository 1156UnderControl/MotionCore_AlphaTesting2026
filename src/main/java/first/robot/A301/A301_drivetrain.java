// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package first.robot.A301;

import java.util.function.DoubleSupplier;

import org.wpilib.command3.Command;
import org.wpilib.command3.Mechanism;
import org.wpilib.command3.button.CommandGamepad;
import org.wpilib.driverstation.Gamepad;
import org.wpilib.hardware.bus.CANPort;
import org.wpilib.hardware.imu.OnboardIMU;
import org.wpilib.hardware.imu.OnboardIMU.MountOrientation;

import com.revrobotics.spark.A301;

public class A301_drivetrain implements Mechanism {
  private final A301 frontLeft = new A301(CANPort.CAN_D10);
  private final A301 frontRight = new A301(CANPort.CAN_D3);
  private final A301 backLeft = new A301(CANPort.CAN_D11);
  private final A301 backRight = new A301(CANPort.CAN_D4);

  private final OnboardIMU imu = new OnboardIMU(MountOrientation.FLAT);

  private static final double kP = 0.05;
  private static final double kPy = 0.05;
  public A301_drivetrain() {

  }

  public Command aimWithAprilTag(DoubleSupplier ty, DoubleSupplier tx) {
    return runRepeatedly(() -> {
      driveRobotOriented(0, tx.getAsDouble() * kP, 0);
    }).named("A301.Aim");
  }

  public Command driveFieldRelative(CommandGamepad gamepad) {
    return runRepeatedly(() -> {
      double forward = -gamepad.getLeftY();
      double strafe = gamepad.getLeftX();
      double turn = gamepad.getRightX();

      double yaw = imu.getYawRadians();

      double normStrafe = strafe * Math.cos(-yaw) - forward * Math.sin(-yaw);
      double normForward = strafe * Math.sin(-yaw) + forward * Math.cos(-yaw);

      driveRobotOriented(normForward, normStrafe, turn);
    }).named("A301.drive");
  }

  private void driveRobotOriented(double forward, double strafe, double turn) {
      double max = Math.max(Math.abs(forward) + Math.abs(strafe) + Math.abs(turn), 1);
      double frontLeftPower = (forward + strafe + turn) / max;
      double frontRightPower = (forward - strafe - turn) / max;
      double backLeftPower = (forward - strafe + turn) / max;
      double backRightPower = (forward + strafe  - turn) / max;

      frontLeft.setThrottle(-frontLeftPower);
      frontRight.setThrottle(frontRightPower);
      backLeft.setThrottle(-backLeftPower);
      backRight.setThrottle(backRightPower);
  }
    private final Gamepad gamepad1 = new Gamepad(0);


  @Override
  public Command idle() {
    return runRepeatedly(() -> {
        frontLeft.setThrottle(0);
        frontRight.setThrottle(0);
        backLeft.setThrottle(0);
        backRight.setThrottle(0);
    })
    .named("Mecanum.idle");
  }
}
