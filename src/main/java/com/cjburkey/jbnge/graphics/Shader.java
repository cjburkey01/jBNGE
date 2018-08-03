package com.cjburkey.jbnge.graphics;

import static org.lwjgl.opengl.GL20.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import com.cjburkey.jbnge.GameEngine;
import com.cjburkey.jbnge.Log;

public class Shader {
    
    private Map<Integer, Integer> shaders = new ConcurrentHashMap<>();
    private int program = -1;
    
    public void addShader(int type, String source) {
        GameEngine.instance.queueRender(true, (deltaTime) -> {
            if (program < 0) {
                program = glCreateProgram();
            }
            if (shaders.containsKey(type)) {
                Log.error("Shader already contains a shader of type: {}", type);
                return;
            }
            int shader = glCreateShader(type);
            glShaderSource(shader, source);
            glCompileShader(shader);
            String info = glGetShaderInfoLog(shader);
            if (info != null && !(info = info.trim()).isEmpty()) {
                Log.error("Shader error: {}", info);
                return;
            }
            glAttachShader(program, shader);
            shaders.put(type, shader);
        });
    }
    
    public void link() {
        GameEngine.instance.queueRender(true, (deltaTime) -> {
            if (program < 0) {
                Log.error("Shader not yet initialized");
                return;
            }
            glLinkProgram(program);
            String info = glGetProgramInfoLog(program);
            if (info != null && !(info = info.trim()).isEmpty()) {
                Log.error("Link error: {}", info);
                return;
            }
            for (Integer shader : shaders.values()) {
                glDetachShader(program, shader);
                glDeleteShader(shader);
            }
        });
    }
    
    public void bind() {
        GameEngine.instance.queueRender(true, (deltaTime) -> {
            if (program < 0) {
                Log.error("Shader not yet initialized");
                return;
            }
            glUseProgram(program);
        });
    }
    
    public static void unbind() {
        GameEngine.instance.queueRender(true, (deltaTime) -> glUseProgram(0));
    }
    
}