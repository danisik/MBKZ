package com.tictactoe.CustomElements;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.Button;

@SuppressLint("AppCompatCustomView")
public class CustomButton extends Button {

    private int buttonX;
    private int buttonY;
    private int player;

    public CustomButton(Context context) {
        super(context);
    }

    public int getButtonX() {
        return buttonX;
    }

    public void setButtonX(int buttonX) {
        this.buttonX = buttonX;
    }

    public int getButtonY() {
        return buttonY;
    }

    public void setButtonY(int buttonY) {
        this.buttonY = buttonY;
    }

    public int getPlayer() {
        return player;
    }

    public void setPlayer(int player) {
        this.player = player;
    }
}

