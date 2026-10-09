// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package first.robot;

import org.wpilib.opmode.OpMode;
import org.wpilib.opmode.Teleop;

@Teleop
public class Teleoperated implements OpMode {
  public Teleoperated(Robot robot) {
    robot.mecanum.setDefaultCommand(robot.mecanum.driveFieldRelative(robot.gamepad));

    robot.gamepad.dpadUp().whileTrue(robot.mecanum.aimWithAprilTag(robot.ty, robot.tx));
  }
}
