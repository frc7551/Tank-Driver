// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants. This class should not be used for any other purpose. All constants should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {
  public static class OperatorConstants {
    public static final int kXboxControllerPort = 0;

    public static final double kDriveSpeedMultiplier = 0.5; // Speed multiplier for driving commands
    public static final double kTurnSpeedMultiplier = 0.5; // Speed multiplier for turning commands

      // *** 可調整的斜坡參數 ***
    // 每次執行時速度變化的最大值 (約每 20ms 的變化量)
    // 這些值需要根據機器人實際表現進行調優
    public static final double MAX_ACCELERATION_CHANGE = 0.05; // 加速時每次迭代的最大變化量
    public static final double MAX_DECCELERATION_CHANGE = 0.02; // 減速時每次迭代的最大變化量 (通常小於加速，提供緩煞車)
    public static final double STOP_THRESHOLD = 0.05; // 搖桿輸入小於此值，則視為停止輸入，啟動減速邏輯
  }
}
