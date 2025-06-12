package frc.robot.commands;

import frc.robot.subsystems.Drivetrain;
import edu.wpi.first.wpilibj2.command.Command;
import java.util.function.DoubleSupplier;

public class DefaultDrive extends Command {
    private final Drivetrain m_drivetrain;
    private final DoubleSupplier m_leftSpeedSupplier;
    private final DoubleSupplier m_rightSpeedSupplier;

    /**
   * 創建一個 DefaultDrive 指令。
   *
   * @param drivetrain       Drivetrain 子系統實例。
   * @param leftSpeedSupplier  提供左側速度的函數。
   * @param rightSpeedSupplier 提供右側速度的函數。
   */
  public DefaultDrive(Drivetrain drivetrain, DoubleSupplier leftSpeedSupplier, DoubleSupplier rightSpeedSupplier) {
    m_drivetrain = drivetrain;
    m_leftSpeedSupplier = leftSpeedSupplier;
    m_rightSpeedSupplier = rightSpeedSupplier;

    // 將 Drivetrain 子系統作為要求（requirement）
    // 這確保在 DefaultDrive 執行時，沒有其他指令可以控制 Drivetrain
    addRequirements(m_drivetrain);
  }
 
   // 指令在每個機器人週期持續執行時呼叫
   @Override
   public void execute() {
     // 從供應器獲取左右馬達的速度值
     double leftSpeed = m_leftSpeedSupplier.getAsDouble();
     double rightSpeed = m_rightSpeedSupplier.getAsDouble();
 
     // 將這些值傳遞給底盤子系統
     m_drivetrain.tankDrive(leftSpeed, rightSpeed);
   }

   // 指令結束時呼叫
   @Override
   public void end(boolean interrupted) {
     // 當指令結束或被中斷時，停止底盤
     m_drivetrain.stop();
   }
}
