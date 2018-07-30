package com.cjburkey.jbnge.event;

public abstract class Event {
    
    protected final boolean cancellable;
    
    public Event(boolean cancellable) {
        this.cancellable = cancellable;
    }
    
    public Event() {
        this(false);
    }
    
    public final String getName() {
        return getClass().getName();
    }
    
    public abstract boolean onCall();
    
}