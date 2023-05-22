package edu.hitsz.game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import edu.hitsz.DAO.ScoreDao;
import edu.hitsz.DAO.ScoreDaoImpl;
import edu.hitsz.ImageManager;
import edu.hitsz.R;
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
     * 【模板模式】
     * 背景图片缓存，可随难度改变
     */
    protected Bitmap backGround;


    /**
     * 时间间隔(ms)，控制刷新频率
     * TODO
     */
    private int timeInterval = 16;

    private HeroAircraft heroAircraft;
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

    /**
     * 最大敌机数量
     */
    protected int maxEnemyNumber;

    protected float eliteProbability;

    /**
     * 产生精英敌机的概率
     *
     * @param time 随时间变化
     * @return 概率（小数）
     */
    protected abstract float eliteProbability(int time);

    /**
     * 产生boss的分数间隔
     */
    protected int bossScore = 400;

    /**
     * 游戏结束标志
     */
    public boolean gameOverFlag = false;

    /**
     * 游戏得分
     */
    private static int score = 0;

    /**
     * 游戏时间 (ms)
     */
    private int time = 0;

    /**
     * 用于标记下一次产生敌机时，是否添加boss
     */
    private boolean addBoss = false;

    /**
     * 周期 (ms)
     * <p>
     * 控制英雄机射击周期，默认值设为简单模式
     * TODO
     */
    private int cycleDuration = 600;
    private int cycleTime = 0;

    /*
     * 声音相关
     * 1. 通过 SoundPool 实现
     * 2. 通过 MediaPlayer 实现
     */
    public SoundPool mySoundPool = null;
    public HashMap<String, Integer> soundPoolMap = new HashMap<>();
    public MediaPlayer bgmMediaPlayer;
    public MediaPlayer bossMediaPlayer;


    public BaseGame(Context context, Handler handler, Boolean musicSwitch) {
        super(context);
        this.handler = handler;

        mbLoop = true;
        mPaint = new Paint();  //设置画笔
        mSurfaceHolder = this.getHolder();
        mSurfaceHolder.addCallback(this);
        this.setFocusable(true);
        ImageManager.initImage(context);
        score = 0;

        // 降低耦合
        if (musicSwitch) {
            bgmMediaPlayer = MediaPlayer.create(context, R.raw.bgm);
            bgmMediaPlayer.start();
            bgmMediaPlayer.setLooping(true);
            bossMediaPlayer = MediaPlayer.create(context, R.raw.bgm_boss);
            bossMediaPlayer.setLooping(true);

            createSoundPool();
            soundPoolMap.put("bullet_hit", mySoundPool.load(context, R.raw.bullet_hit, 1));
            soundPoolMap.put("bomb_explosion", mySoundPool.load(context, R.raw.bomb_explosion, 1));
            soundPoolMap.put("game_over", mySoundPool.load(context, R.raw.game_over, 1));
            soundPoolMap.put("get_supply", mySoundPool.load(context, R.raw.get_supply, 1));
        }

        // 初始化
        heroAircraft = HeroAircraft.getHeroAircraft();
        enemyAircrafts = new LinkedList<>();
        heroBullets = new LinkedList<>();
        enemyBullets = new LinkedList<>();
        props = new LinkedList<>();
        eliteProbability = eliteProbability(0);

        heroController();

    }

    /**
     * 游戏启动入口，执行游戏逻辑
     */
    public void action() {

        // 定时任务：绘制、对象产生、碰撞判定、击毁及结束判定
        //new Thread(new Runnable() {
        Runnable task = () -> {

            time += timeInterval;

            // 周期性执行（控制频率）
            if (timeCountAndNewCycleJudge()) {
                System.out.println(time);

                /**
                 * 新敌机产生
                 */
                if (enemyAircrafts.size() < maxEnemyNumber) {
                    // 产生随机数，用判断生成普通敌机还是精英敌机
                    Random randEnemy = new Random();

                    if (addBoss) {
                        // 中等难度和困难难度精英敌机产生概率随时间变化
                        eliteProbability = eliteProbability(time);

                        // bossBGM
                        if (soundPoolMap != null) {
                            bgmMediaPlayer.pause();
                            bossMediaPlayer.start();
                        }

                        Message message = Message.obtain();
                        message.what = 2;
                        handler.sendMessage(message);

                        Log.d("BaseGame", "produceBoss");
                        AbstractAircraft newEnemy = createBoss(publisher);
                        if (newEnemy != null) {
                            enemyAircrafts.add(newEnemy);
                        }
                        addBoss = false;
                    } else if (randEnemy.nextFloat() < eliteProbability) {
                        // 产生精英敌机
                        Log.d("BaseGame", "produceEliteEnemy");
                        AbstractAircraft newEnemy = createEliteEnemy(publisher, time);
                        enemyAircrafts.add(newEnemy);
                    } else {
                        // 产生普通敌机
                        Log.d("BaseGame", "produceMobEnemy");
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


    /**
     * 控制英雄机移动
     */
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

    /**
     * @return 游戏分数
     */
    public static int getScore() {
        return score;
    }

    //***********************
    //      Action 各部分
    //***********************

    /**
     * 产生boss
     *
     * @return boss
     */
    protected abstract AbstractAircraft createBoss(Publisher publisher);

    /**
     * 产生精英机
     *
     * @return EliteEnemy
     */
    protected abstract AbstractAircraft createEliteEnemy(Publisher publisher, int time);

    /**
     * 产生普通机
     *
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


    /**
     * @return 是否跨越到新的周期 (boolean)
     */
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
    public static String scoreFile;

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

    private void createSoundPool() {
        if (mySoundPool == null) {
            // Android 5.0 及 之后版本
            AudioAttributes audioAttributes = new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_MEDIA).setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).build();
            mySoundPool = new SoundPool.Builder().setMaxStreams(10).setAudioAttributes(audioAttributes).build();
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
        for (int i = 0; i < enemyBullets.size(); i++) {
            if (enemyBullets.get(i).notValid()) {
                continue;
            }
            if (heroAircraft.notValid()) {
                // 避免多个子弹重复击毁的判定
                continue;
            }
            if (heroAircraft.crash(enemyBullets.get(i))) {
                // 英雄机撞击到敌机子弹，英雄机损失一定生命值
                heroAircraft.decreaseHp(enemyBullets.get(i).getPower());
                publisher.removeSubscriber(enemyBullets.get(i));
                enemyBullets.get(i).vanish();
            }
        }

        // 英雄子弹攻击敌机
        for (int i = 0; i < heroBullets.size(); i++) {
            if (heroBullets.get(i).notValid()) {
                continue;
            }
            for (int j = 0; j < enemyAircrafts.size(); j++) {
                if (enemyAircrafts.get(j).notValid()) {
                    // 已被其他子弹击毁的敌机，不再检测，避免多个子弹重复击毁同一敌机的判定
                    continue;
                }
                if (enemyAircrafts.get(j).crash(heroBullets.get(i))) {
                    if (mySoundPool != null && soundPoolMap.get("bullet_hit") != null) {
                        mySoundPool.play(soundPoolMap.get("bullet_hit"), 1, 1, 0, 0, 1f);
                    }

                    // 敌机撞击到英雄机子弹
                    // 敌机损失一定生命值
                    enemyAircrafts.get(j).decreaseHp(heroBullets.get(i).getPower());
                    heroBullets.get(i).vanish();
                    enemyCheckDeath(enemyAircrafts.get(j));

                }
                // 英雄机 与 敌机 相撞，均损毁
                if (enemyAircrafts.get(j).crash(heroAircraft) || heroAircraft.crash(enemyAircrafts.get(j))) {
                    enemyAircrafts.get(j).vanish();
                    heroAircraft.decreaseHp(Integer.MAX_VALUE);
                }
            }
        }

        // 我方获得补给
        for (int i = 0; i < props.size(); i++) {
            AbstractProp prop = props.get(i);
            if (prop.notValid()) {
                continue;
            }
            if (heroAircraft.crash(prop)) {
                prop.active(heroAircraft, publisher, mySoundPool, soundPoolMap);
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

    /**
     * 检查敌机死亡状态
     *
     * @param enemyAircraft 敌机对象
     */
    public void enemyCheckDeath(AbstractAircraft enemyAircraft) {
        if (enemyAircraft.notValid()) {
            // 获得分数，获得道具补给
            scoreAdd();
            if (enemyAircraft instanceof BossEnemy) {
                if (soundPoolMap != null) {
                    bossMediaPlayer.pause();
                    bgmMediaPlayer.start();
                }
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
            if (mySoundPool != null && soundPoolMap.get("game_over") != null) {
                mySoundPool.play(soundPoolMap.get("game_over"), 1, 1, 0, 0, 1f);
            }

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

        for (int i = 0; i < objects.size(); i++) {
            Bitmap image = objects.get(i).getImage();
            assert image != null : objects.getClass().getName() + " has no image! ";
            canvas.drawBitmap(image, objects.get(i).getLocationX() - image.getWidth() / 2,
                    objects.get(i).getLocationY() - image.getHeight() / 2, mPaint);
        }
    }

    private void paintScoreAndLife() {
        int x = 10;
        int y = 75;

        mPaint.setColor(Color.RED);
        mPaint.setTextSize(75);
        canvas.drawText("分数:" + this.score, x, y, mPaint);
        y = y + 75;
        canvas.drawText("血量:" + this.heroAircraft.getHp(), x, y, mPaint);
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

        // 控制 action
        new Thread(() -> {
            while (mbLoop) {
                action();
                try {
                    Thread.sleep(timeInterval);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            // 延迟 1 秒释放 mySoundPool，让 game_over 音效有时间播放
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            if (mySoundPool != null) {
                mySoundPool.release();
            }
        }).start();

        //游戏结束停止绘制
        while (mbLoop) {
            synchronized (mSurfaceHolder) {
                draw();
            }
        }

        if (mySoundPool != null) {
            bgmMediaPlayer.stop();
            bossMediaPlayer.stop();
            bgmMediaPlayer.release();
            bossMediaPlayer.release();
        }

        Message message = Message.obtain();
        message.what = 1;
        handler.sendMessage(message);
    }
}
