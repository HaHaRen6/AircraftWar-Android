package edu.hitsz.factory;

import java.util.Random;

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
    public BossEnemy createEnemy(Long seed) {
        Random randonX = new Random(seed);
        BossEnemy bossEnemy = new BossEnemy((int) (randonX.nextDouble() * (MainActivity.screenWidth - 2 * ImageManager.BOSS_ENEMY_IMAGE.getWidth()) + ImageManager.BOSS_ENEMY_IMAGE.getWidth()),
                (int) (ImageManager.BOSS_ENEMY_IMAGE.getHeight() / 2),
                4,
                0,
                240);
        bossEnemy.setShootStrategy(new ScatterShootStrategy());
        return bossEnemy;
    }
}
