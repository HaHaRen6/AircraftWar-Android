package edu.hitsz.game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import androidx.annotation.NonNull;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import edu.hitsz.DAO.ScoreDao;
import edu.hitsz.DAO.ScoreDaoImpl;
import edu.hitsz.ImageManager;
import edu.hitsz.activity.MainActivity;
import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.aircraft.BossEnemy;
import edu.hitsz.aircraft.EliteEnemy;
import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.application.Publisher;
import edu.hitsz.application.Subscriber;
import edu.hitsz.basic.AbstractFlyingObject;
import edu.hitsz.bullet.AbstractBullet;
import edu.hitsz.prop.AbstractProp;

/**
 * 【模板模式】抽象类
 * 模板模式（必做）已完成：
 * 1. 简单模式不产生boss，普通模式、困难模式产生boss
 * 2. 简单模式难度不随时间增加，普通模式
 * 3. 困难模式每次召唤改变Boss机血量
 * 模板模式（选做）已完成：
 * 1. 不同模式最大飞机数不同
 * 2. 不同模式精英敌机产生概率不同（不超过50%）
 * 3. 精英机血量随时间变化
 * 4. 普通机速度随时间变化
 * 5. 不同难度boss机产生的分数阈值不同
 * <p>
 * 包括：游戏主面板绘制逻辑，游戏执行逻辑。
 * 子类需实现抽象方法，实现相应逻辑
 *
 * @author hitsz
 */
public abstract class BaseGame extends SurfaceView implements SurfaceHolder.Callback, Runnable {

    public static final String TAG = "BaseGame";
    boolean mbLoop = false; // 控制绘画线程的标志位
    private SurfaceHolder mSurfaceHolder;
    private Canvas canvas;  // 绘图的画布
    private Paint mPaint;
    private Handler handler;

    //点击屏幕位置
    float clickX = 0, clickY = 0;

    private int backGroundTop = 0;

    /**
     * 背景图片缓存，可随难度改变
     */
    protected Bitmap backGround;


    /**
     * 时间间隔(ms)，控制刷新频率
     */
    private int timeInterval = 16;

    private final HeroAircraft heroAircraft;

    protected final List<AbstractAircraft> enemyAircrafts;

    private final List<AbstractBullet> heroBullets;
    private final List<AbstractBullet> enemyBullets;

    private List<AbstractProp> props;

    /**
     * 【数据访问对象模式】数据对象
     */
    private final ScoreDao scoreDao = new ScoreDaoImpl();

    /**
     * 【观察者模式】发布者
     */
    private final Publisher publisher = new Publisher();

//    /**
//     * 【工厂模式】敌机工厂
//     */
//    private EnemyFactory enemyFactory;

    /**
     * 最大敌机数量
     */
    protected int maxEnemyNumber;

    protected float eliteProbability;

    /**
     * 产生精英敌机的概率
     * <p>
     *
     * @param time 随时间变化
     * @return 概率（小数）
     */
    protected abstract float eliteProbability(int time);

    protected int bossScore = 400;

    private boolean gameOverFlag = false;
    private int score = 0;
    private int time = 0;

    private boolean addBoss = false;

    /**
     * 周期（ms)
     * 控制英雄机射击周期，默认值设为简单模式
     */
    private int cycleDuration = 600;
    private int cycleTime = 0;


    public BaseGame(Context context, Handler handler) {
        super(context);
        this.handler = handler;

        outputInformation();
        mbLoop = true;
        mPaint = new Paint();  //设置画笔
        mSurfaceHolder = this.getHolder();
        mSurfaceHolder.addCallback(this);
        this.setFocusable(true);
        ImageManager.initImage(context);

        // 初始化英雄机
        heroAircraft = HeroAircraft.getHeroAircraft();
        heroAircraft.setHp(1000);

        enemyAircrafts = new LinkedList<>();
        heroBullets = new LinkedList<>();
        enemyBullets = new LinkedList<>();
        props = new LinkedList<>();

        heroController();
    }

