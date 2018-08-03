package com.cjburkey.jbnge.input;

//Built from GLFW
/*

for (Field field : GLFW.class.getDeclaredFields()) {
    if (field.getName().startsWith("GLFW_MOUSE_")) {
        try {
            System.out.println(field.getName().substring("GLFW_MOUSE_".length()) + '(' + field.get(null) + "),");
        } catch (IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }
}

*/
public enum Mouse {
    
    UNKNOWN(-1),
    ONE(0),
    BUTTON_2(1),
    BUTTON_3(2),
    BUTTON_4(3),
    BUTTON_5(4),
    BUTTON_6(5),
    BUTTON_7(6),
    BUTTON_8(7),
    LAST(7),
    LEFT(0),
    RIGHT(1),
    MIDDLE(2),
    
    ;
    
    public final int button;
    
    private Mouse(int button) {
        this.button = button;
    }
    
    public static Mouse getButton(int code) {
        for (Mouse mouse : values()) {
            if (mouse.button == code) {
                return mouse;
            }
        }
        return UNKNOWN;
    }
    
}