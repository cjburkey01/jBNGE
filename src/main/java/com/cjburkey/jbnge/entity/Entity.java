package com.cjburkey.jbnge.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Entity {
    
    private final UUID uuid;
    private final LinkedComponentHandler componentHandler = new LinkedComponentHandler();
    
    protected Entity() {
        uuid = UUID.randomUUID();
    }
    
    protected void onEarlyUpdate(float deltaTime) {
        componentHandler.update();
        for (EntityComponent component : componentHandler.getIterator()) {
            component.onEarlyUpdate(deltaTime);
        }
    }
    
    protected void onUpdate(float deltaTime) {
        componentHandler.update();
        for (EntityComponent component : componentHandler.getIterator()) {
            component.onUpdate(deltaTime);
        }
    }
    
    protected void onLateUpdate(float deltaTime) {
        componentHandler.update();
        for (EntityComponent component : componentHandler.getIterator()) {
            component.onLateUpdate(deltaTime);
        }
    }
    
    protected void onEarlyRender(float deltaTime) {
        for (EntityComponent component : componentHandler.getIterator()) {
            component.onEarlyRender(deltaTime);
        }
    }
    
    protected void onRender(float deltaTime) {
        for (EntityComponent component : componentHandler.getIterator()) {
            component.onRender(deltaTime);
        }
    }
    
    protected void onLateRender(float deltaTime) {
        for (EntityComponent component : componentHandler.getIterator()) {
            component.onLateRender(deltaTime);
        }
    }
    
    protected void onDestroy() {
        removeComponents(EntityComponent.class, true);
    }
    
    public <T extends EntityComponent> void addComponent(T component) {
        if (component.getAllowMultiple() || getComponent(component.getClass()) == null) {
            componentHandler.add(component);
        }
    }
    
    public <T extends EntityComponent> List<T> getComponents(Class<T> type) {
        List<T> comps = new ArrayList<>();
        for (EntityComponent component : componentHandler.getIterator()) {
            if (type.isAssignableFrom(component.getClass())) {
                comps.add(type.cast(component));
            }
        }
        return comps;
    }
    
    public <T extends EntityComponent> T getComponent(Class<T> type) {
        for (EntityComponent component : componentHandler.getIterator()) {
            if (type.isAssignableFrom(component.getClass())) {
                return type.cast(component);
            }
        }
        return null;
    }
    
    public <T extends EntityComponent> void removeComponent(T component) {
        componentHandler.remove(component);
    }
    
    public <T extends EntityComponent> void removeComponents(Class<T> type) {
        removeComponents(type, false, false);
    }
    
    public <T extends EntityComponent> void removeComponent(Class<T> type) {
        removeComponent(type, false);
    }
    
    private <T extends EntityComponent> void removeComponent(Class<T> type, boolean force) {
        removeComponents(type, force, true);
    }
    
    private <T extends EntityComponent> void removeComponents(Class<T> type, boolean force) {
        removeComponents(type, force, false);
    }
    
    private <T extends EntityComponent> void removeComponents(Class<T> type, boolean force, boolean stop) {
        for (EntityComponent component : componentHandler.getIterator()) {
            if (type.isAssignableFrom(component.getClass()) && (type.cast(component).getRemovable() || force)) {
                removeComponent(component);
                if (stop) {
                    return;
                }
            }
        }
    }
    
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((uuid == null) ? 0 : uuid.hashCode());
        return result;
    }
    
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!getClass().equals(obj.getClass())) {
            return false;
        }
        Entity other = (Entity) obj;
        if (uuid == null) {
            if (other.uuid != null) {
                return false;
            }
        } else if (!uuid.equals(other.uuid)) {
            return false;
        }
        return true;
    }
    
}