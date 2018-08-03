package com.cjburkey.jbnge.entity;

import com.cjburkey.jbnge.util.LinkedHandler;

public class LinkedComponentHandler extends LinkedHandler<EntityComponent> {
    
    public void update() {
        EntityComponent tmp;
        while (!pendingRem.isEmpty()) {
            tmp = pendingRem.poll();
            tmp.onRemove();
            contained.remove(tmp);
        }
        while (!pendingAdd.isEmpty()) {
            tmp = pendingAdd.poll();
            tmp.onAdd();
            contained.add(tmp);
        }
    }
    
}