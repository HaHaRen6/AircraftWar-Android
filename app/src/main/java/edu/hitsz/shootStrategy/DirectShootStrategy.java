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
public class DirectShootStrategy implements ShootStrategy {
    /**
     * 通过射击产生子弹
     *
     * @return 射击出的子弹List
     */
    public List<AbstractBullet> shoot(int aircraftX, int aircraftY, int aircraftSpeedY, int direction, int power) {
        List<AbstractBullet> res = new LinkedList<>();
        int x = aircraftX;
        int y = aircraftY + direction * 2;
        int speedX = 0;
        int speedY = aircraftSpeedY + direction * 7;
        AbstractBullet bullet;

        if (direction == -1) {
            bullet = new HeroBullet(x, y, speedX, -11, power);
        } else {
            bullet = new EnemyBullet(x, y, speedX, speedY, power);
        }
        res.add(bullet);

        return res;
    }
}
