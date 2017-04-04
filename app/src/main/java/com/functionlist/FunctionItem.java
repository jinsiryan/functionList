package com.functionlist;

import android.content.Context;

/**
 * Created by imo on 2016/9/25.
 */
public class FunctionItem {
    private String id;
    private String desc;

    public FunctionItem(String id, String desc) {
        this.id = id;
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
