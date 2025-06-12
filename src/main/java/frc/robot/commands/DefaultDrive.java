package frc.robot.commands;

import frc.robot.subsystems.Drivetrain;
import edu.wpi.first.wpilibj2.command.Command;
import java.util.function.DoubleSupplier;

public class DefaultDrive extends Command {
    private final Drivetrain m_drivetrain;
    private final DoubleSupplier m_speedSupplier;
    private final DoubleSupplier m_rotationSupplier;

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
 
   // 指令在每個機器人週期持續執行時呼叫
   @Override
   public void execute() {
     // 從供應器獲取左右馬達的速度值
     double speed = m_speedSupplier.getAsDouble();
     double rotation = m_rotationSupplier.getAsDouble();
 
     // 將這些值傳遞給底盤子系統
     m_drivetrain.arcadeDrive(speed, rotation);
   }

   // 指令結束時呼叫
   @Override
   public void end(boolean interrupted) {
     // 當指令結束或被中斷時，停止底盤
     m_drivetrain.stop();
   }
}
