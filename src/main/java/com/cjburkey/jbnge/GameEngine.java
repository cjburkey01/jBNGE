package com.cjburkey.jbnge;

public final class GameEngine {
    
    // Constants
    private static final double nanoSecondsPerSecond = 1_000_000_000.0d;
    public static final GameEngine instance = new GameEngine();
    
    // State management
    private boolean hasInitialized = false;
    private boolean hasPostLoopInitialized = false;
    private boolean running = false;
    
    // Game loops
    private Thread renderLoop;
    private Thread updateLoop;
    
    // Timing handling
    private long lastUpdateTime = System.nanoTime();
    private double deltaUpdateTime = 0.0d;
    private long lastRenderTime = System.nanoTime();
    private double deltaRenderTime = 0.0d;
    
    // Game time handling
    private double gameTime = 0.0d;
    private long gameUpdates = 0;
    private long gameFrames = 0;
    private double timeSinceTitleUpdate = 0.0d;
    
    // Game features    
    private GameWindow window;
    
    private GameEngine() {
        Thread.setDefaultUncaughtExceptionHandler((thread, e) -> Log.exception(e));
        if (instance != null) {
            throw new RuntimeException("Cannot reinstantiate GameEngine");
        }
    }
    
    // Initialize and begin the game
    public void initialize() {
        if (hasInitialized || running) {
            return;
        }
        hasInitialized = true;
        running = true;
        
        Log.info("Initializing game engine");
        
        openWindow();
        startGameLoops();
    }
    
    // Create and display the game window as well as initialize OpenGL
    private void openWindow() {
        window = new GameWindow();
        window.show();
        window.setTitle("jBNGE 0.0.1");
        window.setSizeRatioToMonitor(0.5f);
        window.centerOnScreen();
        
        Log.info("Created window");
    }
    
    private void startGameLoops() {
        startUpdateLoop();
        startRenderLoop();
    }
    
    private void startUpdateLoop() {
        createThread(() -> {
            while (running) {
                lastUpdateTime = System.nanoTime();
                
                // Update the game and objects
                onEarlyUpdate();
                onUpdate();
                onLateUpdate();
                
                // Increment update counter and game time
                gameUpdates ++;
                gameTime += deltaUpdateTime;
                
                // Keep track of time usage and control game throttling
                handleUpdateTiming();
            }
        }, true);
    }
    
    private void startRenderLoop() {
        // Pre-initialize when necessary
        onEarlyInitialization();
        
        while (running) {
            lastRenderTime = System.nanoTime();
            
            // Check if normal initialization needs to be done
            if (!hasPostLoopInitialized) {
                hasPostLoopInitialized = true;
                onInitialization();
            }
            
            // Check for new input
            window.pollInput();
            
            // Check if the player is trying to close the game
            if (window.getIsCloseRequested()) {
                running = false;
                break;
            }
            
            // Prepare the window for rendering
            window.preRender();
            
            // Render the objects
            onEarlyRender();
            onRender();
            onLateRender();
            
            // Render the frame to the window
            window.swapBuffers();
            
            // Increment frame counter
            gameFrames ++;
            
            // Make sure the game runs at the right speed and keep track
            // of timings for later usage and smoothing
            handleRenderTiming();
        }
        onExit();
        exit(false, false, true);
    }
    
    // Makes sure the game doesn't run too fast
    private void handleUpdateTiming() {
        // Update timing between updates so movement can be smooth at different update rates
        updateUpdateTimes();
        
        while (deltaUpdateTime < 1.0d / 1000.0d) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                Log.exception(e);
            }
            updateUpdateTimes();
        }
    }
    
    private void updateUpdateTimes() {
        deltaUpdateTime = (System.nanoTime() - lastUpdateTime) / nanoSecondsPerSecond;
    }
    
    // Makes sure the game doesn't run too fast
    private void handleRenderTiming() {
        // Render timing between renders so movement can be smooth at different FPS values
        updateRenderTimes();
        
        // Throttle rendering to about 1000 frames per second (because 500 equals 1000? Idk what's going on, but this seems to somewhat work)
        while (deltaRenderTime < 1.0d / 1000.0d) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                Log.exception(e);
            }
            updateRenderTimes();
        }
    }
    
    private void updateRenderTimes() {
        deltaRenderTime = (System.nanoTime() - lastRenderTime) / nanoSecondsPerSecond;
    }
    
    // Begins the shutdown process for the game to exit "kindly"
    public void exit(boolean peacefully) {
        exit(true, false, false);
    }
    
    // Exits the game
    protected void exit(boolean peacefully, boolean showMessage, boolean cleanExit) {
        if (peacefully) {
            running = false;
            return;
        }
        if (showMessage) {
            Log.error("A fatal error has prevented the game from continuing.");
        }
        Runtime.getRuntime().exit((cleanExit) ? 0 : 1);
    }
    
    // Called before the game loop is entered
    private void onEarlyInitialization() {
        Log.info("Pre-initialized");
    }
    
    // Called just after the game loop has been entered
    private void onInitialization() {
        Log.info("Started game loop");
    }
    
    // Called before the main update loop
    private void onEarlyUpdate() {
        
    }
    
    // Called every frame before the game renders objects
    private void onUpdate() {
        
    }
    
    // Called after the main update but before rendering
    private void onLateUpdate() {
        
    }
    
    // Called just after updating the game
    private void onEarlyRender() {
        
    }
    
    // Called while the window is being resized so the window doesn't become black (essentially redraws even if the window is not "final")
    protected void onWindowRefreshRequired() {
        onRender();
    }
    
    // Called every frame after objects have been updated
    // This is where OpenGL rendering is called
    private void onRender() {
        timeSinceTitleUpdate += deltaRenderTime;
        if (timeSinceTitleUpdate >= 1.0d / 30.0d) {
            timeSinceTitleUpdate = 0.0d;
            window.setTitle("jBNGE 0.0.1 | FPS: " + Format.format2(1.0d / deltaRenderTime) + " | UPS: " + Format.format2(1.0d / deltaUpdateTime));
        }
    }
    
    // Called after the game renders but before the next frame
    private void onLateRender() {
        
    }
    
    // Called before the game completely exits; used to clean up objects in memory
    private void onExit() {
        window.destroy();
    }
    
    // Check whether the current thread is the thread containing the GLFW context and OpenGL rendering engine
    public boolean getInRenderLoop() {
        return Thread.currentThread().equals(renderLoop);
    }
    
    // Check whether the current thread contains the update loop
    public boolean getInUpdateLoop() {
        return Thread.currentThread().equals(updateLoop);
    }
    
    public float getDeltaTime() {
        return (float) deltaUpdateTime;
    }
    
    public float getGameTime() {
        return (float) gameTime;
    }
    
    public long getGameUpdates() {
        return gameUpdates;
    }
    
    public long getGameFrames() {
        return gameFrames;
    }
    
    // Creates a new thread
    public static Thread createThread(Runnable runnable, boolean execute) {
        Thread t = new Thread(runnable);
        if (execute) {
            t.start();
        }
        return t;
    }
    
}