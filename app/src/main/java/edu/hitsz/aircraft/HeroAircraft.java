package edu.hitsz.aircraft;

import java.util.List;

import edu.hitsz.ImageManager;
import edu.hitsz.activity.MainActivity;
import edu.hitsz.bullet.AbstractBullet;
import edu.hitsz.shootStrategy.DirectShootStrategy;

/**
 * 英雄飞机，游戏玩家操控，遵循单例模式（singleton)
 * 【单例模式】
 * 【策略模式】客户端(client)：利用 shootStrategy 产生特定的子弹
 *
 * @author hitsz
 */
public class HeroAircraft extends AbstractAircraft {

    /**
     * 子弹伤害
     */
    private int power = 30;

    /**
     * 子弹射击方向 (向下发射：1，向上发射：-1)
     */
    private int direction = -1;

/*
        volatile 修饰，
        singleton = new Singleton() 可以拆解为3步：
        1、分配对象内存(给singleton分配内存)
        2、调用构造器方法，执行初始化（调用 Singleton 的构造函数来初始化成员变量）。
        3、将对象引用赋值给变量(执行完这步 singleton 就为非 null 了)。
        若发生重排序，假设 A 线程执行了 1 和 3 ，还没有执行 2，B 线程来到判断 NULL，B 线程就会直接返回还没初始化的 instance 了。
        volatile 可以避免重排序。
    */
    /**
     * 英雄机对象单例
     */
    private volatile static HeroAircraft heroAircraft;

    /**
     * 单例模式：私有化构造方法
     */
    private HeroAircraft() {
        super(MainActivity.screenWidth / 2, MainActivity.screenHeight - ImageManager.HERO_IMAGE.getHeight(),
                0, 0, 1000);
    }


    /**
     * 通过单例模式获得初始化英雄机
     * 【单例模式：双重校验锁方法】
     *
     * @return 英雄机单例
     */
    public static HeroAircraft getHeroAircraft() {
        if (heroAircraft == null) {
            synchronized (HeroAircraft.class) {
                if (heroAircraft == null) {
                    heroAircraft = new HeroAircraft();
                    heroAircraft.setShootStrategy(new DirectShootStrategy());
                }
            }
        }
        return heroAircraft;
    }

    @Override
    public void forward() {
        // 英雄机由鼠标控制，不通过forward函数移动
    }

    @Override
    public List<AbstractBullet> shoot() {
        // TODO rate可以改改
        return shootStrategy.shoot(getLocationX(), getLocationY(), getSpeedY(), direction, power);
    }
}
