package edu.hitsz.prop;

import android.media.SoundPool;

import java.util.HashMap;

import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.application.Publisher;

/**
 * 火力道具
 *
 * @Author hhr
 */
public class BombProp extends AbstractProp {

    public BombProp(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);
    }

    @Override
    public void active(HeroAircraft heroAircraft, Publisher publisher, SoundPool mySoundPool, HashMap<String, Integer> soundPoolMap) {
        if (mySoundPool != null && soundPoolMap.get("bomb_explosion") != null) {
            mySoundPool.play(soundPoolMap.get("bomb_explosion"), 1, 1, 0, 0, 1f);
        }
        publisher.bombActive();
    }

}
