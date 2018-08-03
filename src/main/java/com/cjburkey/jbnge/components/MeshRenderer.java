package com.cjburkey.jbnge.components;

import static org.lwjgl.opengl.GL20.*;
import org.joml.Vector3f;
import com.cjburkey.jbnge.GameEngine;
import com.cjburkey.jbnge.Log;
import com.cjburkey.jbnge.entity.EntityComponent;
import com.cjburkey.jbnge.graphics.Mesh;
import com.cjburkey.jbnge.graphics.Shader;
import com.cjburkey.jbnge.input.Input;
import com.cjburkey.jbnge.input.Key;
import com.cjburkey.jbnge.util.Resource;

public class MeshRenderer extends EntityComponent {
    
    private Vector3f[] verts = new Vector3f[] {
        new Vector3f(0.0f, 0.5f, 0.0f),
        new Vector3f(-0.5f, -0.5f, 0.0f),
        new Vector3f(0.5f, -0.5f, 0.0f),
    };
    
    private short[] inds = new short[] {
        0, 1, 2,
    };
    
    Shader shader;
    Mesh mesh;
    
    protected void onAdd() {
        GameEngine.instance.queueRender(true, (rc) -> onRenderInit());
    }
    
    private void onRenderInit() {
        Resource vertexShader = new Resource("", "shaders/basic.vsh");
        Resource fragmentShader = new Resource("", "shaders/basic.fsh");
        
        shader = new Shader();
        shader.addShader(GL_VERTEX_SHADER, vertexShader.readFullTextFile());
        shader.addShader(GL_FRAGMENT_SHADER, fragmentShader.readFullTextFile());
        shader.link();
        shader.bind();
        
        Log.info("Created shaders");
        
        mesh = new Mesh();
        mesh.setVertices(verts);
        mesh.setIndices(inds);
        mesh.build();
        
        Log.info("Uploaded mesh");
    }
    
    protected void onUpdate() {
        if (Input.getKeyDown(Key.T)) {
            Log.info("Key pressed: T");
        }
    }
    
    protected void onRender(float deltaTime) {
        mesh.render();
    }
    
}