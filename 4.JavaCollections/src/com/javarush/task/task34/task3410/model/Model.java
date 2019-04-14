package com.javarush.task.task34.task3410.model;

import com.javarush.task.task34.task3410.controller.EventListener;

import java.nio.file.Paths;
import java.util.Set;

import static com.javarush.task.task34.task3410.model.Direction.*;

public class Model {
    public static final int FIELD_CELL_SIZE = 20;

    private EventListener eventListener;

    private GameObjects gameObjects;

    private int currentLevel = 1;

    private LevelLoader levelLoader = new LevelLoader(Paths.get("4.JavaCollections/src/com/javarush/task/task3410/res/levels.txt"));

    public void setEventListener(EventListener eventListener) {
        this.eventListener = eventListener;
    }

    public GameObjects getGameObjects() {
        return gameObjects;
    }

    public void restartLevel(int level) {
        gameObjects = levelLoader.getLevel(level);
    }

    public void restart() {
        restartLevel(currentLevel);
    }

    public void startNextLevel() {
        restartLevel(++currentLevel);
    }

    public void move(Direction direction) {
        Player player = gameObjects.getPlayer();
        if (!checkWallCollision(player, direction) && !checkBoxCollisionAndMoveIfAvaliable(direction)) {
            move(player, direction);
            checkCompletion();
        }
    }

    public boolean checkWallCollision(CollisionObject gameObject, Direction direction) {
        return checkCollision(gameObject, direction, gameObjects.getWalls());
    }

    public boolean checkBoxCollisionAndMoveIfAvaliable(Direction direction) {
        Player player = gameObjects.getPlayer();
        for (Box box : gameObjects.getBoxes()) {
            if (player.isCollision(box, direction)) {
                if (checkWallCollision(box, direction) || checkCollision(box, direction, gameObjects.getBoxes())) {
                    return true;
                }
                move(box, direction);
            }
        }
        return false;
    }

    private void move(Movable gameObject, Direction direction) {
        gameObject.move(
                FIELD_CELL_SIZE * (direction == RIGHT ? 1 : direction == LEFT ? -1 : 0),
                FIELD_CELL_SIZE * (direction == DOWN ? 1 : direction == UP ? -1 : 0)
        );
    }

    private boolean checkCollision(CollisionObject gameObject, Direction direction, Set<? extends CollisionObject> checkedObjects) {
        return checkedObjects.stream().anyMatch(o -> gameObject.isCollision(o, direction));
    }

    public void checkCompletion() {
        boolean isComplete = true;
        for (Box box : gameObjects.getBoxes()) {
            if (gameObjects.getHomes().stream().noneMatch(h -> h.getX() == box.getX() && h.getY() == box.getY())) {
                isComplete = false;
                break;
            }
        }
        if (isComplete) {
            eventListener.levelCompleted(currentLevel);
        }
    }
}
