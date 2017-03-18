/**
 * Class: CS 3331, Advanced Object Oriented Programming
 * Instructor: Omar Ochoa
 * Author: Jose G Perez (UTEP ID: 80473954)
 * Contact: <josegperez@mail.com> or <jperez50@miners.utep.edu>
 * Last Date Modified: 5/1/2016
 */
package edu.utep.cs3350.connect4.perezJose.connectFour;

import edu.utep.cs3350.connect4.perezJose.database.Database;
import edu.utep.cs3350.connect4.perezJose.database.Saveable;

class Board implements Saveable {
    public static final char EMPTY_TOKEN = '#';
    public static final Direction[] ALL_DIRECTIONS = new Direction[] { Direction.Down, Direction.Left, Direction.LeftDiagonalDown,
            Direction.LeftDiagonalUp, Direction.Right, Direction.RightDiagonalDown, Direction.RightDiagonalUp, Direction.Up };

    private char[][] grid;

    private int rows;

    public int getRows() {
        return rows;
    }

    private int columns;

    public int getColumns() {
        return columns;
    }

    private int totalPieces;

    public Board(int rows, int columns) {
        restart(rows, columns);

    }

    public void restart(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        grid = new char[rows][columns];

        for (int row = 0; row < grid.length; row++)
            for (int col = 0; col < grid[row].length; col++)
                grid[row][col] = EMPTY_TOKEN;
    }

    /**
     * Attempts to place a token inside the grid.
     * Based on the rules of ConnectFour you select a column to place your token
     * in
     * and then drop it. The token falls to the bottom of that column.
     * 
     * Assumption: Column has been already been adjusted for array operations
     * [Index begins at 0]
     * @param token The symbol of the token that will be placed (if successful)
     * @param column Column to try to drop the token in
     * @return One of many possible placement results
     */
    public PlacementResult placeToken(char token, int column) {
        if (column < 0 || column >= grid[0].length)
            return PlacementResult.OUTOFBOUNDS;

        if (grid[0][column] != EMPTY_TOKEN)
            return PlacementResult.COLUMNFULL;

        int placedRow = -3;

        /*
         * Go to the last row in the column that's not taken
         * (gravity makes the piece fall down in real life)
         * if it's not empty place the token there
         * otherwise keep moving up the row
         */
        for (int row = grid.length - 1; row >= 0; row--) {
            if (grid[row][column] == EMPTY_TOKEN) {
                grid[row][column] = token;
                placedRow = row;
                totalPieces++;
                break;
            }
        }
        if (checkWin(placedRow, column, token))
            return PlacementResult.WIN;

        if (checkTie())
            return PlacementResult.TIE;

        return PlacementResult.GOOD;
    }

    public char tokenAt(int row, int column) {
        return grid[row][column];
    }

    /**
     * Check if a coordinate is out of bounds (not within the board)
     * @param row Number of row to check
     * @param column Number of column to check
     * @return True if the specified point is not within the bounds of the game
     *         board
     */
    public boolean outOfBounds(int row, int column) {
        return (row < 0) || (row >= grid.length) || (column < 0) || (column >= grid[0].length);
    }

    /**
     * Checks if the placed token coordinate has won in the specified direction
     * (4 tokens are in the same direction)
     * @param row Row of the token placed
     * @param col Column of the token placed
     * @param token Character used for the token
     * @param direction Direction to check for winning conditions
     * @return True if there are 4 tokens in the same direction (including the
     *         one it was placed on), false otherwise
     */
    private boolean checkDirection(int row, int col, char token, Direction direction) {
        int tempRow = row;
        int tempCol = col;

        /*
         * We can skip and start at 1 then check the following 3 pieces
         * We assume we are checking because a piece was successfully placed
         * therefore we don't need to check the piece at grid[row][column] or
         * i = 0
         * 
         * All directions are tested (following regular connect 4 rules)
         */
        for (int i = 1; i < 4; i++) {
            switch (direction) {
                case Up:
                    tempRow--;
                    break;
                case Down:
                    tempRow++;
                    break;
                case Left:
                    tempCol--;
                    break;
                case Right:
                    tempCol++;
                    break;
                case LeftDiagonalUp:
                    tempRow--; // Same as Up
                    tempCol--; // Same as Left
                    break;
                case LeftDiagonalDown:
                    tempRow++; // Same as Down
                    tempCol--; // Same as Left
                    break;
                case RightDiagonalUp:
                    tempRow--; // Same as Up
                    tempCol++; // Same as Right
                    break;
                case RightDiagonalDown:
                    tempRow++; // Same as Right
                    tempCol++; // Same as Down
                    break;
            }

            if (outOfBounds(tempRow, tempCol))
                return false;

            if (grid[tempRow][tempCol] != token)
                return false;
        }
        return true;
    }

    /**
     * Checks if the placed token coordinate has won in any direction
     * (4 tokens are in the same direction)
     * @param row Row of the token placed
     * @param col Column of the token placed
     * @param token Character used for the token
     * @return True if there are 4 tokens in the same direction (including the
     *         one it was placed on), false otherwise
     */
    private boolean checkWin(int row, int col, char token) {
        for (Direction direction : ALL_DIRECTIONS) {
            if (checkDirection(row, col, token, direction))
                return true;
        }

        return false;
    }

    private boolean checkTie() {
        return totalPieces == (rows * columns);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[row].length; col++) {
                sb.append(grid[row][col] + ",");
            }
        }

        return sb.toString();
    }

    public void loadFromString(String boardString) {
        System.out.println("Attempting to load: " + boardString);
        boardString = boardString.replaceAll("0", EMPTY_TOKEN + "");
        boardString = boardString.replaceAll("1", "X");
        boardString = boardString.replaceAll("2", "O");

        int currentIndex = 0;
        String[] boardSplit = boardString.split(",");

        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[row].length; col++) {
                grid[row][col] = boardSplit[currentIndex].charAt(0);
                currentIndex++;
            }
        }
    }

    @Override
    public void toDatabase(Database database) {
        database.add("BOARD_ROWS", rows);
        database.add("BOARD_COLUMNS", columns);
        database.add("BOARD_GRID", toString());
    }

    @Override
    public boolean fromDatabase(Database database) {
        /*
         * DatabaseItem rowItem = items.get("BOARD_ROWS");
         * if(rowItem == null || rowItem.getItemType() != ItemType.INTEGER)
         * return false;
         * int rowCount = (Integer)rowItem.getValue();
         * 
         * DatabaseItem columnItem = items.get("BOARD_COLUMNS");
         * if(columnItem == null || columnItem.getItemType() !=
         * ItemType.INTEGER)
         * return false;
         * int colCount = (Integer)rowItem.getValue();
         * 
         * DatabaseItem gridItem = items.get("BOARD_GRID");
         * if(gridItem == null || gridItem.getItemType() != ItemType.STRING)
         * return false;
         * String[] gridRows = gridItem.getValue().toString().split("~");
         * 
         * restart(rows, columns);
         * 
         * for (int i = 0; i < rowCount; i++){
         * String[] currentRow = gridRows[i].split("|");
         * 
         * if(currentRow.length != colCount)
         * return false;
         * 
         * for(int col = 0; col < colCount; col++)
         * grid[i - 1][col] = currentRow[i].charAt(0);
         * }
         * 
         * return true;
         */
        return true;
    }
}
