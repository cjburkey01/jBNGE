package com.cjburkey.jbnge.entity;

import com.cjburkey.jbnge.util.LinkedHandler;

public class LinkedEntityHandler extends LinkedHandler<Entity> {
    
    public void update() {
        Entity tmp;
        while (!pendingRem.isEmpty()) {
            tmp = pendingRem.poll();
            tmp.onDestroy();
            contained.remove(tmp);
        }
        while (!pendingAdd.isEmpty()) {
            contained.add(pendingAdd.poll());
        }
    }
    
}