/**
 * Class: CS 3331, Advanced Object Oriented Programming
 * Instructor: Omar Ochoa
 * Author: Jose G Perez (UTEP ID: 80473954)
 * Contact: <josegperez@mail.com> or <jperez50@miners.utep.edu>
 * Last Date Modified: 5/1/2016
 */
package edu.utep.cs3350.connect4.perezJose.ui.component;

import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;

class BoardView extends Canvas {
    private static final double CELL_WIDTH = 55;
    private static final double CELL_HEIGHT = 55;

    private BoardViewSubscriber boardSubscriber;
    private int gameColumns;
    private int gameRows;

    private Image imageX;
    private Image imageO;
    private Image imageEmpty;

    public BoardView(BoardViewSubscriber boardSubscriber, int gameRows, int gameColumns) {
        super();
        this.gameRows = gameRows;
        this.gameColumns = gameColumns;
        this.boardSubscriber = boardSubscriber;

        imageX = new Image("file:x.png", CELL_WIDTH, CELL_HEIGHT, false, false);
        imageO = new Image("file:o.png", CELL_WIDTH, CELL_HEIGHT, false, false);
        imageEmpty = new Image("file:empty.png", CELL_WIDTH, CELL_HEIGHT, false, false);

        setWidth(gameColumns * CELL_WIDTH);
        setHeight(gameRows * CELL_HEIGHT);

        for (int row = 0; row < gameRows; row++)
            for (int column = 0; column < gameColumns; column++)
                drawImage(imageEmpty, row * CELL_WIDTH, column * CELL_HEIGHT);

        addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                onMouseClicked(event);
            }

        });
    }

    private void onMouseClicked(MouseEvent event) {
        int column = Math.floorDiv((int) event.getX(), (int) CELL_WIDTH);
        boardSubscriber.onColumnSelected(column);
    }

    public void updateBoard() {
        getGraphicsContext2D().clearRect(0, 0, getWidth(), getHeight());
        for (int row = 0; row < gameRows; row++) {
            for (int column = 0; column < gameColumns; column++) {
                char currentToken = boardSubscriber.tokenAt(row, column);

                if (currentToken == 'O')
                    drawImage(imageO, column * CELL_HEIGHT, row * CELL_WIDTH);
                else if (currentToken == 'X')
                    drawImage(imageX, column * CELL_HEIGHT, row * CELL_WIDTH);
                else
                    drawImage(imageEmpty, column * CELL_HEIGHT, row * CELL_WIDTH);
            }
        }
    }

    private void drawImage(Image image, double x, double y) {
        getGraphicsContext2D().drawImage(image, x, y);
    }
}
