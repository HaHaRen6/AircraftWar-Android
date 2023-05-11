package edu.hitsz.shootStrategy;

import java.util.LinkedList;
import java.util.List;

import edu.hitsz.bullet.AbstractBullet;
import edu.hitsz.bullet.EnemyBullet;
import edu.hitsz.bullet.HeroBullet;

/**
 * 【策略模式】具体策略(concrete strategy)
 *
 * @author hhr
 */
public class ScatterShootStrategy implements ShootStrategy {
    private int shootNum = 3;

    /**
     * 通过射击产生子弹
     *
     * @return 射击出的子弹List
     */
    public List<AbstractBullet> shoot(int aircraftX, int aircraftY, int aircraftSpeedY, int direction, int power) {
        List<AbstractBullet> res = new LinkedList<>();
        int x = aircraftX;
        int y = aircraftY + direction * 5;
        int speedX = 0;
        int heroSpeedY = -11;
        int enemySpeedY = 13;
        AbstractBullet bullet;
        for (int i = 0; i < shootNum; i++) {
            // 子弹发射位置相对飞机位置向前偏移
            // 多个子弹横向分散
            if (direction == -1) {
                bullet = new HeroBullet(x + (i * 2 - shootNum + 1) * 3, y - 5, speedX + (i * 2 - shootNum + 1), heroSpeedY, power);
            } else {
                bullet = new EnemyBullet(x + (i * 2 - shootNum + 1) * 3, y + 90, speedX + (i * 2 - shootNum + 1), enemySpeedY, power);
            }
            res.add(bullet);
        }
        return res;
    }
}
