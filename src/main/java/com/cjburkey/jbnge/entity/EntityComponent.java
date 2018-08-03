package com.cjburkey.jbnge.entity;

public abstract class EntityComponent {
    
    protected final Entity parent = null;
    
    private boolean allowMultiple;
    private boolean removable;
    
    protected final void setAllowMultiple(boolean allowMultiple) {
        this.allowMultiple = allowMultiple;
    }
    
    protected final void setRemovable(boolean removable) {
        this.removable = removable;
    }
    
    public final boolean getAllowMultiple() {
        return allowMultiple;
    }
    
    public final boolean getRemovable() {
        return removable;
    }
    
    protected void onAdd() {
    }
    
    protected void onRemove() {
    }
    
    protected void onEarlyUpdate() {
    }
    
    protected void onUpdate() {
    }
    
    protected void onLateUpdate() {
    }
    
    protected void onEarlyUpdate(float deltaTime) {
        onEarlyUpdate();
    }
    
    protected void onUpdate(float deltaTime) {
        onUpdate();
    }
    
    protected void onLateUpdate(float deltaTime) {
        onLateUpdate();
    }
    
    protected void onEarlyRender(float deltaTime) {
    }
    
    protected void onRender(float deltaTime) {
    }
    
    protected void onLateRender(float deltaTime) {
    }
    
}