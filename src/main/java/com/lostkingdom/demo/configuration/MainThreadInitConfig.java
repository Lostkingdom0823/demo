package com.lostkingdom.demo.configuration;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.concurrent.locks.AbstractQueuedSynchronizer;

/**
 * @ClassName MainThreadInitConfig
 * @Author yin.jiang
 * @Date 2020/1/17 18:07
 * @Version 1.0
 */
@Component
public class MainThreadInitConfig implements ApplicationRunner{
    @Override
    public void run(ApplicationArguments args) throws Exception {
        initThread();
    }


    private void initThread(){
        Thread countThread = new Thread(new Runnable(){
            @Override
            public void run() {
                threadAct();
            }
        });

        countThread.start();
    }

    private void threadAct(){
        Integer i = 0;
        while(true){
            System.out.println(i);
            i++;
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private final Sync sync = new Sync();

    private static class Sync extends AbstractQueuedSynchronizer {

        @Override
        public boolean tryAcquire(int acquires){
            if(compareAndSetState(0,1)){
                setExclusiveOwnerThread(Thread.currentThread());
                return true;
            }
            return false;
        }

        @Override
        public boolean tryRelease(int acquires){
            if(getState() == 0){
                throw new IllegalMonitorStateException();
            }
            setExclusiveOwnerThread(null);
            setState(0);
            return false;
        }
    }
}
