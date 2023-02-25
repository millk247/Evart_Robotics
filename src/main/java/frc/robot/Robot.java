// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.PWMSparkMax;
import edu.wpi.first.wpilibj.motorcontrol.Victor;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the
 * name of this class or
 * the package after creating this project, you must also update the manifest
 * file in the resource
 * directory.
 */
public class Robot extends TimedRobot {

  private final WPI_VictorSPX m_leftDrive1 = new WPI_VictorSPX(1);
  private final WPI_VictorSPX m_leftDrive2 = new WPI_VictorSPX(2);

  // private final WPI_VictorSPX m_arm = new WPI_VictorSPX(3); // Moves arm

  private final WPI_VictorSPX m_rightDrive1 = new WPI_VictorSPX(11);
  private final WPI_VictorSPX m_rightDrive2 = new WPI_VictorSPX(12);
  private final WPI_VictorSPX some_motor = new WPI_VictorSPX(10); // customize to the controller

  // Spark MAX can be used in CAN bus or PWM. If using CAN, set CAN ID in the REV Client
  private final PWMSparkMax vSlide = new PWMSparkMax(14); // change number where ever its plugged  

  private final DifferentialDrive m_robotDrive = new DifferentialDrive(m_leftDrive1, m_rightDrive1);
  private final Joystick m_driver = new Joystick(0);
  private final Joystick operator = new Joystick(1);

  // private final XboxController m_controller = new XboxController(0);
  // private final XboxController m_controller = new XboxController(0); // Second
  // controller goes here
  private final Timer m_timer = new Timer();

  /**
   * This function is run when the robot is first started up and should be used
   * for any
   * initialization code.
   */
  @Override
  public void robotInit() {
    // We need to invert one side of the drivetrain so that positive voltages
    // result in both sides moving forward. Depending on how your robot's
    // gearbox is constructed, you might have to invert the left side instead.
    m_rightDrive1.setInverted(true);
    m_rightDrive2.setInverted(true);

    m_leftDrive2.set(ControlMode.Follower, Constants.Victor_1_ID);
    m_rightDrive2.set(ControlMode.Follower, Constants.Victor_11_ID);

    // 2 is follower of 1, and 12 is follwoer of 11
  }

  /** This function is run once each time the robot enters autonomous mode. */
  @Override
  public void autonomousInit() {
    m_timer.reset();
    m_timer.start();
  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {
    // Drive for 2 seconds
    if (m_timer.get() < 2.0) {
      // Drive forwards half speed, make sure to turn input squaring off
      m_robotDrive.arcadeDrive(0.5, 0.0, false);
    } else {
      m_robotDrive.stopMotor(); // stop robot
    }
  }

  /**
   * This function is called once each time the robot enters teleoperated mode.
   */
  @Override
  public void teleopInit() {
  }

  /** This function is called periodically during teleoperated mode. */
  @Override
  public void teleopPeriodic() {
    m_robotDrive.arcadeDrive(-m_driver.getY(), -m_driver.getX());
    // m_robotDrive.arcadeDrive(-m_controller.getLeftY(), -m_controller.getLeftX());
    // m_arm.set(ControlMode.PercentOutput, m_controller.getRightY());

    // move some motor
    // Each button has numeric value that needs to be looked up online
    if (m_driver.getRawButtonPressed(1)) {
      some_motor.set(.5); // turn on some motor at half speed (0.5) set a value between 0 and 1. 
      // Make set value negative to run motor other direction
    } else {
      some_motor.set(0.0);
    } // turn off a motor


    if (operator.getRawButtonPressed(1)) {
      vSlide.set(0.2); // turn on some motor at set a value between 0 and 1. 
      // Make set value negative to run motor other direction
    } else {
      vSlide.set(0.0);
    } // turn off a motor

    if (operator.getRawButtonPressed(2)) {
      vSlide.set(-0.2); // turn on some motor at set a value between 0 and 1. 
      // Make set value negative to run motor other direction
    } else {
      vSlide.set(0.0);
    } // turn off a motor

  } // end of teleop period

  /** This function is called once each time the robot enters test mode. */
  @Override
  public void testInit() {
  }

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {
  }
}

/*
 * Vendor Library for REV
 * https://software-metadata.revrobotics.com/REVLib-2023.json
 * 
 */
