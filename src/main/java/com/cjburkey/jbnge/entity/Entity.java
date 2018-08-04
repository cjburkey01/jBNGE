package com.cjburkey.jbnge.entity;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import com.cjburkey.jbnge.Log;
import com.cjburkey.jbnge.components.Transform;

public class Entity {
    
    private final UUID uuid;
    private final LinkedComponentHandler componentHandler = new LinkedComponentHandler();
    
    public final Transform transform;
    
    protected Entity() {
        transform = new Transform();
        addComponent(transform);
        
        uuid = UUID.randomUUID();
    }
    
    protected void onEarlyUpdate(float deltaTime) {
        componentHandler.update();
        
        for (EntityComponent component : componentHandler.getIterator()) {
            component.onEarlyUpdate(deltaTime);
        }
    }
    
    protected void onUpdate(float deltaTime) {
        for (EntityComponent component : componentHandler.getIterator()) {
            component.onUpdate(deltaTime);
        }
    }
    
    protected void onLateUpdate(float deltaTime) {
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
        if ((!component.getAllowMultiple() && getComponent(component.getClass()) != null) ||
                component.parent != null || !updateComponentParent(component) ||
                !validateRequirements(component)) {
            return;
        }
        componentHandler.add(component);
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
            if (type.isAssignableFrom(component.getClass()) && (type.cast(component).getRemovable() ||
                    force)) {
                removeComponent(component);
                if (stop) {
                    return;
                }
            }
        }
    }
    
    private <T extends EntityComponent> boolean updateComponentParent(T component) {
        try {
            Field parentField = component.getClass().getField("parent");
            parentField.setAccessible(true);
            parentField.set(component, this);
            return true;
        } catch (Exception e) {
            Log.error("Failed to set parent for component {}", component.getClass().getName());
            Log.exception(e);
        }
        return false;
    }
    
    private <T extends EntityComponent> boolean validateRequirements(T component) {
        Class<? extends EntityComponent>[] types = getRequired(component.getClass());
        for (Class<? extends EntityComponent> type : types) {
            if (getComponent(type) == null && !componentHandler.containsPending(type)) {
                Log.error("Component {} requires component type of {}", component.getClass().getName(),
                        type.getName());
                return false;
            }
        }
        return true;
    }
    
    @SuppressWarnings("unchecked")
    private <T extends EntityComponent> Class<? extends EntityComponent>[] getRequired(Class<T> component) {
        Annotation[] annotations = component.getAnnotations();
        List<Class<? extends EntityComponent>> required = new ArrayList<>();
        for (Annotation annotation : annotations) {
            if (annotation.annotationType().equals(RequireComponent.class)) {
                required.add(RequireComponent.class.cast(annotation).value());
            }
        }
        return required.toArray(new Class[required.size()]);
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