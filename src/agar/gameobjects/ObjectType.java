package agar.gameobjects;

import java.io.Serializable;


public enum ObjectType implements Serializable {

    ORDINARY_FOOD(0),
    PLAYER(1),
    GOD_MODE(2),
    JOIN_ALL(3),
    SPEED_UP(4),
    DESTROYER(5),
    HALFENER(6),
    SAW(7);

    private int stateID;

    private  ObjectType(int stateID) {
        this.stateID = stateID;
    }

    public int getObjectTypeID() {
        return stateID;
    }
}
