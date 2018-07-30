package com.cjburkey.jbnge;

public final class RawGameEventCore {
    
    // Called before the game loops are entered
    protected static void onEarlyInitialization() {
        Log.info("Pre-initialized");
    }
    
    // Called just after the update game loop has been entered
    protected static void onUpdateInitialization() {
        Log.info("Started update loop");
    }
    
    // Called just after the render loop has been entered
    protected static void onRenderInitialization() {
        Log.info("Started render loop");
    }
    
    // Called before the main update loop
    protected static void onEarlyUpdate() {
        
    }
    
    // Called every frame before the game renders objects
    protected static void onUpdate() {
        
    }
    
    // Called after the main update but before rendering
    protected static void onLateUpdate() {
        
    }
    
    // Called just after updating the game
    protected static void onEarlyRender() {
        
    }
    
    // Called every frame after objects have been updated
    // This is where OpenGL rendering is called
    protected static void onRender() {
        
    }
    
    // Called after the game renders but before the next frame
    protected static void onLateRender() {
        
    }
    
    // Called before the game completely exits; used to clean up objects in memory
    protected static void onExit() {
        Log.info("Exiting game loops");
    }
    
}