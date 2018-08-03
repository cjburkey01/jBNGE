package com.cjburkey.jbnge.graphics;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.*;
import org.joml.Vector2i;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFWVidMode;
import com.cjburkey.jbnge.GameEngine;
import com.cjburkey.jbnge.input.Input;

public final class GLFWWindow implements IWindow {
    
    private final Vector2i windowSize = new Vector2i(300, 300);
    private StringBuffer windowTitle = new StringBuffer();
    private long window;
    private Vector3f clearColor = new Vector3f(0.0f, 0.0f, 0.0f);
    private int vsync = 0;
    
    public void init(Runnable onWindowRefreshRequired) {
        // Initialize GLFW (window library)
        if (!glfwInit()) {
            throw new RuntimeException();
        }
        
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        
        // Create and verify window
        window = glfwCreateWindow(windowSize.x, windowSize.y, windowTitle, NULL, NULL);
        if (window == NULL) {
            throw new RuntimeException("Failed to create GLFW window");
        }
        
        // Register event listeners for input
        glfwSetFramebufferSizeCallback(window, (win, w, h) -> onResize(w, h));
        glfwSetWindowRefreshCallback(window, (win) -> onWindowRefreshRequired.run());
        glfwSetKeyCallback(window, (win, key, code, action, mods) -> {
            if (action == GLFW_PRESS) {
                Input.onKeyPress(key);
            } else if (action == GLFW_RELEASE) {
                Input.onKeyRelease(key);
            }
        });
        glfwSetMouseButtonCallback(window, (win, button, action, mods) -> {
            if (action == GLFW_PRESS) {
                Input.onMousePress(button);
            } else if (action == GLFW_RELEASE) {
                Input.onMousePress(button);
            }
        });
        glfwSetCursorPosCallback(window, (win, x, y) -> Input.onCursorMove((float) x, (float) y));
        
        // Make this the OpenGL-active window
        glfwMakeContextCurrent(window);
        
        // Initialize OpenGL
        GameEngine.getGraphics().createCapabilities();
        
        // Initialize background color to black
        setBackgroundColor(new Vector3f(0.0f, 0.0f, 0.0f));
        
        // Enable V-Sync by default (finish one frame before drawing another; run in sync with the GPU)
        setVsync(true);
    }
    
    // Called when the window size is changed
    private void onResize(int width, int height) {
        windowSize.set(width, height);
        GameEngine.getGraphics().updateViewport(0, 0, width, height);
    }
    
    // Clears the current window to the specified background color
    public void preRender() {
        GameEngine.getGraphics().clear();
    }
    
    // Draws the next frame
    public void swapBuffers() {
       glfwSwapBuffers(window); 
    }
    
    // Checks for any new events that have occurred
    public void pollInput() {
       glfwPollEvents(); 
    }
    
    // Checks if the user is trying to close the window
    public boolean getIsCloseRequested() {
        return glfwWindowShouldClose(window);
    }
    
    // Sets the framebuffer width and height
    public void setSize(int width, int height) {
        glfwSetWindowSize(window, width, height);
    }
    
    // Retrieves the framebuffer width
    public int getWidth() {
        return windowSize.x;
    }
    
    // Retrives the frammer height
    public int getHeight() {
        return windowSize.y;
    }
    
    // Sets the window title
    public void setTitle(String title) {
        windowTitle.setLength(0);
        windowTitle.append(title);
        glfwSetWindowTitle(window, title);
    }
    
    // Retrieves the window title
    public String getTitle() {
        return windowTitle.toString();
    }
    
    // Makes the window visible
    public void show() {
        glfwShowWindow(window);
    }
    
    // Makes the window invisible
    public void hide() {
        glfwHideWindow(window);
    }
    
    // Cleans up the window and GLFW
    public void destroy() {
        hide();
        glfwDestroyWindow(window);
        glfwTerminate();
    }
    
    // Sets the background (clear) color
    public void setBackgroundColor(Vector3f color) {
        clearColor.set(color);
        GameEngine.getGraphics().setClearColor(color);
    }
    
    // Retrieves the background (clear) color
    public Vector3f getBackgroundColor() {
        return new Vector3f(clearColor);
    }
    
    // Enables or disables vertical synchronization
    public void setVsync(boolean vsync) {
        this.vsync = (vsync) ? 1 : 0;
        glfwSwapInterval(this.vsync);
    }
    
    // Checks whether or not vertical synchronization is enabled
    public boolean getVsync() {
        return this.vsync > 0;
    }
    
    // Sets the window's position on the current monitor
    public void setPosition(int x, int y) {
        glfwSetWindowPos(window, x, y);
    }
    
    // Retrives the primary monitor for the system
    public long getPrimaryMonitor() {
        return glfwGetPrimaryMonitor();
    }
    
    // Gets the size of the provided monitor
    public Vector2i getMonitorSize(long monitor) {
        GLFWVidMode glfwVidMode = glfwGetVideoMode(monitor);
        return new Vector2i(glfwVidMode.width(), glfwVidMode.height());
    }
    
    // Gets the size of the primary monitor for the system
    public Vector2i getPrimaryMonitorSize() {
        return getMonitorSize(getPrimaryMonitor());
    }
    
    // Centers the window on the current monitor
    public void centerOnScreen() {
        Vector2i monitorSize = getPrimaryMonitorSize();
        setPosition((monitorSize.x - getWidth()) / 2, (monitorSize.y - getHeight()) / 2);
    }
    
    // Sets the size of the window to the specified ratio compared to the primary monitor size
    public void setSizeRatioToMonitor(float ratio) {
        Vector2i monitorSize = getPrimaryMonitorSize();
        setSize((int) (monitorSize.x * ratio), (int) (monitorSize.y * ratio));
    }
    
}