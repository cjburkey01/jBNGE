package com.cjburkey.jbnge.physics;

import com.bulletphysics.collision.shapes.CollisionShape;

public interface IColliderListener {
    
    void onColliderChange(CollisionShape newShape);
    
}