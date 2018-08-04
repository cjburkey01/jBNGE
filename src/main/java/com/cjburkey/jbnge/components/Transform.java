package com.cjburkey.jbnge.components;

import org.joml.Quaternionf;
import org.joml.Vector3f;
import com.cjburkey.jbnge.entity.EntityComponent;

public class Transform extends EntityComponent {
    
    public final Vector3f position = new Vector3f();
    public final Quaternionf rotation = new Quaternionf();
    public final Vector3f scale = new Vector3f();
    private final Vector3f previousPos = new Vector3f();
    private final Quaternionf previousRot = new Quaternionf();
    private final Vector3f previousScl = new Vector3f();
    
    public Transform() {
        setAllowMultiple(false);
        setRemovable(false);
    }
    
    protected void onEarlyUpdate() {
        if (!position.equals(previousPos)) {
            previousPos.set(position);
        }
        if (!rotation.equals(previousRot)) {
            previousRot.set(rotation);
        }
        if (!scale.equals(previousScl)) {
            previousScl.set(scale);
        }
    }
    
}