package com.javarush.task.task35.task3513;

import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

public class Model {
    private final static int FIELD_WIDTH = 4;
    int score, maxTile;
    private Tile[][] gameTiles;

    private Stack<Tile[][]> previousStates = new Stack<>();
    private Stack<Integer> previousScores = new Stack<>();
    private boolean isSaveNeeded = true;

    public Model() {
        resetGameTiles();
    }

    public Tile[][] getGameTiles() {
        return gameTiles;
    }

    public void left() {
        saveState(gameTiles);
        move();
    }

    public void right() {
        saveState(gameTiles);
        rotate();
        rotate();
        move();
        rotate();
        rotate();

    }

    public void up() {
        saveState(gameTiles);
        rotate();
        move();
        rotate();
        rotate();
        rotate();
    }

    public void down() {
        saveState(gameTiles);
        rotate();
        rotate();
        rotate();
        move();
        rotate();
    }

    public boolean canMove() {
        boolean result = !getEmptyTiles().isEmpty();
        for (int i = 0; i < FIELD_WIDTH && !result; i++) {
            for (int j = 0; j < FIELD_WIDTH - 1; j++) {
                if (
                        gameTiles[i][j].value == gameTiles[i][j + 1].value
                                || gameTiles[j][i].value == gameTiles[j + 1][i].value
                ) {
                    result = true;
                    break;
                }
            }
        }
        return result;
    }

    void resetGameTiles() {
        gameTiles = new Tile[FIELD_WIDTH][FIELD_WIDTH];
        for (int i = 0; i < FIELD_WIDTH; i++) {
            for (int j = 0; j < FIELD_WIDTH; j++) {
                gameTiles[i][j] = new Tile();
            }
        }
        addTile();
        addTile();
        score = 0;
        maxTile = 0;
    }

    private void addTile() {
        List<Tile> emptyTiles = getEmptyTiles();
        if (!emptyTiles.isEmpty()) {
            emptyTiles
                    .get((int) (Math.random() * emptyTiles.size()))
                    .value = Math.random() < 0.9 ? 2 : 4;
        }
    }

    private List<Tile> getEmptyTiles() {
        return Arrays.stream(gameTiles).flatMap(Arrays::stream)
                .filter(tile -> tile.value == 0)
                .collect(Collectors.toList());
    }

    private boolean compressTiles(Tile[] tiles) {
        boolean result = false;
        for (int i = 0, offset = 0; i < tiles.length; i++) {
            if (tiles[i].value == 0) {
                offset++;
            } else if (offset != 0) {
                swap(tiles[i - offset], tiles[i]);
                result = true;
            }
        }
        return result;
    }

    private boolean mergeTiles(Tile[] tiles) {
        boolean result = false;
        for (int i = 1, offset = 1; i < tiles.length && tiles[i].value != 0; i++) {
            if (tiles[i - offset].value == tiles[i].value) {
                result = true;
                tiles[i - offset].value *= 2;
                tiles[i].value = 0;
                updateScore(tiles[i - offset]);
                offset++;
                i++;
            }
            if (offset > 1) {
                shiftBackCurrentTile(tiles, i, offset);
            }
        }
        return result;
    }

    private void shiftBackCurrentTile(Tile[] tiles, int i, int offset) {
        if (i != tiles.length) {
            tiles[i - offset + 1].value = tiles[i].value;
            tiles[i].value = 0;
        } else {
            tiles[i - offset + 1].value = 0;
        }
    }

    private void updateScore(Tile tile) {
        score += tile.value;
        if (maxTile < tile.value) {
            maxTile = tile.value;
        }
    }

    private void swap(Tile a, Tile b) {
        a.value ^= b.value;
        b.value ^= a.value;
        a.value ^= b.value;
    }

    private void move() {
        boolean isChanged = false;
        for (Tile[] line : gameTiles) {
            isChanged = compressTiles(line) | mergeTiles(line) || isChanged;
        }
        if (isChanged) {
            addTile();
        }
    }

    private void rotate() {
        Tile[][] copy = new Tile[FIELD_WIDTH][FIELD_WIDTH];

        for (int i = 0; i < FIELD_WIDTH; i++) {
            for (int j = 0; j < FIELD_WIDTH; j++) {
                copy[i][j] = gameTiles[j][FIELD_WIDTH - i - 1];
            }
        }
        gameTiles = copy;
    }

    private Tile[][] getDeepCopy(Tile[][] tiles) {
        Tile[][] result = new Tile[FIELD_WIDTH][FIELD_WIDTH];
        for (int i = 0; i < FIELD_WIDTH; i++) {
            for (int j = 0; j < FIELD_WIDTH; j++) {
                result[i][j] = new Tile(tiles[i][j].value);
            }
        }
        return result;
    }

    private void saveState(Tile[][] tiles) {
        previousStates.push(getDeepCopy(tiles));
        previousScores.push(score);
        isSaveNeeded = false;
    }

    public void rollback() {
        if (!previousScores.empty() && !previousStates.empty()) {
            gameTiles = previousStates.pop();
            score = previousScores.pop();
        }


    }
}