/**
 * Class: CS 3331, Advanced Object Oriented Programming
 * Instructor: Omar Ochoa
 * Author: Jose G Perez (UTEP ID: 80473954)
 * Contact: <josegperez@mail.com> or <jperez50@miners.utep.edu>
 * Last Date Modified: 5/1/2016
 */
package edu.utep.cs3350.connect4.perezJose.ui.component;

import edu.utep.cs3350.connect4.perezJose.database.Database;
import edu.utep.cs3350.connect4.perezJose.database.Saveable;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class TimerComponent implements Runnable, Saveable {
    private static final Color CURRENT_TIME_COLOR_STROKE = Color.CORNFLOWERBLUE;
    private static final Color CURRENT_TIME_COLOR_FILL = Color.BLACK;
    private static final int CURRENT_TIME_FONT_SIZE = 12;

    private TimerSubscriber timerSubscriber;

    private boolean timerRunning;

    private long lastRecordedTime;

    private int minutes;
    private int seconds;

    private Text currentTime;

    public synchronized Text getCurrentTime() {
        return currentTime;
    }

    private Thread timerThread;

    public TimerComponent(TimerSubscriber timerSubscriber) {
        this.timerSubscriber = timerSubscriber;
        currentTime = new Text();

        currentTime.setFont(Font.font(CURRENT_TIME_FONT_SIZE));
        currentTime.setStroke(CURRENT_TIME_COLOR_STROKE);
        currentTime.setFill(CURRENT_TIME_COLOR_FILL);

        restartTimer();

        lastRecordedTime = System.currentTimeMillis();

        timerThread = new Thread(this);
        timerThread.setDaemon(true);
        timerThread.start();
    }

    public void stopTimer() {
        timerRunning = false;
    }

    public void startTimer() {
        timerRunning = true;
    }

    public void restartTimer() {
        timerRunning = true;
        seconds = 0;
        minutes = 0;
        currentTime.setText("00:00");
    }

    @Override
    public void run() {
        while (timerThread.isAlive()) {
            if (timerRunning) {
                long deltaTime = System.currentTimeMillis() - lastRecordedTime;

                if (deltaTime >= 1000) {
                    seconds++;
                    timerSubscriber.onElapsedTime(minutes, seconds);
                    lastRecordedTime = System.currentTimeMillis();
                }

                if (seconds > 60) {
                    seconds = 0;
                    minutes++;
                    timerSubscriber.onElapsedTime(minutes, seconds);
                }

                currentTime.setText(String.format("%02d:%02d", minutes, seconds));
            }
        }
    }

    @Override
    public void toDatabase(Database database) {
        database.add("PS_TIME", getCurrentTime().getText());
    }

    @Override
    public boolean fromDatabase(Database database) {
        return true;
    }
}