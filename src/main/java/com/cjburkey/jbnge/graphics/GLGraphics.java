package com.cjburkey.jbnge.graphics;

import static org.lwjgl.opengl.GL11.*;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL;

public class GLGraphics implements IGraphics {
    
    public void createCapabilities() {
        GL.createCapabilities();
    }
    
    public void updateViewport(int x, int y, int w, int h) {
        glViewport(x, y, w, h);
    }
    
    public void setClearColor(Vector3f color) {
        glClearColor(color.x, color.y, color.z, 1.0f);
    }
    
    public void clear() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }
    
}