package com.cjburkey.jbnge;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public final class GameEngine {
    
    // Constants
    private static final double nanoSecondsPerSecond = 1000000000.0d;
    public static final GameEngine instance = new GameEngine();
    
    // State management
    private boolean hasInitialized = false;
    private boolean hasUpdateInitialized = false;
    private boolean hasRenderInitialized = false;
    private GameState gameState;
    
    // Game loops
    private Thread renderLoop;
    private Thread updateLoop;
    
    // Render queues
    private final Queue<RenderCall> localRenderCalls = new ConcurrentLinkedQueue<>();       // From the render thread
    private final Queue<RenderCall> externalRenderCalls = new ConcurrentLinkedQueue<>();    // From the update/other thread(s)
    
    // Timing handling
    private long lastUpdateTime = System.nanoTime();
    private double deltaUpdateTime = 0.0d;
    private long lastRenderTime = System.nanoTime();
    private double deltaRenderTime = 0.0d;
    private double publicDeltaUpdate = 0.0d;
    private double publicDeltaRender = 0.0d;
    
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
        if (hasInitialized) {
            return;
        }
        hasInitialized = true;
        updateGameState(GameState.PRE_INIT);
        
        Thread.currentThread().setName("Raw");
        Log.info("Initializing game engine");
        
        openWindow();
        RawGameEventCore.onEarlyInitialization();
        updateGameState(GameState.INIT);
        startGameLoops();
    }
    
    // Create and display the game window as well as initialize OpenGL
    private void openWindow() {
        window = new GameWindow();
        window.setVsync(true);
        window.show();
        window.setTitle("jBNGE...");
        window.setSizeRatioToMonitor(0.5f);
        window.centerOnScreen();
        
        Log.info("Created window");
    }
    
    private void startGameLoops() {
        startUpdateLoop();
        startRenderLoop();
    }
    
    private void startUpdateLoop() {
        createThread("Update", () -> {
            while (gameState.running) {
                lastUpdateTime = System.nanoTime();
                
                // Check if update initialization needs to be done
                if (!hasUpdateInitialized) {
                    hasUpdateInitialized = true;
                    RawGameEventCore.onUpdateInitialization();
                }
                
                if (hasUpdateInitialized && hasRenderInitialized && gameState.running && !gameState.equals(GameState.RUNNING)) {
                    updateGameState(GameState.RUNNING);
                }
                
                // Update the game and objects
                RawGameEventCore.onEarlyUpdate();
                RawGameEventCore.onUpdate();
                RawGameEventCore.onLateUpdate();
                
                // Increment update counter and game time
                gameUpdates ++;
                gameTime += getDeltaTime();
                
                // Keep track of time usage and control game throttling
                handleUpdateTiming();
            }
        }, true);
    }
    
    private void startRenderLoop() {
        Thread.currentThread().setName("Render");
        while (gameState.running) {
            lastRenderTime = System.nanoTime();
            
            // Check if update initialization needs to be done
            if (!hasRenderInitialized) {
                hasRenderInitialized = true;
                RawGameEventCore.onRenderInitialization();
            }
            
            // Check for new input
            window.pollInput();
            
            // Check if the player is trying to close the game
            if (window.getIsCloseRequested()) {
                updateGameState(GameState.STOPPING);
                break;
            }
            
            timeSinceTitleUpdate += publicDeltaRender;
            if (timeSinceTitleUpdate >= 1.0d / 60.0d) {
                timeSinceTitleUpdate = 0.0d;
                window.setTitle("jBNGE 0.0.1 | FPS: " + Format.format2(1.0d / publicDeltaRender) + " | UPS: " + Format.format2(1.0d / getDeltaTime()));
            }
            
            // Prepare the window for rendering
            window.preRender();
            
            // Render the objects
            RawGameEventCore.onEarlyRender();
            callQueuedRenders();
            RawGameEventCore.onRender();
            RawGameEventCore.onLateRender();
            
            // Render the frame to the window
            window.swapBuffers();
            
            // Increment frame counter
            gameFrames ++;
            
            // Make sure the game runs at the right speed and keep track
            // of timings for later usage and smoothing
            handleRenderTiming();
        }
        Thread.currentThread().setName("Raw");
        RawGameEventCore.onExit();
        callQueuedRenders();    // Call queued calls again so that anything cleaning up with queue calls is cleaned up
        window.destroy();
        updateGameState(GameState.STOPPED);
        exit(false, false, true);
    }
    
    // Makes sure the game doesn't run too fast
    private void handleUpdateTiming() {
        // Update timing between updates so movement can be smooth at different update rates
        updateUpdateTimes();
        
        // Prevent more than 100 updates per second
        while (deltaUpdateTime < 1.0d / 100.0d) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                Log.exception(e);
            }
            updateUpdateTimes();
        }
        
        publicDeltaUpdate = deltaUpdateTime;
    }
    
    private void updateUpdateTimes() {
        deltaUpdateTime = (System.nanoTime() - lastUpdateTime) / nanoSecondsPerSecond;
    }
    
    // Make sure the game doesn't run too fast
    private void handleRenderTiming() {
        // Render timing between renders so movement can be smooth at different FPS values
        updateRenderTimes();
        
        // Make sure that the game doesn't run at more than 500 fps
        while (deltaRenderTime < 1.0d / 500.0d) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                Log.exception(e);
            }
            updateRenderTimes();
        }
        
        publicDeltaRender = deltaRenderTime;
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
            updateGameState(GameState.STOPPING);
            return;
        }
        if (showMessage) {
            Log.error("A fatal error has prevented the game from continuing.");
        }
        Runtime.getRuntime().exit((cleanExit) ? 0 : 1);
    }
    
    // Called while the window is being resized so the window doesn't become black (essentially redraws even if the window is not "final")
    protected void onWindowRefreshRequired() {
        RawGameEventCore.onRender();
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
        return (float) publicDeltaUpdate;
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
    
    public GameWindow getWindow() {
        return window;
    }
    
    public void queueRender(RenderCall call) {
        if (getInRenderLoop()) {
            localRenderCalls.offer(call);
            return;
        }
        externalRenderCalls.offer(call);
    }
    
    private void callQueuedRenders() {
        cleanQueue(localRenderCalls);
        cleanQueue(externalRenderCalls);
    }
    
    private void cleanQueue(Queue<RenderCall> calls) {
        float delta = (float) publicDeltaRender;
        while (!calls.isEmpty()) {
            RenderCall call = calls.poll();
            if (call != null) {
                call.call(delta);
            }
        }
    }
    
    public GameState getGameState() {
        return gameState;
    }
    
    private void updateGameState(GameState gameState) {
        this.gameState = gameState;
        Log.info("Game state entered: {}", gameState.name());
    }
    
    // Creates a new thread
    public static Thread createThread(String name, Runnable runnable, boolean execute) {
        Thread t = new Thread(runnable);
        t.setName(name);
        if (execute) {
            t.start();
        }
        return t;
    }
    
}