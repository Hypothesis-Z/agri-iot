package com.zzq.app.rx;

public class RxBusBaseMessage {

    private  int code;
    private Object object;
    private RxBusBaseMessage(){}
    public RxBusBaseMessage(int code, Object object){
        this.code=code;
        this.object=object;
    }

    public int getCode() {
        return code;
    }

    public Object getObject() {
        return object;
    }
}
