package com.cjburkey.jbnge.util;

import java.util.LinkedList;

public class LinkedHandler<T> extends Handler<T> {
    
    public LinkedHandler() {
        super(new LinkedList<>());
    }
    
}