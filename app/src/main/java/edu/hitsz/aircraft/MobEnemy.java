package edu.hitsz.aircraft;


import java.util.LinkedList;
import java.util.List;

import edu.hitsz.bullet.AbstractBullet;


/**
 * 普通敌机
 * 不可射击
 *
 * @author hitsz
 */
public class MobEnemy extends AbstractAircraft {

    public MobEnemy(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp);
    }


    @Override
    public List<AbstractBullet> shoot() {
        return new LinkedList<>();
    }

}
