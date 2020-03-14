package com.lostkingdom.demo.local;

import java.util.Random;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author yin.jiang
 * @date 2019/10/24 17:07
 */
public class multipleThreadLockTest {

    public static void main(String[] args) {
        final Data data = new Data();
        for (int i = 0; i < 3; i++) {
            new Thread(new Runnable() {
                public void run() {
                    for (int j = 0; j < 5; j++) {
                        data.set(new Random().nextInt(30));
                    }
                }
            }).start();
        }
        for (int i = 0; i < 3; i++) {
            new Thread(new Runnable() {
                public void run() {
                    for (int j = 0; j < 5; j++) {
                        data.get();
                    }
                }
            }).start();
        }
    }

    static class Data {
        private int data;// 共享数据
        private ReadWriteLock rwl = new ReentrantReadWriteLock();
        public void set(int data) {
            try{
                rwl.writeLock().lock();
                System.out.println(Thread.currentThread().getName() + "准备写入数据");
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                this.data = data;
                System.out.println(Thread.currentThread().getName() + "写入" + this.data);
            } finally{
                rwl.writeLock().unlock();
            }
        }
        public void get() {
            rwl.readLock().lock();
            try {
                System.out.println(Thread.currentThread().getName() + "准备读取数据");
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName() + "读取" + this.data);
            } finally {
                rwl.readLock().unlock();
            }
        }
    }


}
