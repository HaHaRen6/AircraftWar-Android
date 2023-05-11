package edu.hitsz.factory;

import edu.hitsz.ImageManager;
import edu.hitsz.activity.MainActivity;
import edu.hitsz.aircraft.MobEnemy;

/**
 * 【工厂模式】实现工厂接口的具体工厂类，充当具体创建者角色
 *
 * @author hhr
 */
public class MobEnemyFactory implements EnemyFactory {

    @Override
    public MobEnemy createEnemy() {
        return new MobEnemy((int) (Math.random() * (MainActivity.screenWidth - 2 * ImageManager.MOB_ENEMY_IMAGE.getWidth()) + ImageManager.MOB_ENEMY_IMAGE.getWidth()),
                (int) (Math.random() * MainActivity.screenHeight * 0.03),
                0,
                6,
                30);
    }
}

