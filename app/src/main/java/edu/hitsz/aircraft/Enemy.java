package edu.hitsz.aircraft;

import java.util.List;

import edu.hitsz.prop.AbstractProp;

/**
 * 【工厂模式】Enemy接口，充当产品角色
 *
 * @author hhr
 */
public interface Enemy {
    /**
     * @param props 子弹集合
     */
    void dropProp(List<AbstractProp> props);
}
