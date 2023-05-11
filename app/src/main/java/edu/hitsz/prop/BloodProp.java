package edu.hitsz.prop;

import android.media.SoundPool;

import java.util.HashMap;

import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.application.Publisher;

/**
 * 回血道具
 *
 * @Author hhr
 */
public class BloodProp extends AbstractProp {

    private int hpIncrease = 100;

    public BloodProp(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);
    }

    @Override
    public void active(HeroAircraft heroAircraft, Publisher publisher, SoundPool mySoundPool, HashMap<String, Integer> soundPoolMap) {
        if (mySoundPool != null && soundPoolMap.get("get_supply") != null) {
            mySoundPool.play(soundPoolMap.get("get_supply"), 1, 1, 0, 0, 1f);
        }
        heroAircraft.increaseHp(hpIncrease);
    }

}
