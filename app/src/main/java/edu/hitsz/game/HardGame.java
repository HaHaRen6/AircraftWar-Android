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
        this.scoreFile = "HardGameScore.txt";
    }

    @Override
    protected void outputInformation(){
        System.out.println("你选择的是困难模式");
        System.out.println("有boss机，有精英机，难度随时间变化");
        System.out.println("boss机血量逐次递增");
        System.out.println("最大飞机数为 7 个");
        System.out.println("精英机产生概率随时间增加");
        System.out.println("精英机血量随时间变化");
        System.out.println("普通机速度随时间变化");
        System.out.println("产生boss机的分数阈值为 300 分");
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
        return (float) Math.min(0.1 + (float) time / 200000, 0.5);
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
