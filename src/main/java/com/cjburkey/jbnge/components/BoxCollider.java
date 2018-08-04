package com.cjburkey.jbnge.components;

import org.joml.Vector3f;
import com.bulletphysics.collision.shapes.BoxShape;
import com.bulletphysics.collision.shapes.CollisionShape;
import com.cjburkey.jbnge.entity.EntityComponent;
import com.cjburkey.jbnge.physics.IColliderListener;

public class BoxCollider extends Collider {
    
    private final Vector3f size = new Vector3f(1.0f, 1.0f, 1.0f);
    private CollisionShape collisionShape;
    
    public void setSize(Vector3f size) {
        buildCollisionShape();
        this.size.set(size);
        for (EntityComponent component : parent.getComponents(EntityComponent.class)) {
            if (component != this && component instanceof IColliderListener) {
                ((IColliderListener) component).onColliderChange(collisionShape);
            }
        }
    }
    
    public Vector3f getSize() {
        return new Vector3f(size);
    }
    
    private CollisionShape buildCollisionShape() {
        return collisionShape = new BoxShape(new javax.vecmath.Vector3f(size.x / 2.0f, size.y / 2.0f, size.z / 2.0f));
    }
    
    public void setScale(Vector3f scale) {
        collisionShape.setLocalScaling(new javax.vecmath.Vector3f(scale.x, scale.y, scale.z));
    }
    
    public CollisionShape getShape() {
        return collisionShape;
    }
    
}