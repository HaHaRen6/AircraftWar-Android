package edu.hitsz.prop;

import edu.hitsz.activity.MainActivity;
import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.application.Publisher;
import edu.hitsz.basic.AbstractFlyingObject;

/**
 * 道具类
 *
 * @author hhr
 */
public abstract class AbstractProp extends AbstractFlyingObject {

    public AbstractProp(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);
    }

    @Override
    public void forward() {
        super.forward();

        // 判定 x 轴出界
        if (locationX <= 0 || locationX >= MainActivity.screenWidth) {
            vanish();
        }

        // 判定 y 轴出界
        if (speedY > 0 && locationY >= MainActivity.screenHeight) {
            // 向下飞行出界
            vanish();
        } else if (locationY <= 0) {
            // 向上飞行出界
            vanish();
        }
    }

    /**
     * 抽象方法active
     * <p>
     * 道具起作用
     */
    public abstract void active(HeroAircraft heroAircraft, Publisher publisher);
}
