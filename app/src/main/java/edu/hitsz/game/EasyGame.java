package edu.hitsz.game;

import android.content.Context;
import android.os.Handler;

import edu.hitsz.ImageManager;
import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.application.Publisher;
import edu.hitsz.application.Subscriber;
import edu.hitsz.factory.EnemyFactory;
import edu.hitsz.factory.MobEnemyFactory;

public class EasyGame extends BaseGame {


    @Override
    protected float eliteProbability(int time) {
        return 0;
    }

    public EasyGame(Context context, Handler handler) {
        super(context,handler);
        this.backGround = ImageManager.BACKGROUND1_IMAGE;
        this.maxEnemyNumber = 4;
        this.scoreFile = "EasyGameScore.txt";
    }

    @Override
    protected void outputInformation() {
        System.out.println("你选择的是简单模式");
        System.out.println("无boss机，无精英机，难度不随时间变化");
        System.out.println("最大飞机数为 4 个");
    }

    @Override
    protected AbstractAircraft createBoss(Publisher publisher) {
        return null;
    }

    @Override
    protected AbstractAircraft createEliteEnemy(Publisher publisher, int time) {
        return null;
    }

    @Override
    protected AbstractAircraft createMobEnemy(Publisher publisher, int time) {
        EnemyFactory enemyFactory = new MobEnemyFactory();
        AbstractAircraft newEnemy = enemyFactory.createEnemy();
        publisher.addSubscriber((Subscriber) newEnemy);
        return newEnemy;
    }

}
