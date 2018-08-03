package com.cjburkey.jbnge.entity;

public abstract class EntityHandler {
    
    private final LinkedEntityHandler entityHandler = new LinkedEntityHandler();
    
    public Entity createEntity() {
        Entity entity = new Entity();
        entityHandler.add(entity);
        return entity;
    }
    
    public void destroyEntity(Entity entity) {
        for (Entity ent : entityHandler.getIterator()) {
            if (ent.equals(entity)) {
                entity.onDestroy();
                entityHandler.remove(entity);
                return;
            }
        }
    }
    
    public void onEarlyUpdate(float deltaTime) {
        entityHandler.update();
        for (Entity entity : entityHandler.getIterator()) {
            entity.onEarlyUpdate(deltaTime);
        }
    }
    
    public void onUpdate(float deltaTime) {
        for (Entity entity : entityHandler.getIterator()) {
            entity.onUpdate(deltaTime);
        }
    }
    
    public void onLateUpdate(float deltaTime) {
        for (Entity entity : entityHandler.getIterator()) {
            entity.onLateUpdate(deltaTime);
        }
    }
    
    public void onEarlyRender(float deltaTime) {
        for (Entity entity : entityHandler.getIterator()) {
            entity.onEarlyRender(deltaTime);
        }
    }
    
    public void onRender(float deltaTime) {
        for (Entity entity : entityHandler.getIterator()) {
            entity.onRender(deltaTime);
        }
    }
    
    public void onLateRender(float deltaTime) {
        for (Entity entity : entityHandler.getIterator()) {
            entity.onLateRender(deltaTime);
        }
    }
    
}