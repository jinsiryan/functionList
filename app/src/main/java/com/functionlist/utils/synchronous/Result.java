package com.functionlist.utils.synchronous;

/**
 * Created by yan on 2016/12/10.
 */

public class Result {
    private String guid;
    private long id;
    private Object obj;
    private boolean endFlag;

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }

    public boolean isEndFlag() {
        return endFlag;
    }

    public Result(String guid, long id, Object obj) {
        this.guid = guid;
        this.id = id;
        this.obj = obj;
    }
    public Result(String guid, long id, boolean endFlag,Object obj) {
        this.guid = guid;
        this.id = id;
        this.obj = obj;
    }
}
