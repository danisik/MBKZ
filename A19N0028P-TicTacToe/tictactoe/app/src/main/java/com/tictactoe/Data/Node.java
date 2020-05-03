package com.tictactoe.Data;

import android.graphics.Point;

import com.tictactoe.CustomElements.CustomButton;

public class Node {
    public Point p;
    private int player;

    public Node(int x, int y, int player) {
        this.p = new Point(x,y);
        this.player = player;
    }

    public int getPlayer() {
        return player;
    }

    public void setPlayer(int player) {
        this.player = player;
    }
}
