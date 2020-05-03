package com.tictactoe.Checkers;

import com.tictactoe.CustomElements.CustomButton;

public class MoveChecker {

    private int mapSize = 0;

    public int getWinLineLength() {
        return winLineLength;
    }

    private int winLineLength = 5;

    public EGameStatus checkGameStatus(CustomButton[][] buttons, CustomButton btn) {

        Boolean winLineExists = false;

        winLineExists = checkHorizontalLine(buttons, btn);

        if (winLineExists) return EGameStatus.WIN;

        winLineExists = checkVerticalLine(buttons, btn);

        if (winLineExists) return EGameStatus.WIN;

        winLineExists = checkTopLeftRightBottomLine(buttons, btn);

        if (winLineExists) return EGameStatus.WIN;

        winLineExists = checkBottomLeftTopRightLine(buttons, btn);

        if (winLineExists) return EGameStatus.WIN;

        winLineExists = checkDraw(buttons);

        if (winLineExists) return EGameStatus.DRAW;

        return EGameStatus.RUNNING;
    }

    private Boolean checkHorizontalLine(CustomButton[][] buttons, CustomButton btn) {
        if (btn == null) return false;

        int currentWinLineLength = 1;

        String marker = (String) btn.getText();
        int y = btn.getButtonY();

        // Left.
        for(int x = btn.getButtonX() - 1; x >= 0; x--) {
            if (currentWinLineLength == winLineLength) return true;

            if (buttons[x][y].getText().equals(marker)) {
                currentWinLineLength++;
            }
            else break;
        }

        // Right
        for(int x = btn.getButtonX() + 1; x < mapSize; x++) {
            if (currentWinLineLength == winLineLength) return true;

            if (buttons[x][y].getText().equals(marker)) {
                currentWinLineLength++;
            }
            else break;
        }

        if (currentWinLineLength == winLineLength) return true;
        else return false;
    }

    private Boolean checkVerticalLine(CustomButton[][] buttons, CustomButton btn) {
        if (btn == null) return false;

        int currentWinLineLength = 1;

        String marker = (String) btn.getText();
        int x = btn.getButtonX();

        // Up.
        for(int y = btn.getButtonY() - 1; y >= 0; y--) {
            if (currentWinLineLength == winLineLength) return true;

            if (buttons[x][y].getText().equals(marker)) {
                currentWinLineLength++;
            }
            else break;
        }

        // Bot.
        for(int y = btn.getButtonY() + 1; y < mapSize; y++) {
            if (currentWinLineLength == winLineLength) return true;

            if (buttons[x][y].getText().equals(marker)) {
                currentWinLineLength++;
            }
            else break;
        }

        if (currentWinLineLength == winLineLength) return true;
        else return false;
    }



    private Boolean checkTopLeftRightBottomLine(CustomButton[][] buttons, CustomButton btn) {
        if (btn == null) return false;

        int currentWinLineLength = 1;

        String marker = (String) btn.getText();

        int x = btn.getButtonX() - 1;
        int y = btn.getButtonY() - 1;

        // Left top.
        while(x >= 0 && y >= 0) {
            if (currentWinLineLength == winLineLength) return true;

            if (buttons[x][y].getText().equals(marker)) {
                currentWinLineLength++;
            }
            else break;

            x--;
            y--;
        }

        x = btn.getButtonX() + 1;
        y = btn.getButtonY() + 1;

        // Right bot.
        while(x < mapSize && y < mapSize) {
            if (currentWinLineLength == winLineLength) return true;

            if (buttons[x][y].getText().equals(marker)) {
                currentWinLineLength++;
            }
            else break;

            x++;
            y++;
        }

        if (currentWinLineLength == winLineLength) return true;
        else return false;

    }

    private Boolean checkBottomLeftTopRightLine(CustomButton[][] buttons, CustomButton btn) {
        if (btn == null) return false;

        int currentWinLineLength = 1;

        String marker = (String) btn.getText();

        int x = btn.getButtonX() + 1;
        int y = btn.getButtonY() - 1;

        // Right top.
        while(x < mapSize && y >= 0) {
            if (currentWinLineLength == winLineLength) return true;

            if (buttons[x][y].getText().equals(marker)) {
                currentWinLineLength++;
            }
            else break;

            x++;
            y--;
        }

        x = btn.getButtonX() - 1;
        y = btn.getButtonY() + 1;

        // Left bot.
        while(x >= 0 && y < mapSize) {
            if (currentWinLineLength == winLineLength) return true;

            if (buttons[x][y].getText().equals(marker)) {
                currentWinLineLength++;
            }
            else break;

            x--;
            y++;
        }

        if (currentWinLineLength == winLineLength) return true;
        else return false;
    }

    private Boolean checkDraw(CustomButton[][] buttons) {
        for (int i = 0; i < mapSize; i++) {
            for (int j = 0; j < mapSize; j++) {
                if (buttons[i][j].getText().equals("")) return false;
            }
        }

        return true;
    }

    public void setMapSize(int mapSize) {
        this.mapSize = mapSize;
    }
}
