package com.cjburkey.jbnge.event;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public final class EventSystem {
    
    private final Queue<EventHandler<? extends Event>> eventHandlers = new ConcurrentLinkedQueue<>();
    
    public <T extends Event> void registerEventListener(Class<T> eventType, EventListener<T> listener) {
        getEventHandler(eventType).registerEventListener(listener);
    }
    
    @SuppressWarnings("unchecked")
    private <T extends Event> EventHandler<T> getEventHandler(Class<T> eventType) {
        for (EventHandler<?> eventHandler : eventHandlers) {
            if (eventHandler.eventType.equals(eventType)) {
                return (EventHandler<T>) eventHandler;
            }
        }
        EventHandler<T> eventHandler = new EventHandler<>(eventType);
        eventHandlers.offer(eventHandler);
        return eventHandler;
    }
    
}