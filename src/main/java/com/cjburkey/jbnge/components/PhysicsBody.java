package com.cjburkey.jbnge.components;

import com.bulletphysics.collision.shapes.CollisionShape;
import com.cjburkey.jbnge.entity.EntityComponent;
import com.cjburkey.jbnge.entity.RequireComponent;
import com.cjburkey.jbnge.physics.IColliderListener;

@RequireComponent(Collider.class)
public class PhysicsBody extends EntityComponent implements IColliderListener {
    
    // TODO: IMPLEMENT JBULLET INTEGRATION
    
    public PhysicsBody() {
        
    }
    
    public void onAdd() {
        
    }
    
    public void onColliderChange(CollisionShape newShape) {
        
    }
    
}