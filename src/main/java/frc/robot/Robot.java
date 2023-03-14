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
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Compressor;
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
  private final WPI_VictorSPX vertMotor = new WPI_VictorSPX(21); // customize to the controller
  private final WPI_VictorSPX horzMotor = new WPI_VictorSPX(22);

  // Spark MAX can be used in CAN bus or PWM. If using CAN, set CAN ID in the REV Client
  // plug into PWM  assign channel number according to spot on Rio
  private final PWMSparkMax rotateMotor = new PWMSparkMax(1); // change number where ever its plugged  

  private final DifferentialDrive m_robotDrive = new DifferentialDrive(m_leftDrive1, m_rightDrive1);
  private final Joystick m_driver = new Joystick(0);
  private final XboxController m_operator = new XboxController(1);
//pneumatics  (copied from GitHub/rugh draft)
  // Solenoid corresponds to a single solenoid.
  //private final Solenoid m_solenoid = new Solenoid(PneumaticsModuleType.REVPH, 0);

  // DoubleSolenoid corresponds to a double solenoid.
  //module 9 is the location you plug the main power into the PDH
  private final DoubleSolenoid m_doubleSolenoid =
    new DoubleSolenoid(9,PneumaticsModuleType.REVPH, 1, 2);

  //end of pneumatics

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

    // OLD_move motor that rotates mast_button
    //if (m_operator.getRawButtonPressed(1)) {
      //rotateMotor.set(.1); // turn on some motor at half speed (0.5) set a value between 0 and 1. 
      // Make set value negative to run motor other direction
    //} else {
      //rotateMotor.set(0.0);
    //} // turn off a motor
    //if (m_operator.getRawButtonPressed(2)) {
      //rotateMotor.set(-.1); 
    //} else {
      //rotateMotor.set(0.0);
    //} 

    // OLD_move mast horizontally_button
    //if (m_operator.getRawButtonPressed(3)) {
      //horzMotor.set(.5); 
    //} else {
      //horzMotor.set(0.0);
    //}
    //if (m_operator.getRawButtonPressed(4)) {
      //horzMotor.set(-.5); 
    //} else {
      //rotateMotor.set(0.0);
    //}

    // OLD_move forks vertically_button
    //if (m_operator.getRawButtonPressed(5)) {
      //vertMotor.set(.5); 
    //} else {
      //vertMotor.set(0.0);
    //}

    //if (m_operator.getRawButtonPressed(6)) {
      //vertMotor.set(-.5); 
    //} else {
      //rotateMotor.set(0.0);
    //}

    // rotate mast
    double rotateForwardSpeed = m_operator.getRawAxis(3); //Get the manual lift speed
    if(rotateForwardSpeed > 0) { //If the manual speed is negative..._??negative??
      rotateForwardSpeed *= 0.2; //Limit the rotate forward speed
       rotateMotor.set(rotateForwardSpeed);
    }  else {  //Else...
      rotateForwardSpeed *= 0; //Limit the down speed_?? it was 0.1??_Oh...joystick down(-) 
      rotateMotor.set(rotateForwardSpeed);
    }

    double rotateBackSpeed = m_operator.getRawAxis(2); //Get the manual lift speed
    if(rotateBackSpeed > 0) { //If the manual speed is negative..._??negative??
      rotateBackSpeed *= 0.2; //Limit the rotate backward speed
       rotateMotor.set(rotateBackSpeed);
    }  else {  //Else...
      rotateBackSpeed *= 0; //...
      rotateMotor.set(rotateBackSpeed);
    }

    // move forks vertical
    double liftSpeed = m_operator.getRawAxis(1); //Get the manual lift speed
    if(liftSpeed > 0) { //If the manual speed is negative..._??negative??
      liftSpeed *= 0.2; //Limit the up speed
       vertMotor.set(liftSpeed);
    }  else {  //Else...
      liftSpeed *= 0.1; //Limit the down speed_?? it was 0.1??_Oh...joystick down(-)
      vertMotor.set(liftSpeed);
    }

    // move mast horizontally  --USING CONSTANTS.--
    double horzSpeed = m_operator.getRawAxis(Constants.RIGHT_STICK_X); //Get the manual lift speed
    if(horzSpeed > 0) { //If the manual speed is negative..._??negative??
      horzSpeed *= 0.2; //Limit the forward speed
       horzMotor.set(horzSpeed);
    }  else {  //Else...
      horzSpeed *= 0.2; //Limit the back speed_?? it was 0.1??_Oh...joystick down(-)
      horzMotor.set(horzSpeed);
    }


    /*Pneumatics (copied from GitHub/rugh draft)
     * The output of GetRawButton is true/false depending on whether
     * the button is pressed; Set takes a boolean for whether
     * to use the default (false) channel or the other (true).
     */
    //m_solenoid.set(m_operator.getRawButton(kSolenoidButton));

    /*
     * In order to set the double solenoid, if just one button
     * is pressed, set the solenoid to correspond to that button.
     * If both are pressed, set the solenoid will be set to Forwards.
     */
    if (m_operator.getRawButton(Constants.kDoubleSolenoidForward)) {
      m_doubleSolenoid.set(DoubleSolenoid.Value.kForward);
    } else if (m_operator.getRawButton(Constants.kDoubleSolenoidReverse)) {
      m_doubleSolenoid.set(DoubleSolenoid.Value.kReverse);
    }

    //Use pressure limiter... I need to import something I think_DONE??
    Compressor pcmCompressor = new Compressor(0, PneumaticsModuleType.CTREPCM);
    Compressor phCompressor = new Compressor(1, PneumaticsModuleType.REVPH);

    pcmCompressor.enableDigital();
    pcmCompressor.disable();

    boolean enabled = pcmCompressor.IsEnabled();
    boolean pressureSwitch = pcmCompressor.getPressureSwitchValue();
    double current = pcmCompressor.getCompressorCurrent();

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
