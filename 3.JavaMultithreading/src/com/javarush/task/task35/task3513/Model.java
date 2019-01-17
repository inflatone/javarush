package com.javarush.task.task35.task3513;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Model {
    private final static int FIELD_WIDTH = 4;
    int score, maxTile;
    private Tile[][] gameTiles;

    public Model() {
        resetGameTiles();
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

    private void compressTiles(Tile[] tiles) {
        for (int i = 0, offset = 0; i < tiles.length; i++) {
            if (tiles[i].value == 0) {
                offset++;
            } else if (offset != 0) {
                swap(tiles[i - offset], tiles[i]);
            }
        }
    }

    private void mergeTiles(Tile[] tiles) {
        for (int i = 1, offset = 1; i < tiles.length && tiles[i].value != 0; i++) {
            if (tiles[i - offset].value == tiles[i].value) {
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

}
