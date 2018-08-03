package com.cjburkey.jbnge.graphics;

import org.joml.Vector2i;
import org.joml.Vector3f;

public interface IWindow {
    
    void init(Runnable onWindowRefreshRequired);
    
    // Clears the current window to the specified background color
    void preRender();
    
    // Draws the next frame
    void swapBuffers();
    
    // Checks for any new events that have occurred
    void pollInput();
    
    // Checks if the user is trying to close the window
    boolean getIsCloseRequested();
    
    // Sets the framebuffer width and height
    void setSize(int width, int height);
    
    // Retrieves the framebuffer width
    int getWidth();
    
    // Retrives the framebuffer height
    int getHeight();
    
    // Sets the window title
    void setTitle(String title);
    
    // Retrieves the window title
    String getTitle();
    
    // Makes the window visible
    void show();
    
    // Makes the window invisible
    void hide();
    
    // Cleans up the window and GLFW
    void destroy();
    
    // Sets the background (clear) color
    void setBackgroundColor(Vector3f color);
    
    // Retrieves the background (clear) color
    Vector3f getBackgroundColor();
    
    // Enables or disables vertical synchronization
    void setVsync(boolean vsync);
    
    // Checks whether or not vertical synchronization is enabled
    boolean getVsync();
    
    // Sets the window's position on the current monitor
    void setPosition(int x, int y);
    
    // Retrives the primary monitor for the system
    long getPrimaryMonitor();
    
    // Gets the size of the provided monitor
    Vector2i getMonitorSize(long monitor);
    
    // Gets the size of the primary monitor for the system
    Vector2i getPrimaryMonitorSize();
    
    // Centers the window on the current monitor
    void centerOnScreen();
    
    // Sets the size of the window to the specified ratio compared to the primary monitor size
    void setSizeRatioToMonitor(float ratio);
    
}