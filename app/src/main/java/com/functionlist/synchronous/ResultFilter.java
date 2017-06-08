package com.functionlist.synchronous;

/**
 * Created by yan on 2016/12/10.
 */

public interface  ResultFilter {
    String getGuid();
    boolean accept(Result result);
}
