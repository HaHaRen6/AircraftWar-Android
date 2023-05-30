package edu.hitsz.game;

import android.content.Context;
import android.os.Handler;

import edu.hitsz.ImageManager;
import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.application.Publisher;
import edu.hitsz.application.Subscriber;
import edu.hitsz.factory.BossEnemyFactory;
import edu.hitsz.factory.EliteEnemyFactory;
import edu.hitsz.factory.EnemyFactory;
import edu.hitsz.factory.MobEnemyFactory;

public class MediumGame extends BaseGame {
    @Override
    protected float eliteProbability(int time) {
        return (float) Math.min(0.15 + (float) time / 200000, 0.5);
    }

    public MediumGame(Context context, Handler handler) {
        super(context,handler);
        this.backGround = ImageManager.BACKGROUND2_IMAGE;
        this.maxEnemyNumber = 5;
        this.bossScore = 400;
        scoreFile = "MediumGameScore.txt";
    }


    @Override
    protected AbstractAircraft createBoss(Publisher publisher, Long seed) {
        EnemyFactory enemyFactory = new BossEnemyFactory();
        AbstractAircraft newEnemy = enemyFactory.createEnemy(seed);
        publisher.addSubscriber((Subscriber) newEnemy);
        return newEnemy;
    }

    @Override
    protected AbstractAircraft createEliteEnemy(Publisher publisher, int time, Long seed) {
        EnemyFactory enemyFactory = new EliteEnemyFactory();
        AbstractAircraft newEnemy = enemyFactory.createEnemy(seed);
        newEnemy.setHp(90 + time / 2000);
        publisher.addSubscriber((Subscriber) newEnemy);
        return newEnemy;
    }

    @Override
    protected AbstractAircraft createMobEnemy(Publisher publisher, int time, Long seed) {
        EnemyFactory enemyFactory = new MobEnemyFactory();
        AbstractAircraft newEnemy = enemyFactory.createEnemy(seed);
        newEnemy.setSpeedY(6 + time / 10000);
        publisher.addSubscriber((Subscriber) newEnemy);
        return newEnemy;
    }
}
