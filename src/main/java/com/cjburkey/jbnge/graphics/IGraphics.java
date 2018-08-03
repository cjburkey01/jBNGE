package com.cjburkey.jbnge.graphics;

import org.joml.Vector3f;

public interface IGraphics {
    
    void createCapabilities();
    
    void updateViewport(int x, int y, int w, int h);
    
    void setClearColor(Vector3f color);
    
    void clear();
    
}