package edu.hitsz.factory;

import edu.hitsz.ImageManager;
import edu.hitsz.activity.MainActivity;
import edu.hitsz.aircraft.BossEnemy;
import edu.hitsz.shootStrategy.ScatterShootStrategy;

/**
 * EliteEnemyFactory
 *
 * @author hhr
 */
public class BossEnemyFactory implements EnemyFactory {

    @Override
    public BossEnemy createEnemy() {
        BossEnemy bossEnemy = new BossEnemy((int) (Math.random() * (MainActivity.screenWidth - 2 * ImageManager.BOSS_ENEMY_IMAGE.getWidth()) + ImageManager.BOSS_ENEMY_IMAGE.getWidth()),
                (int) (ImageManager.BOSS_ENEMY_IMAGE.getHeight() / 2),
                4,
                0,
                240);
        bossEnemy.setShootStrategy(new ScatterShootStrategy());
        return bossEnemy;
    }
}
