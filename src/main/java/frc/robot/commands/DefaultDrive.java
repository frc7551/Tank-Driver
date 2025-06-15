package frc.robot.commands;

import frc.robot.Constants;
import frc.robot.Constants.OperatorConstants;
import frc.robot.subsystems.Drivetrain;
import edu.wpi.first.wpilibj2.command.Command;
import java.util.function.DoubleSupplier;

public class DefaultDrive extends Command {
  private final Drivetrain m_drivetrain;
  private final DoubleSupplier m_speedSupplier;
  private final DoubleSupplier m_rotationSupplier;

  // 用於速度斜坡的變數
  private double m_currentSpeed = 0.0;
  private double m_currentRotation = 0.0;

    /**
   * 創建一個 DefaultDrive 指令。
   *
   * @param drivetrain       Drivetrain 子系統實例。
   * @param speedSupplier  供應器，用於獲取左右馬達的速度。
   * @param rotationSupplier  供應器，用於獲取旋轉速度。
   */
  public DefaultDrive(Drivetrain drivetrain, DoubleSupplier speedSupplier, DoubleSupplier rotationSupplier) {
    m_drivetrain = drivetrain;
    m_speedSupplier = speedSupplier;
    m_rotationSupplier = rotationSupplier;

    // 將 Drivetrain 子系統作為要求（requirement）
    // 這確保在 DefaultDrive 執行時，沒有其他指令可以控制 Drivetrain
    addRequirements(m_drivetrain);
  }

  // 指令開始執行時呼叫。確保每次啟動時速度歸零。
  @Override
  public void initialize() {
    m_currentSpeed = 0.0;
    m_currentRotation = 0.0;
  }
 
  // 指令在每個機器人週期持續執行時呼叫
  @Override
  public void execute() {
    // 從供應器獲取左右馬達的速度值
    double desiredSpeed = m_speedSupplier.getAsDouble();
    double desiredRotation = m_rotationSupplier.getAsDouble();

    // --- 速度斜坡邏輯 ---

    // 處理前進/後退的速度斜坡
    if (Math.abs(desiredSpeed) < OperatorConstants.STOP_THRESHOLD) {
      // 如果搖桿輸入接近零，則緩慢減速到零
      m_currentSpeed = limitAndDecay(m_currentSpeed, 0.0, OperatorConstants.MAX_DECCELERATION_CHANGE);
    } else {
      // 正常加速或減速到目標速度
      m_currentSpeed = limitChange(m_currentSpeed, desiredSpeed, OperatorConstants.MAX_ACCELERATION_CHANGE);
    }

    // 處理轉向的速度斜坡
    if (Math.abs(desiredRotation) < OperatorConstants.STOP_THRESHOLD) {
      // 如果搖桿輸入接近零，則緩慢減速到零
      m_currentRotation = limitAndDecay(m_currentRotation, 0.0, OperatorConstants.MAX_DECCELERATION_CHANGE);
    } else {
      // 正常加速或減速到目標轉向
      m_currentRotation = limitChange(m_currentRotation, desiredRotation, OperatorConstants.MAX_ACCELERATION_CHANGE);
    }

    // 將這些值傳遞給底盤子系統
    m_drivetrain.arcadeDrive(m_currentSpeed, m_currentRotation);
  }

  /**
   * 限制當前值向目標值變化的速率。
   * 主要用於正常加速和非停止減速時的平滑過渡。
   *
   * @param current   當前速度值。
   * @param target    目標速度值（來自搖桿）。
   * @param maxChange 每次執行週期中允許的最大變化量 (例如 0.05)。
   * @return 經過限制調整後的新的速度值。
   */
  private double limitChange(double current, double target, double maxChange) {
    double delta = target - current;
    // 限制變化量在 -maxChange 到 +maxChange 之間
    if (delta > maxChange) {
      delta = maxChange;
    } else if (delta < -maxChange) {
      delta = -maxChange;
    }
    return current + delta;
  }

  /**
   * 限制當前值向目標值（通常為零）變化的速率，並在接近零時強制歸零。
   * 主要用於鬆開搖桿時，實現緩慢減速並最終停止（緩煞車）的效果。
   *
   * @param current   當前速度值。
   * @param target    目標速度值（通常為 0.0）。
   * @param maxChange 每次執行週期中允許的最大變化量。
   * @return 經過限制調整後的新的速度值，或在接近零時直接為零。
   */
  private double limitAndDecay(double current, double target, double maxChange) {
    double delta = target - current;
    if (Math.abs(current) < maxChange) { // 如果當前速度很小，直接歸零
        return target;
    }
    // 否則，緩慢地向目標速度減速
    if (delta > maxChange) {
      delta = maxChange;
    } else if (delta < -maxChange) {
      delta = -maxChange;
    }
    return current + delta;
  }

  // 指令結束時呼叫
  @Override
  public void end(boolean interrupted) {
    // 當指令結束或被中斷時，停止底盤
    m_drivetrain.stop();
  }
}
