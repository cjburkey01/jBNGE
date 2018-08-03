package com.cjburkey.jbnge.graphics;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.joml.Vector3f;
import com.cjburkey.jbnge.GameEngine;

public class Mesh {
    
    private static final Queue<Mesh> meshes = new ConcurrentLinkedQueue<>();
    
    private Vector3f[] vertices;
    private short[] indices;
    private int indexCount;
    private int vao = -1, vbo = -1, ebo = -1;
    
    private boolean built;
    
    public void setVertices(Vector3f[] vertices) {
        this.vertices = vertices;
        if (built) {
            GameEngine.instance.queueRender(true, (deltaTime) -> {
                glBindVertexArray(vao);
                bufferVerts();
                glBindVertexArray(0);
            });
        }
    }
    
    public void setIndices(short[] indices) {
        this.indices = indices;
        indexCount = indices.length;
        if (built) {
            GameEngine.instance.queueRender(true, (deltaTime) -> {
                glBindVertexArray(vao);
                bufferInds();
                glBindVertexArray(0);
            });
        }
    }
    
    public void build() {
        if (built) {
            return;
        }
        built = true;   // Sad attempt at thread safety
        GameEngine.instance.queueRender(true, (deltaTime) -> {
            built = false;
            
            vao = glGenVertexArrays();
            vbo = glGenBuffers();
            ebo = glGenBuffers();
            
            glBindVertexArray(vao);
            bufferVerts();
            bufferInds();
            glBindVertexArray(0);
            
            built = true;
            meshes.offer(this);
        });
    }
    
    private void bufferVerts() {
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, generateFloatsFromVectors(vertices), GL_STATIC_DRAW);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }
    
    private void bufferInds() {
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
    }
    
    public void render() {
        if (!built) {
            return;
        }
        GameEngine.instance.queueRender(true, (deltaTime) -> {
            glBindVertexArray(vao);
            glEnableVertexAttribArray(0);
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);
            glDrawElements(GL_TRIANGLES, indexCount, GL_UNSIGNED_SHORT, 0);
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
            glDisableVertexAttribArray(0);
            glBindVertexArray(0);
        });
    }
    
    public void destroy() {
        if (meshes.contains(this)) {
            meshes.remove(this);
        }
        GameEngine.instance.queueRender(true, (deltaTime) -> {
            if (vao >= 0) {
                glDeleteVertexArrays(vao);
            }
            if (vbo >= 0) {
                glDeleteBuffers(vbo);
            }
            if (ebo >= 0) {
                glDeleteBuffers(ebo);
            }
        });
    }
    
    public static void cleanup() {
        Mesh mesh;
        while (!meshes.isEmpty()) {
            mesh = meshes.poll();
            if (mesh.built) {
                mesh.destroy();
            }
        }
    }
    
    public static final float[] generateFloatsFromVectors(Vector3f[] vecs) {
        float[] out = new float[vecs.length * 3];
        for (int i = 0; i < vecs.length; i ++) {
            out[i * 3] = vecs[i].x;
            out[i * 3 + 1] = vecs[i].y;
            out[i * 3 + 2] = vecs[i].z;
        }
        return out;
    }
    
}