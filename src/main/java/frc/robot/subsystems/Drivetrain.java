package frc.robot.subsystems;

import com.revrobotics.REVLibError;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.EncoderConfig;
import com.revrobotics.spark.config.SparkBaseConfig;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Drivetrain extends SubsystemBase {
    private final SparkMax leftFrontMotor = new SparkMax(1, MotorType.kBrushless);
    private final SparkMax leftBackMotor = new SparkMax(2, MotorType.kBrushless);
    private final SparkMax rightFrontMotor = new SparkMax(3, MotorType.kBrushless);
    private final SparkMax rightBackMotor = new SparkMax(4, MotorType.kBrushless);
    
    private final MotorControllerGroup m_leftMotors = new MotorControllerGroup(leftFrontMotor, leftBackMotor);
    private final MotorControllerGroup m_rightMotors = new MotorControllerGroup(rightFrontMotor, rightBackMotor);

    private final DifferentialDrive m_drive = new DifferentialDrive(m_leftMotors, m_rightMotors);

    public Drivetrain() {
        var driveLeftMotorsConfig = new SparkMaxConfig()
            .idleMode(IdleMode.kBrake).inverted(false).smartCurrentLimit(40);
        if(leftFrontMotor.configure(driveLeftMotorsConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters) != REVLibError.kOk) {
            throw new RuntimeException("Failed to configure left front motor");
        }
        if(leftBackMotor.configure(driveLeftMotorsConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters) != REVLibError.kOk) {
            throw new RuntimeException("Failed to configure left back motor");
        }

        var driveRightMotorsConfig = new SparkMaxConfig()
            .idleMode(IdleMode.kBrake).inverted(true).smartCurrentLimit(40);
        if(rightFrontMotor.configure(driveRightMotorsConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters) != REVLibError.kOk) {
            throw new RuntimeException("Failed to configure right front motor");
        }
        if(rightBackMotor.configure(driveRightMotorsConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters) != REVLibError.kOk) {
            throw new RuntimeException("Failed to configure right back motor");
        }

        m_drive.setSafetyEnabled(false); // Disable safety checks for the drive system
    
    }

  /**
   * 控制底盤進行 Tank Drive。
   *
   * @param leftSpeed  左側馬達的速度，範圍 -1.0 到 1.0。
   * @param rightSpeed 右側馬達的速度，範圍 -1.0 到 1.0。
   */
  public void tankDrive(double leftSpeed, double rightSpeed) {
    m_drive.tankDrive(leftSpeed, rightSpeed);
  }

  /**
   * 控制底盤進行 Arcade Drive。
   *
   * @param speed    前進/後退的速度，範圍 -1.0 到 1.0。
   * @param rotation 轉向的速度，範圍 -1.0 到 1.0。
   */
  public void arcadeDrive(double speed, double rotation) {
    // 將輸入傳給 DifferentialDrive 的 arcadeDrive 方法
    m_drive.arcadeDrive(speed, rotation);
  }

  /**
   * 停止底盤所有馬達。
   */
  public void stop() {
    m_drive.arcadeDrive(0, 0);
  }

  @Override
  public void periodic() {
    // 這個方法會在每個機器人週期被呼叫
    // 可以在這裡放一些需要持續執行的任務，例如更新 Dashboard 數據
  }

  @Override
  public void simulationPeriodic() {
    // 這個方法會在模擬環境的每個機器人週期被呼叫
    // 可以在這裡放一些模擬專用的邏輯
  }
}
