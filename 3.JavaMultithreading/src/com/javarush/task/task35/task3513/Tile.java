package com.javarush.task.task35.task3513;

import java.awt.*;

public class Tile {
    int value;

    public Tile(int value) {
        this.value = value;
    }

    public Tile() {
        this.value = 0;
    }

    public boolean isEmpty() {
        return value == 0;
    }

    public Color getFontColor() {
        return new Color(value < 16 ? 0x776e65 : 0xf9f6f2);
    }

    public Color getTileColor() {
        int code = 0xff0000;
        switch (value) {
            case 0:
                code = 0xcdc1b4;
                break;
            case 2:
                code = 0xeee4da;
                break;
            case 4:
                code = 0xede0c8;
                break;
            case 8:
                code = 0xf2b179;
                break;
            case 16:
                code = 0xf59563;
                break;
            case 32:
                code = 0xf67c5f;
                break;
            case 64:
                code = 0xf65e3b;
                break;
            case 128:
                code = 0xedcf72;
                break;
            case 256:
                code = 0xedcc61;
                break;
            case 512:
                code = 0xedc850;
                break;
            case 1024:
                code = 0xedc53f;
                break;
            case 2048:
                code = 0xedc22e;
                break;
        }
        return new Color(code);
    }
}
