package edu.hitsz.factory;

import edu.hitsz.ImageManager;
import edu.hitsz.activity.MainActivity;
import edu.hitsz.aircraft.EliteEnemy;
import edu.hitsz.shootStrategy.DirectShootStrategy;

/**
 * EliteEnemyFactory
 *
 * @author hhr
 */
public class EliteEnemyFactory implements EnemyFactory {

    @Override
    public EliteEnemy createEnemy() {
        EliteEnemy eliteEnemy = new EliteEnemy((int) (Math.random() * (MainActivity.screenWidth - 2 * ImageManager.ELITE_ENEMY_IMAGE.getWidth()) + ImageManager.ELITE_ENEMY_IMAGE.getWidth()),
                (int) (Math.random() * MainActivity.screenHeight * 0.03),
                0,
                6,
                90);
        eliteEnemy.setShootStrategy(new DirectShootStrategy());
        return eliteEnemy;
    }
}
