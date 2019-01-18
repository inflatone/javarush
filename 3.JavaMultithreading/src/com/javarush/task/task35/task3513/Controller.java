package com.javarush.task.task35.task3513;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Controller extends KeyAdapter {
    private final static int WINNING_TILE = 2048;
    private Model model;
    private View view;

    public Controller(Model model) {
        this.model = model;
        this.view = new View(this);
    }

    public Tile[][] getGameTiles() {
        return model.getGameTiles();
    }

    public int getScore() {
        return model.score;
    }

    public void resetGame() {
        model.score = 0;
        model.maxTile = 0;
        view.isGameWon = false;
        view.isGameLost = false;
        model.resetGameTiles();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE || view.isGameLost) {
            resetGame();
        } else if (!model.canMove()) {
            view.isGameLost = true;
        } else if (!view.isGameLost && !view.isGameWon) {
            processStep(e.getKeyCode());
            if (model.maxTile == WINNING_TILE) {
                view.isGameWon = true;
            }
        }
        view.repaint();
    }

    private void processStep(int keyCode) {
        if (keyCode == KeyEvent.VK_Z) {
            model.rollback();
        } else if (keyCode == KeyEvent.VK_R) {
            model.randomMove();
        } else if (keyCode == KeyEvent.VK_A) {
            model.autoMove();
        } else if (keyCode == KeyEvent.VK_LEFT) {
            model.left();
        } else if (keyCode == KeyEvent.VK_RIGHT) {
            model.right();
        } else if (keyCode == KeyEvent.VK_UP) {
            model.up();
        } else if (keyCode == KeyEvent.VK_DOWN) {
            model.down();
        }
    }

    public View getView() {
        return view;
    }
}
