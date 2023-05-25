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

public class HardGame extends BaseGame {

    private int bossHp = 240;

    public HardGame(Context context, Handler handler) {
        super(context,handler);
        this.backGround = ImageManager.BACKGROUND3_IMAGE;
        this.maxEnemyNumber = 7;
        this.bossScore = 300;
        scoreFile = "HardGameScore.txt";
    }

    @Override
    protected AbstractAircraft createBoss(Publisher publisher) {
        EnemyFactory enemyFactory = new BossEnemyFactory();
        AbstractAircraft newEnemy = enemyFactory.createEnemy();
        newEnemy.setHp(bossHp);
        System.out.println("Boss机血量：" + bossHp);
        bossHp += 60;
        publisher.addSubscriber((Subscriber) newEnemy);

        return newEnemy;
    }

    @Override
    protected float eliteProbability(int time) {
        return (float) Math.min(0.15 + (float) time / 200000, 0.5);
    }

    @Override
    protected AbstractAircraft createEliteEnemy(Publisher publisher, int time){
        EnemyFactory enemyFactory = new EliteEnemyFactory();
        AbstractAircraft newEnemy = enemyFactory.createEnemy();
        newEnemy.setHp(90 + time / 2000);
        publisher.addSubscriber((Subscriber) newEnemy);
        return newEnemy;
    }

    @Override
    protected AbstractAircraft createMobEnemy(Publisher publisher, int time) {
        EnemyFactory enemyFactory = new MobEnemyFactory();
        AbstractAircraft newEnemy = enemyFactory.createEnemy();
        newEnemy.setSpeedY(6 + time / 10000);
        publisher.addSubscriber((Subscriber) newEnemy);
        return newEnemy;
    }
}
