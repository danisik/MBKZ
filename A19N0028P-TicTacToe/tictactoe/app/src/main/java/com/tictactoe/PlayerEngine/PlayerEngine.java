package com.tictactoe.PlayerEngine;

import com.tictactoe.Data.Constants;
import com.tictactoe.Data.Node;
import com.tictactoe.MainActivity;

import java.util.ArrayList;
import java.util.Random;

public class PlayerEngine {

    MainActivity activity;
    private Node[][] grid;
    int winLineLength = 0;
    int depth = 0;
    int maxDepth = 4;

    public PlayerEngine(MainActivity activity) {
        this.activity = activity;
        winLineLength = activity.moveChecker.getWinLineLength();
    }

    public int[] getOptimalButton() {
        initGrid();
        int[] bestMove = alternateFindBestMove(grid, true, null);
        /*
        int x = 0;
        int y = 0;
        if (bestMove[0] == 0) {
            while(true) {
                Random r = new Random();
                x = r.nextInt(activity.mapSize - 1);
                y = r.nextInt(activity.mapSize - 1);

                if (activity.buttons[x][y].getPlayer() == Constants.NO_PLAYER) break;
            }
            bestMove[1] = x;
            bestMove[2] = y;
        }
         */
        return bestMove;
    }

    public void initGrid() {
        grid = new Node[activity.mapSize][activity.mapSize];
        for (int i = 0; i < activity.mapSize; i++)
            for (int j = 0; j < activity.mapSize; j++)
                grid[i][j] = new Node(i, j, activity.buttons[i][j].getPlayer());
    }

    ArrayList<Node> availableNodes() {
        ArrayList<Node> nodes = new ArrayList<>();

        for (int i = 0; i < activity.mapSize; i++) {
            for (int j = 0; j < activity.mapSize; j++) {
                if (grid[i][j].getPlayer() == Constants.NO_PLAYER)
                    nodes.add(grid[i][j]);
            }
        }
        return nodes;
    }

    public int[] alternateFindBestMove(Node[][] board, boolean AITurn, Node node) {
        depth++;
        // If the game is already over with this board, return the result

        boolean status = hasWon(node);
        if (status) {
            int cost = !AITurn ? 1 : -1;
            depth--;
            return new int[] {cost, -1, -1};
        }
        else {
            if (availableNodes().isEmpty()) {
                depth--;
                return new int[] {0, -1, -1};
            }
        }

        if (depth >= maxDepth) {
            depth--;
            return new int[] {0, -1, -1};
        }

        // If the game is still going, check all the possible moves
        // and choose the one with the most favorable outcome for the player

        int bestX = -1, bestY = -1, result = AITurn ? -1:1;

        for (int i = 0; i < board.length; i++)
            for (int a = 0; a < board[i].length; a++) {
                if (board[i][a].getPlayer() != Constants.NO_PLAYER)
                    continue;
                if (AITurn) board[i][a].setPlayer(Constants.PLAYER_2_ID);
                else board[i][a].setPlayer(Constants.PLAYER_1_ID);

                int tempResult = alternateFindBestMove(board, !AITurn, board[i][a])[0];
                board[i][a].setPlayer(Constants.NO_PLAYER);

                // Check if the result is favorable for the player
                if ((AITurn && tempResult > result) || (!AITurn && tempResult < result)) {
                    bestX = i;
                    bestY = a;
                    result = tempResult;
                }
                else if (tempResult == result && Math.random() > 0.2) { // element of randomness, optional
                    bestX = i;
                    bestY = a;
                    result = tempResult;
                }
            }

        depth--;
        return new int[] {result, bestX, bestY};
    }

    public boolean hasWon(Node node) {

        if (node == null) return false;

        Boolean winLineExists = false;

        winLineExists = checkHorizontalLine(grid, node);

        if (winLineExists) return true;

        winLineExists = checkVerticalLine(grid, node);

        if (winLineExists) return true;

        winLineExists = checkTopLeftRightBottomLine(grid, node);

        if (winLineExists) return true;

        winLineExists = checkBottomLeftTopRightLine(grid, node);

        if (winLineExists) return true;

        return false;
    }

    private Boolean checkHorizontalLine(Node[][] grid, Node node) {
        if (node == null) return false;

        int currentWinLineLength = 1;

        int y = node.p.y;

        // Left.
        for(int x = node.p.x - 1; x >= 0; x--) {
            if (currentWinLineLength == winLineLength) return true;

            if (grid[x][y].getPlayer() == node.getPlayer()) {
                currentWinLineLength++;
            }
            else break;
        }

        // Right
        for(int x = node.p.x + 1; x < activity.mapSize; x++) {
            if (currentWinLineLength == winLineLength) return true;

            if (grid[x][y].getPlayer() == node.getPlayer()) {
                currentWinLineLength++;
            }
            else break;
        }

        if (currentWinLineLength == winLineLength) return true;
        else return false;
    }

    private Boolean checkVerticalLine(Node[][] grid, Node node) {
        if (node == null) return false;

        int currentWinLineLength = 1;

        int x = node.p.x;

        // Up.
        for(int y = node.p.y - 1; y >= 0; y--) {
            if (currentWinLineLength == winLineLength) return true;

            if (grid[x][y].getPlayer() == node.getPlayer()) {
                currentWinLineLength++;
            }
            else break;
        }

        // Bot.
        for(int y = node.p.y + 1; y < activity.mapSize; y++) {
            if (currentWinLineLength == winLineLength) return true;

            if (grid[x][y].getPlayer() == node.getPlayer()) {
                currentWinLineLength++;
            }
            else break;
        }

        if (currentWinLineLength == winLineLength) return true;
        else return false;
    }



    private Boolean checkTopLeftRightBottomLine(Node[][] grid, Node node) {
        if (node == null) return false;

        int currentWinLineLength = 1;


        int x = node.p.x - 1;
        int y = node.p.y - 1;

        // Left top.
        while(x >= 0 && y >= 0) {
            if (currentWinLineLength == winLineLength) return true;

            if (grid[x][y].getPlayer() == node.getPlayer()) {
                currentWinLineLength++;
            }
            else break;

            x--;
            y--;
        }

        x = node.p.x + 1;
        y = node.p.y + 1;

        // Right bot.
        while(x < activity.mapSize && y < activity.mapSize) {
            if (currentWinLineLength == winLineLength) return true;

            if (grid[x][y].getPlayer() == node.getPlayer()) {
                currentWinLineLength++;
            }
            else break;

            x++;
            y++;
        }

        if (currentWinLineLength == winLineLength) return true;
        else return false;

    }

    private Boolean checkBottomLeftTopRightLine(Node[][] grid, Node node) {
        if (node == null) return false;

        int currentWinLineLength = 1;

        int x = node.p.x + 1;
        int y = node.p.y - 1;

        // Right top.
        while(x < activity.mapSize && y >= 0) {
            if (currentWinLineLength == winLineLength) return true;

            if (grid[x][y].getPlayer() == node.getPlayer()) {
                currentWinLineLength++;
            }
            else break;

            x++;
            y--;
        }

        x = node.p.x - 1;
        y = node.p.y + 1;

        // Left bot.
        while(x >= 0 && y < activity.mapSize) {
            if (currentWinLineLength == winLineLength) return true;

            if (grid[x][y].getPlayer() == node.getPlayer()) {
                currentWinLineLength++;
            }
            else break;

            x--;
            y++;
        }

        if (currentWinLineLength == winLineLength) return true;
        else return false;
    }
}
