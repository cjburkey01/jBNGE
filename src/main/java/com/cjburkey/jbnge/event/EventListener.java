package com.cjburkey.jbnge.event;

@FunctionalInterface
public interface EventListener<T extends Event> {
    
    boolean onTrigger(T event);
    
}