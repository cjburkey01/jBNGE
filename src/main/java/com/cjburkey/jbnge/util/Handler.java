package com.cjburkey.jbnge.util;

import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public abstract class Handler<T> {
    
    protected final List<T> contained;
    protected final Queue<T> pendingAdd = new ConcurrentLinkedQueue<>();
    protected final Queue<T> pendingRem = new ConcurrentLinkedQueue<>();
    
    protected Handler(List<T> list) {
        contained = list;
    }
    
    public final void add(T element) {
        pendingAdd.offer(element);
    }
    
    public final void remove(T element) {
        pendingRem.offer(element);
    }
    
    public void update() {
        while (!pendingRem.isEmpty()) {
            contained.remove(pendingRem.poll());
        }
        while (!pendingAdd.isEmpty()) {
            contained.add(pendingAdd.poll());
        }
    }
    
    public Iterable<T> getIterator() {
        return new IteratorHandler();
    }
    
    private class IteratorHandler implements Iterable<T> {
        
        public Iterator<T> iterator() {
            return contained.iterator();
        }
        
    }
    
}