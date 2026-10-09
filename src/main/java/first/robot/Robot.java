// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package first.robot;

import java.util.function.DoubleSupplier;

import org.wpilib.command3.Scheduler;
import org.wpilib.command3.button.CommandGamepad;
import org.wpilib.framework.OpModeRobot;

import com.limelightvision.Limelight;

import first.robot.A301.A301_drivetrain;

public class Robot extends OpModeRobot {
  final CommandGamepad gamepad =
      new CommandGamepad(0);

  final A301_drivetrain mecanum = new A301_drivetrain();

  Limelight camera = new Limelight(Limelight.SYSTEMCORE_USB0);    

  public DoubleSupplier tx = () -> {
      if (camera.hasTarget()) {                   
        var results = camera.getLatestResults();
        return results.txDegrees;
      }
      return 0;
  };

  public DoubleSupplier ty = () -> {
      if (camera.hasTarget()) {                   
        var results = camera.getLatestResults();
        return results.tyDegrees;
      }
      return 0;
  };

  public Robot() {
    mecanum.setDefaultCommand(mecanum.idle());
  }

  @Override
  public void robotPeriodic() {
    Scheduler.getDefault().run();
  }
}
