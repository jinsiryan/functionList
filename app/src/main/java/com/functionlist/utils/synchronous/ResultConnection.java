package com.functionlist.utils.synchronous;

import android.text.TextUtils;

import java.util.Collection;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by yan on 2016/12/10.
 */

public class ResultConnection {

    protected final Collection<ResultCollector> collectors = new ConcurrentLinkedQueue<ResultCollector>();


    public ResultCollector createResultCollector(ResultFilter resultFilter) {
        ResultCollector collector = new ResultCollector(this, resultFilter);
        collectors.add(collector);
        return collector;
    }
    protected void removePacketCollector(ResultCollector collector) {
        collectors.remove(collector);
    }

    protected Collection<ResultCollector> getPacketCollectors() {
        return collectors;
    }

    public ResultCollector getResultCollector(String guid){
        if(TextUtils.isEmpty(guid)){
            return null;
        }
        for (ResultCollector collector: collectors) {
           if(guid.equals(collector.getResultFilter().getGuid())){
               return collector;
           }
        }
        return null;
    }

    public void processResult(Result result) {
        if (result == null) {
            return;
        }
        for (ResultCollector collector: collectors) {
            collector.processResult(result);
        }
    }

}
