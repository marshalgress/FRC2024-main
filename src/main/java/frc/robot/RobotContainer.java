// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import frc.robot.subsystems.shooter.shooterSubsystem;
import frc.robot.subsystems.Uptake.UptakeSubsystem;
// import frc.robot.subsystems.Vision.VisionSubsystem;
import frc.robot.subsystems.Intake.IntakeSubsystem;
import frc.robot.subsystems.Piviot.piviotSubsystem;
import frc.robot.subsystems.proximity.proximitysubsystem;
import frc.robot.subsystems.Climber.climberSubsystem;

import java.io.Console;
import java.util.Optional;

import com.pathplanner.lib.auto.AutoBuilder;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import frc.robot.subsystems.Swerve.SwerveSubsystem;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.Constants.OperatorConstants;
import frc.robot.Constants.Vision;

import com.pathplanner.lib.auto.NamedCommands;
import com.pathplanner.lib.commands.PathPlannerAuto;

/**
 * This class is where the bulk of the robot should be declared. Since
 * Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in
 * the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of
 * the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer {
  // The robot's subsystems and commands are defined here...
  public final IntakeSubsystem m_intake = new IntakeSubsystem();
  public final shooterSubsystem m_shooter = new shooterSubsystem();
  public final piviotSubsystem m_piviot = new piviotSubsystem();
  public final climberSubsystem m_climber = new climberSubsystem();
  // private final proximitysubsystem m_proximity = new proximitysubsystem();

  // private final ShooterSubsystem m_shooter = new ShooterSubsystem();
  // private final ElevatorSubsystem m_elevator = new ElevatorSubsystem();
  // private final LEDSubsystem m_LED = new LEDSubsystem

  double a =1;

  public final SwerveSubsystem m_drivebase = SwerveSubsystem.getInstance();
  // public final VisionSubsystem m_vision = new VisionSubsystem();

  private final SendableChooser<Command> autoChooser;

  // private Pose2d curentSpeakerPose;
  // private int currentSpeakerTagIndex;

  /**
   * The container for the robot. Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {
    configurePathPlanner();
    autoChooser = AutoBuilder.buildAutoChooser("Simple Auto");
    // Optional<Alliance> ally = DriverStation.getAlliance();
    // if (ally.isPresent() && ally.get() != Alliance.Blue) {
    // curentSpeakerPose = Constants.BLUE_SPEAKER;
    // currentSpeakerTagIndex = 4;
    // } else {
    // curentSpeakerPose = Constants.RED_SPEAKER;
    // currentSpeakerTagIndex = 3;
    // }
    Shuffleboard.getTab("Pre-Match").add("Auto Chooser", autoChooser);
    configureBindings(); // Configure the trigger bindings
  }

  /**
   * Use this method to define your trigger->command mappings. Triggers can be
   * created via the
   * {@link Trigger#Trigger(java.util.function.BooleanSupplier)} constructor with
   * an arbitrary
   * predicate, or via the named factories in {@link
   * edu.wpi.first.wpilibj2.command.button.CommandGenericHID}'s subclasses for
   * {@link
   * CommandXboxController
   * Xbox}/{@link edu.wpi.first.wpilibj2.command.button.CommandPS4Controller
   * PS4} controllers or
   * {@link edu.wpi.first.wpilibj2.command.button.CommandJoystick Flight
   * joysticks}.
   */
  private void configureBindings() {
    // Constants.operatorController.y().whileTrue(superstructure.toState(SuperState.SCORE_AMP_SETUP));
    // Constants.operatorController.x().whileTrue(superstructure.toState(SuperState.SCORE_STAGE_PROTECTED_SETUP));
    // Constants.operatorController.b().whileTrue(superstructure.toState(SuperState.SCORE_SPEAKER_SETUP));
    // Constants.operatorController.a().whileTrue(superstructure.toState(SuperState.SAFE));

    // Constants.operatorController.rightBumper().whileTrue(superstructure.toState(superstructure.getShootState()));
    // Constants.operatorController.leftBumper().whileTrue(superstructure.toState(SuperState.GROUND_INTAKE));
    // Constants.operatorController.leftTrigger(0.1).whileTrue(superstructure.toState(SuperState.SOURCE_INTAKE));
    // Manual controls
    // new Trigger(() -> Math.abs(Constants.operatorController.getRawAxis(1)) > 0.1)
    // .whileTrue(m_elevator.runManual(Constants.operatorController::getLeftY));
    Constants.operatorController.x().whileTrue(m_intake.uptakeshoot());
    Constants.operatorController.x().whileFalse(m_intake.disableUptake());
    Constants.operatorController.b().or(Constants.driverController.rightTrigger(.1))
        .whileTrue(m_intake.startIntaking());
    Constants.operatorController.b().or(Constants.driverController.rightTrigger(.1))
        .whileFalse(m_intake.stopIntaking());
    // Constants.operatorController.b().or(Constants.driverController.rightTrigger(.1)).whileFalse(m_uptake.stopUptaking());
    // Constants.operatorController.b().or(Constants.driverController.rightTrigger(.1)).whileTrue(m_uptake.startUptaking());

    // m_uptake.startUptaking()
    // Constants.operatorController.rightTrigger(0.1).onTrue(m_piviot.piviotAmp());
    Constants.operatorController.povUp().onTrue(m_piviot.piviotAmp());
    Constants.operatorController.povRight().onTrue(m_piviot.piviotspeakerclose());
    Constants.operatorController.povDown().onTrue(m_piviot.understage());
    // Constants.operatorController.rightBumper().onTrue(m_climber.up1Command());
    // Constants.operatorController.rightTrigger().onTrue(m_climber.down1Command());
    // Constants.operatorController.rightBumper().or
    // (Constants.operatorController.rightBumper()).whileFalse(m_climber.stop1Command());

    // Constants.operatorController.leftBumper().onTrue(m_climber.up2Command());
    // Constants.operatorController.leftTrigger().onTrue(m_climber.down2Command());
    // Constants.operatorController.leftBumper().or
    // (Constants.operatorController.leftBumper()).whileFalse(m_climber.stop2Command());

    // m_elevator.setDefaultCommand(m_elevator.stopManual());
    // Constants.operatorController.x().whileTrue(m_uptake.startUptaking());
    // Constants.operatorController.x().whileFalse(m_uptake.stopUptaking());

    // Constants.operatorController.b().or(Constants.driverController.rightTrigger(.1)).or(m_proximity.piecein.whileTrue(m_intake.startIntaking().andThen(m_uptake.startUptaking())));
    // Constants.operatorController.b().or(Constants.driverController.rightTrigger(.1)).or(m_proximity.piecein.whileFalse(m_intake.stopIntaking().andThen((m_uptake.stopUptaking()))));

    Constants.operatorController.y().whileTrue(m_shooter.startSpeakerCommand());
    Constants.operatorController.a().whileTrue(m_shooter.startAmpCommand());
    Constants.operatorController.y().or(Constants.operatorController.a()).whileFalse(m_shooter.stopShooterCommand());

    // Constants.driverController.b().whileTrue(m_drivebase.aimAtTarget(m_vision,
    // Constants.driverController.getLeftX(), Constants.driverController.getLeftY(),
    // currentSpeakerTagIndex));
    // Constants.driverController.a().whileTrue(m_drivebase.driveToPose(curentSpeakerPose));

  }

  public void configurePathPlanner() {
    m_drivebase.setupPathPlanner();
    NamedCommands.registerCommand("Intake", m_intake.astartIntaking());
    NamedCommands.registerCommand("Spin_up", m_shooter.astartSpeakerCommand());
    NamedCommands.registerCommand("shoot", m_intake.auptakeshoot());
    NamedCommands.registerCommand("stop_Spin_up", m_shooter.astopShooterCommand());
    NamedCommands.registerCommand("stop_uptake", m_intake.astopIntaking());
    NamedCommands.registerCommand("stopshoot", m_intake.auptakestopshoot());

  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    return autoChooser.getSelected();
  }

  public void setDriveMode() {
    // Applies deadbands and inverts controls because joysticks
    // are back-right positive while robot
    // controls are front-left positive
    // left stick controls translation
    // right stick controls the desired angle NOT angular rotation red positive blue
    // negitive

    Optional<Alliance> ally = DriverStation.getAlliance();
    if (ally.isPresent() && ally.get() != Alliance.Blue) {
      a = 1;

    } else {
      a = -1;

    }

    Command driveinfinityturn = m_drivebase.driveCommand(
        () -> MathUtil.applyDeadband( .6 * Constants.driverController.getLeftY(), OperatorConstants.LEFT_Y_DEADBAND),
        () -> MathUtil.applyDeadband( .6 * Constants.driverController.getLeftX(), OperatorConstants.LEFT_X_DEADBAND),
        () -> MathUtil.applyDeadband(-.8 * Constants.driverController.getRightX(), .3));

    Command driveinfinityturn_sim = m_drivebase.driveCommand(
        () -> MathUtil.applyDeadband(Constants.driverController.getLeftX(), OperatorConstants.LEFT_X_DEADBAND),
        () -> -MathUtil.applyDeadband(Constants.driverController.getLeftY(), OperatorConstants.LEFT_Y_DEADBAND),
        () -> MathUtil.applyDeadband(Constants.driverController.getRightX(), .3));
    m_drivebase.setDefaultCommand(
        RobotBase.isSimulation() ? driveinfinityturn : driveinfinityturn);

  }

  public void setMotorBrake(boolean brake) {
    m_drivebase.setMotorBrake(brake);
  }

  // public void setRumbleDetection()
  // {
  // if (m_vision.getLatestResult().hasTargets()) {
  // Constants.driverController.getHID().setRumble(RumbleType.kRightRumble, 1.0);
  // Constants.driverController.getHID().setRumble(RumbleType.kLeftRumble, 1.0);
  // System.out.println(m_vision.getLatestResult().targets);
  // } else {
  // // Constants.driverController.getHID().setRumble(RumbleType.kRightRumble, 0);
  // // Constants.driverController.getHID().setRumble(RumbleType.kLeftRumble, 0);
  // }
  // }

  // public void updateVisionSimulationPeriod() {
  // m_vision.simulationPeriodic(m_drivebase.getPose());

  // var debugField = m_vision.getSimDebugField();
  // debugField.getObject("EstimatedRobot").setPose(m_drivebase.getPose());
  // //
  // debugField.getObject("EstimatedRobotModules").setPoses(m_drivebase.getModulePoses());
  // }
 

  public void driveSimulationPeriodic() {
    m_drivebase.simulationPeriodic();
  }

}
