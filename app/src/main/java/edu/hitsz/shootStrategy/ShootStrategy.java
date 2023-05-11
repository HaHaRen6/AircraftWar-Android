package edu.hitsz.shootStrategy;

import java.util.List;

import edu.hitsz.bullet.AbstractBullet;

/**
 * 【策略模式】策略(strategy)：接口是所有具体策略的通用接口
 *
 * @author hhr
 */
public interface ShootStrategy {
    /**
     * 射击方法
     * @param aircraftX x坐标
     * @param aircraftY y坐标
     * @param aircraftSpeedY 飞机速度
     * @param direction 射击方向
     * @param power 子弹伤害
     * @return 射出的子弹序列
     */
    List<AbstractBullet> shoot(int aircraftX, int aircraftY, int aircraftSpeedY, int direction, int power);
}
