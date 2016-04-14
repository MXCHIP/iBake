package com.lidroid.xutils.task;

/**
 * Author: wyouflf
 * Date: 14-5-16
 * Time: am11:25
 */
public class PriorityRunnable extends PriorityObject<Runnable> implements Runnable {

    public PriorityRunnable(Priority priority, Runnable obj) {
        super(priority, obj);
    }

    @Override
    public void run() {
        this.obj.run();
    }
}
