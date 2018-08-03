package com.cjburkey.jbnge.entity;

public class Scene extends EntityHandler {
    
    private static Scene currentScene;
    
    public Scene() {
        if (currentScene == null) {
            currentScene = this;
        }
    }
    
    public void makeCurrent() {
        currentScene = this;
    }
    
    public static Scene getCurrent() {
        return currentScene;
    }
    
}