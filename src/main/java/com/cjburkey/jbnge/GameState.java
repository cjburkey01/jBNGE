package com.cjburkey.jbnge;

public enum GameState {
    
    PRE_INIT(true),
    INIT(true),
    RUNNING(true),
    STOPPING(false),
    STOPPED(false),
    
    ;
    
    public final boolean running;
    
    private GameState(boolean running) {
        this.running = running;
    }
    
}