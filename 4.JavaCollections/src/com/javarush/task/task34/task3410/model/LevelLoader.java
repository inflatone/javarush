package com.javarush.task.task34.task3410.model;

import java.nio.file.Path;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.javarush.task.task34.task3410.model.Model.FIELD_CELL_SIZE;

public class LevelLoader {
    private Path levels;

    public LevelLoader(Path levels) {
        this.levels = levels;
    }

    public GameObjects getLevel(int level) {
        return new GameObjects(
                Stream.of(
                        new Wall(FIELD_CELL_SIZE * 0, FIELD_CELL_SIZE), new Wall(FIELD_CELL_SIZE * 3, FIELD_CELL_SIZE)
                ).collect(Collectors.toSet()),
                Collections.singleton(new Box(FIELD_CELL_SIZE * 2, FIELD_CELL_SIZE)),
                Collections.singleton(new Home(FIELD_CELL_SIZE * 3, FIELD_CELL_SIZE * 2)),
                new Player(FIELD_CELL_SIZE * 4, FIELD_CELL_SIZE * 2)
        );
    }
}
