package com.cjburkey.jbnge.components;

import org.joml.Vector3f;
import com.bulletphysics.collision.shapes.CollisionShape;
import com.cjburkey.jbnge.entity.EntityComponent;

public abstract class Collider extends EntityComponent {
    
    private Vector3f previousScale;
    
    public abstract CollisionShape getShape();
    
    protected abstract void setScale(Vector3f scale);
    
    protected void onEarlyUpdate() {
        if (!parent.transform.scale.equals(previousScale)) {
            previousScale.set(parent.transform.scale);
            setScale(parent.transform.scale);
        }
    }
    
}