package com.cjburkey.jbnge.entity;

import java.util.LinkedList;
import java.util.List;
import com.cjburkey.jbnge.util.LinkedHandler;

public class LinkedComponentHandler extends LinkedHandler<EntityComponent> {
    
    public void update() {
        EntityComponent tmp;
        List<EntityComponent> removed = new LinkedList<>();
        List<EntityComponent> added = new LinkedList<>();
        while (!pendingRem.isEmpty()) {
            tmp = pendingRem.poll();
            removed.add(tmp);
            contained.remove(tmp);
        }
        for (EntityComponent component : removed) {
            component.onRemove();
        }
        removed.clear();
        while (!pendingAdd.isEmpty()) {
            tmp = pendingAdd.poll();
            added.add(tmp);
            contained.add(tmp);
        }
        for (EntityComponent component : added) {
            component.onAdd();
        }
        added.clear();
    }
    
    public <T extends EntityComponent> boolean containsPending(Class<T> component) {
        for (EntityComponent comp : pendingAdd) {
            if (component.isAssignableFrom(comp.getClass())) {
                return true;
            }
        }
        return false;
    }
    
}