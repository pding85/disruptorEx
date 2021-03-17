package com.pding85.fanxing.component;

public class MessageWapper<M extends MessageStore > {

    M data ;

    int type;

    public M getData() {
        return data;
    }

    public void setData(M data) {
        this.data = data;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
