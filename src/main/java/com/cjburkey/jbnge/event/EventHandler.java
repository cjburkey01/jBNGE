package com.cjburkey.jbnge.event;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public final class EventHandler<T extends Event> {
    
    private final Queue<EventListener<T>> listeners = new ConcurrentLinkedQueue<>();
    protected final Class<T> eventType;
    
    protected EventHandler(Class<T> eventType) {
        this.eventType = eventType;
    }
    
    protected void registerEventListener(EventListener<T> listener) {
        listeners.offer(listener);
    }
    
    protected void clearEventListeners() {
        while (!listeners.isEmpty()) {
            listeners.poll();
        }
    }
    
    protected void triggerEvent(T event) {
        for (EventListener<T> listener : listeners) {
            if (listener.onTrigger(event)) {
                return; // Event cancelled
            }
        }
    }
    
}