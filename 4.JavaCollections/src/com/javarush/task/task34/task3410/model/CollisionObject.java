package com.javarush.task.task34.task3410.model;

import static com.javarush.task.task34.task3410.model.Direction.*;
import static com.javarush.task.task34.task3410.model.Model.FIELD_CELL_SIZE;

public abstract class CollisionObject extends GameObject {
    public CollisionObject(int x, int y) {
        super(x, y);
    }

    public boolean isCollision(GameObject gameObject, Direction direction) {
        return gameObject.getX() == getX() + FIELD_CELL_SIZE * (direction == LEFT ? -1 : direction == RIGHT ? 1 : 0)
                && gameObject.getY() == getY() + FIELD_CELL_SIZE * (direction == UP ? -1 : direction == DOWN ? 1 : 0);
    }
}
