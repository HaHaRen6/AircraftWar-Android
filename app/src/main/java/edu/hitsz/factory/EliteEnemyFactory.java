package edu.hitsz.factory;

import java.util.Random;

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
    public EliteEnemy createEnemy(Long seed) {
        Random randonX = new Random();
        randonX.setSeed(seed);
        EliteEnemy eliteEnemy = new EliteEnemy((int) (randonX.nextDouble() * (MainActivity.screenWidth - 2 * ImageManager.ELITE_ENEMY_IMAGE.getWidth()) + ImageManager.ELITE_ENEMY_IMAGE.getWidth()),
                (int) (Math.random() * MainActivity.screenHeight * 0.03),
                0,
                6,
                90);
        eliteEnemy.setShootStrategy(new DirectShootStrategy());
        return eliteEnemy;
    }
}
