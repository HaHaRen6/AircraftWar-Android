package edu.hitsz.aircraft;


import java.util.LinkedList;
import java.util.List;

import edu.hitsz.application.Subscriber;
import edu.hitsz.bullet.AbstractBullet;
import edu.hitsz.prop.AbstractProp;


/**
 * 普通敌机
 * 不可射击
 *
 * @author hitsz
 */
public class MobEnemy extends AbstractAircraft implements Enemy, Subscriber {

    public MobEnemy(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp);
    }

    @Override
    public List<AbstractBullet> shoot() {
        return new LinkedList<>();
    }

    @Override
    public void dropProp(List<AbstractProp> props) {
    }

    @Override
    public void update() {
        vanish();
    }
}
