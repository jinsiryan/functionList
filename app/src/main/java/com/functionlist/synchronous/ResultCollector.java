package com.functionlist.synchronous;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Created by yan on 2016/12/10.
 */

public class ResultCollector {
    private static final int MAX_PACKETS = 65536;

    private ResultFilter resultFilter;
    private LinkedBlockingQueue<Result> resultQueue;
    private boolean cancelled = false;
    private ResultConnection conection;

    protected ResultCollector(ResultConnection conection, ResultFilter resultFilter) {
        this.conection = conection;
        this.resultFilter = resultFilter;
        this.resultQueue = new LinkedBlockingQueue<Result>(MAX_PACKETS);
    }
    public void cancel(){
        if(!cancelled){
            conection.removePacketCollector(this);
            cancelled = true;
        }
    }
    public ResultFilter getResultFilter(){
        return resultFilter;
    }
    public Result pollResult(){
        return resultQueue.poll();
    }
    public Result nextResult(){
        while (true){
            try {
                return resultQueue.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    public Result nextResult(long timeout){
        long endTime = System.currentTimeMillis() + timeout;
        do {
            try {
                return resultQueue.poll(timeout, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }while (System.currentTimeMillis() < endTime);
        return null;
    }
    protected synchronized  void processResult(Result result){
        if(result == null){
            return;
        }
        if(resultFilter == null || resultFilter.accept(result)){
            while (!resultQueue.offer(result)){
                resultQueue.poll();
            }
        }
    }

}
