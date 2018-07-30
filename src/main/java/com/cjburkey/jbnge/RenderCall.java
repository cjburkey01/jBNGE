package com.cjburkey.jbnge;

@FunctionalInterface
public interface RenderCall {
    
    void call(float deltaTime);
    
}