    /**
     * 游戏启动入口，执行游戏逻辑
     */
    public void action() {
        eliteProbability = eliteProbability(0);

        //TODO BGM
//        backGroundMusic = new MusicThread("src/videos/bgm.wav");
//        backGroundMusic.start();
//        backGroundMusic.setLoop(true)


        // 定时任务：绘制、对象产生、碰撞判定、击毁及结束判定
        //new Thread(new Runnable() {
        Runnable task = () -> {

            time += timeInterval;

            // 周期性执行（控制频率）
            if (timeCountAndNewCycleJudge()) {
                System.out.println(time);

//                if (enemyAircrafts.size() < maxEnemyNumber) {
//                    Log.d("BaseGame", "produceEnemy");
//                    enemyAircrafts.add(new MobEnemy(
//                            (int) (Math.random() * (MainActivity.screenWidth - ImageManager.MOB_ENEMY_IMAGE.getWidth())) * 1,
//                            (int) (Math.random() * MainActivity.screenHeight * 0.2),
//                            0,
//                            10,
//                            30
//                    ));
//                }

                /**
                 * 新敌机产生
                 */
                if (enemyAircrafts.size() < maxEnemyNumber) {
                    // 产生随机数，用判断生成普通敌机还是精英敌机
                    Random randEnemy = new Random();

                    Log.d("BaseGame", "produceEnemy");
                    if (addBoss) {
                        // 中等难度和困难难度精英敌机产生概率随时间变化
                        eliteProbability = eliteProbability(time);
                        System.out.println("精英敌机产生概率: " + eliteProbability);
                        System.out.println("当前精英机血量：" + (90 + time / 2000));
                        System.out.println("当前普通机速度：" + (6 + time / 10000));
//                        handler.handleMessage("boss!");

                        // TODO bossBGM
                        // 产生Boss机
//                        bossMusic = new MusicThread("src/videos/bgm_boss.wav");
                        AbstractAircraft newEnemy = createBoss(publisher);
                        if (newEnemy != null) {
                            enemyAircrafts.add(newEnemy);
                        }
                        addBoss = false;
                    } else if (randEnemy.nextFloat() < eliteProbability) {
                        // 产生精英敌机
                        AbstractAircraft newEnemy = createEliteEnemy(publisher, time);
                        enemyAircrafts.add(newEnemy);

                    } else {
                        // 产生普通敌机
                        AbstractAircraft newEnemy = createMobEnemy(publisher, time);
                        enemyAircrafts.add(newEnemy);
                    }
                }

                // 飞机射出子弹
                shootAction();
            }

            // 子弹移动
            bulletsMoveAction();
            // 飞机移动
            aircraftsMoveAction();
            // 道具移动
            propMoveAction();
            // 出界检测
            outCheckAction();

            // 撞击检测
            try {

                crashCheckAction();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // 后处理
            postProcessAction();

            try {
                Thread.sleep(timeInterval);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };
        new Thread(task).start();
    }

    public void heroController() {
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                clickX = motionEvent.getX();
                clickY = motionEvent.getY();
                heroAircraft.setLocation(clickX, clickY);

                if (clickX < 0 || clickX > MainActivity.screenWidth || clickY < 0 || clickY > MainActivity.screenHeight) {
                    // 防止超出边界
                    return false;
                }
                return true;
            }
        });
    }

    //***********************
    //      Action 各部分
    //***********************

    /**
     * output game's information
     */
    protected abstract void outputInformation();

    /**
     * 产生boss
     *
     * @return boss
     */
    protected abstract AbstractAircraft createBoss(Publisher publisher);

    /**
     * @return EliteEnemy
     */
    protected abstract AbstractAircraft createEliteEnemy(Publisher publisher, int time);

    /**
     * @return MobEnemy
     */
    protected abstract AbstractAircraft createMobEnemy(Publisher publisher, int time);

    private void shootAction() {
        // 英雄射击
        heroBullets.addAll(heroAircraft.shoot());
        // 敌机射击
        for (AbstractAircraft enemyAircraft : enemyAircrafts) {
            List<AbstractBullet> bullets = enemyAircraft.shoot();
            enemyBullets.addAll(bullets);
            for (AbstractBullet bullet : bullets) {
                publisher.addSubscriber(bullet);
            }
        }
    }

    private boolean timeCountAndNewCycleJudge() {
        cycleTime += timeInterval;
        if (cycleTime >= cycleDuration && cycleTime - timeInterval < cycleTime) {
            // 跨越到新的周期
            cycleTime %= cycleDuration;
            return true;
        } else {
            return false;
        }
    }

    private void bulletsMoveAction() {
        for (AbstractBullet bullet : heroBullets) {
            bullet.forward();
        }
        for (AbstractBullet bullet : enemyBullets) {
            bullet.forward();
        }
    }

    private void aircraftsMoveAction() {
        for (AbstractAircraft enemyAircraft : enemyAircrafts) {
            enemyAircraft.forward();
        }
    }

    private void propMoveAction() {
        for (AbstractProp prop : props) {
            prop.forward();
        }
    }

    /**
     * 分数文件名字
     */
    protected String scoreFile;

    private void outCheckAction() {
        for (AbstractAircraft enemyAircraft : enemyAircrafts) {
            if (enemyAircraft.getLocationY() >= MainActivity.screenHeight) {
                publisher.removeSubscriber((Subscriber) enemyAircraft);
                enemyAircraft.vanish();
            }
        }
        for (AbstractBullet bullet : enemyBullets) {
            // 判定 x 轴出界
            if (bullet.getLocationX() <= 0 || bullet.getLocationX() >= MainActivity.screenWidth) {
                publisher.removeSubscriber(bullet);
                bullet.vanish();
            }
            if (bullet.getLocationY() >= MainActivity.screenHeight) {
                publisher.removeSubscriber(bullet);
                bullet.vanish();
            }
        }
        for (AbstractBullet bullet : heroBullets) {
            // 判定 x 轴出界
            if (bullet.getLocationX() <= 0 || bullet.getLocationX() >= MainActivity.screenWidth) {
                bullet.vanish();

            } else if (bullet.getLocationY() <= 0) {
                bullet.vanish();
            }
        }
    }

    /**
     * 碰撞检测：
     * 碰撞检测：
     * 1. 敌机攻击英雄
     * 2. 英雄攻击/撞击敌机
     * 3. 英雄获得补给
     */
    private void crashCheckAction() throws InterruptedException {
        // 敌机子弹攻击英雄
        for (AbstractBullet bullet : enemyBullets) {
            if (bullet.notValid()) {
                continue;
            }
            if (heroAircraft.notValid()) {
                // 避免多个子弹重复击毁的判定
                continue;
            }
            if (heroAircraft.crash(bullet)) {
                // 英雄机撞击到敌机子弹，英雄机损失一定生命值
                heroAircraft.decreaseHp(bullet.getPower());
                publisher.removeSubscriber(bullet);
                bullet.vanish();
            }
        }

        // 英雄子弹攻击敌机
        for (AbstractBullet bullet : heroBullets) {
            if (bullet.notValid()) {
                continue;
            }
            for (AbstractAircraft enemyAircraft : enemyAircrafts) {
                if (enemyAircraft.notValid()) {
                    // 已被其他子弹击毁的敌机，不再检测，避免多个子弹重复击毁同一敌机的判定
                    continue;
                }
                if (enemyAircraft.crash(bullet)) {
                    // TODO music
                    /*
                    // 播放音乐
                    MusicThread m = new MusicThread("src/videos/bullet_hit.wav");
                    m.start();*/


                    // 敌机撞击到英雄机子弹
                    // 敌机损失一定生命值
                    enemyAircraft.decreaseHp(bullet.getPower());
                    bullet.vanish();
                    enemyCheckDeath(enemyAircraft);
                }
                // 英雄机 与 敌机 相撞，均损毁
                if (enemyAircraft.crash(heroAircraft) || heroAircraft.crash(enemyAircraft)) {
                    enemyAircraft.vanish();
                    heroAircraft.decreaseHp(Integer.MAX_VALUE);
                }
            }
        }

        // 我方获得补给
        // !! 不能改成 for-each !!
        for (int i = 0; i < props.size(); i++) {
            AbstractProp prop = props.get(i);
            if (prop.notValid()) {
                continue;
            }
            if (heroAircraft.crash(prop)) {
                prop.active(heroAircraft, publisher);
                for (AbstractAircraft enemyAircraft : enemyAircrafts) {
                    enemyCheckDeath(enemyAircraft);
                }
                prop.vanish();
            }
        }
    }

    public void scoreAdd() {
        score += 10;
        if (score % bossScore == 0) {
            addBoss = true;
        }
    }

    public void enemyCheckDeath(AbstractAircraft enemyAircraft) {
        if (enemyAircraft.notValid()) {
            // 获得分数，获得道具补给
            scoreAdd();
            if (enemyAircraft instanceof BossEnemy) {
//                bossMusic.setLoop(false);
//                bossMusic.stopMusic();
//                backGroundMusic = new MusicThread("src/videos/bgm.wav");
//                backGroundMusic.start();
//                backGroundMusic.setLoop(true);
                ((BossEnemy) enemyAircraft).dropProp(props);
            }
            if (enemyAircraft instanceof EliteEnemy) {
                ((EliteEnemy) enemyAircraft).dropProp(props);
            }
            publisher.removeSubscriber((Subscriber) enemyAircraft);
        }
    }

    /**
     * 后处理：
     * 1. 删除无效的子弹
     * 2. 删除无效的敌机
     * 3. 检查英雄机生存
     * <p>
     * 无效的原因可能是撞击或者飞出边界
     */
    private void postProcessAction() {
        enemyBullets.removeIf(AbstractFlyingObject::notValid);
        heroBullets.removeIf(AbstractFlyingObject::notValid);
        enemyAircrafts.removeIf(AbstractFlyingObject::notValid);
        props.removeIf(AbstractFlyingObject::notValid);

        if (heroAircraft.getHp() <= 0) {
//            if (backGroundMusic != null) {
//                backGroundMusic.stopMusic();
//            }
//            if (bossMusic != null) {
//                bossMusic.stopMusic();
//            }
//            MusicThread m = new MusicThread("src/videos/game_over.wav");
//            m.start();

//            // 以设定格式获取当前时间
//            Date date = new Date();
//            SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd HH:mm");
//
//            // 产生本次成绩
//            ScoreInfo scoreInfo = new ScoreInfo();
//            scoreInfo.setScore(score);
//            scoreInfo.setDate(dateFormat.format(date));
//            System.out.println("Your score: " + score);
//
//            // 成绩处理
//            AskName askName = new AskName(scoreInfo, scoreDao, getScoreFile(), this);
//            Main.cardPanel.add(askName.getMainPanel());
//            Main.frame.setSize(200, 200);
//            Main.cardLayout.last(Main.cardPanel);

            gameOverFlag = true;
            mbLoop = false;
            Log.i(TAG, "heroAircraft is not Valid");

        }

    }

    //***********************
    //      Paint 各部分
    //***********************

    public void draw() {
        canvas = mSurfaceHolder.lockCanvas();
        if (mSurfaceHolder == null || canvas == null) {
            return;
        }

        //绘制背景，图片滚动
        canvas.drawBitmap(backGround, 0, this.backGroundTop - backGround.getHeight(), mPaint);
        canvas.drawBitmap(backGround, 0, this.backGroundTop, mPaint);
        backGroundTop += 1;
        if (backGroundTop == MainActivity.screenHeight)
            this.backGroundTop = 0;

        //先绘制子弹，后绘制飞机
        paintImageWithPositionRevised(enemyBullets); // 敌机子弹

        paintImageWithPositionRevised(heroBullets);  // 英雄机子弹

        paintImageWithPositionRevised(enemyAircrafts); // 敌机

        paintImageWithPositionRevised(props);// 道具


        canvas.drawBitmap(ImageManager.HERO_IMAGE,
                heroAircraft.getLocationX() - ImageManager.HERO_IMAGE.getWidth() / 2,
                heroAircraft.getLocationY() - ImageManager.HERO_IMAGE.getHeight() / 2,
                mPaint);

        //画生命值
        paintScoreAndLife();

        mSurfaceHolder.unlockCanvasAndPost(canvas);

    }

    private void paintImageWithPositionRevised(List<? extends AbstractFlyingObject> objects) {
        if (objects.size() == 0) {
            return;
        }

        for (AbstractFlyingObject object : objects) {
            Bitmap image = object.getImage();
            assert image != null : objects.getClass().getName() + " has no image! ";
            canvas.drawBitmap(image, object.getLocationX() - image.getWidth() / 2,
                    object.getLocationY() - image.getHeight() / 2, mPaint);
        }
    }

    private void paintScoreAndLife() {
        int x = 10;
        int y = 40;

        mPaint.setColor(Color.RED);
        mPaint.setTextSize(50);

        canvas.drawText("SCORE:" + this.score, x, y, mPaint);
        y = y + 60;
        canvas.drawText("LIFE:" + this.heroAircraft.getHp(), x, y, mPaint);
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        new Thread(this).start();
        Log.i(TAG, "start surface view thread");
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        MainActivity.screenWidth = i1;
        MainActivity.screenHeight = i2;
    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {
        mbLoop = false;
    }

    @Override
    public void run() {

        while (mbLoop) {   //游戏结束停止绘制
            synchronized (mSurfaceHolder) {
                action();
                draw();
            }
        }
        Message message = Message.obtain();
        message.what = 1;
        handler.sendMessage(message);
    }
}
