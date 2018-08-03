package com.cjburkey.jbnge.input;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.joml.Vector2f;
import com.cjburkey.jbnge.GameEngine;
import com.cjburkey.jbnge.Log;

public class Input {
    
    private static final Set<Key> newKeys = new HashSet<>();
    private static final Set<Mouse> newMouse = new HashSet<>();
    private static final Map<Key, Boolean> pressedKeys = new HashMap<>();
    private static final Map<Mouse, Boolean> pressedMouse = new HashMap<>();
    
    private static final Vector2f cursorPos = new Vector2f();
    private static final Vector2f previousCursorPos = new Vector2f();
    private static final Vector2f cursorDeltaPos = new Vector2f();
    
    public static void onKeyPress(int key) {
        GameEngine.instance.queueUpdate(false, (dt) -> {
            newKeys.add(Key.getKey(key));
            pressedKeys.put(Key.getKey(key), true);
        });
    }
    
    public static void onKeyRelease(int key) {
        GameEngine.instance.queueUpdate(false, (dt) -> pressedKeys.put(Key.getKey(key), false));
    }
    
    public static void onMousePress(int button) {
        GameEngine.instance.queueUpdate(false, (dt) -> {
            newMouse.add(Mouse.getButton(button));
            pressedMouse.put(Mouse.getButton(button), true);
        });
    }
    
    public static void onMouseRelease(int button) {
        GameEngine.instance.queueUpdate(false, (dt) -> pressedMouse.put(Mouse.getButton(button), false));
    }
    
    public static void onCursorMove(float x, float y) {
        previousCursorPos.set(cursorPos);
        cursorPos.set(x, y);
        cursorPos.sub(previousCursorPos, cursorDeltaPos);
    }
    
    public static void onUpdate() {
        newKeys.clear();
        newMouse.clear();
        for (Entry<Key, Boolean> pressedKey : pressedKeys.entrySet()) {
            if (!pressedKey.getValue()) {
                pressedKeys.remove(pressedKey.getKey());
            }
        }
        for (Entry<Mouse, Boolean> pressed : pressedMouse.entrySet()) {
            if (!pressed.getValue()) {
                pressedMouse.remove(pressed.getKey());
            }
        }
    }
    
    public static boolean getKey(Key key) {
        if (checkThread()) {
            return pressedKeys.containsKey(key) && pressedKeys.get(key);
        }
        return false;
    }
    
    public static boolean getKeyDown(Key key) {
        if (checkThread()) {
            return newKeys.contains(key);
        }
        return false;
    }
    
    public static boolean getKeyUp(Key key) {
        if (checkThread()) {
            return pressedKeys.containsKey(key) && !pressedKeys.get(key);
        }
        return false;
    }
    
    public static boolean getMouseButton(Mouse mouse) {
        if (checkThread()) {
            return pressedMouse.containsKey(mouse) && pressedMouse.get(mouse);
        }
        return false;
    }
    
    public static boolean getMouseButtonDown(Mouse mouse) {
        if (checkThread()) {
            return newMouse.contains(mouse);
        }
        return false;
    }
    
    public static boolean getMouseButtonUp(Mouse mouse) {
        if (checkThread()) {
            return pressedMouse.containsKey(mouse) && !pressedMouse.get(mouse);
        }
        return false;
    }
    
    public static Vector2f getCursorPos() {
        return new Vector2f(cursorPos);
    }
    
    public static Vector2f getDeltaCursor() {
        return new Vector2f(cursorDeltaPos);
    }
    
    private static boolean checkThread() {
        if (!GameEngine.instance.getInUpdateLoop()) {
            Log.error("Cannot access input from outside of the update loop");
            return false;
        }
        return true;
    }
    
}