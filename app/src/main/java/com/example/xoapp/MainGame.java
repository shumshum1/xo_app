package com.example.xoapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainGame extends AppCompatActivity {
    private TextView player1NameTextView;
    private TextView player1TimeTextView;
    private TextView player2NameTextView;
    private TextView player2TimeTextView;
    private TextView winnerTextTextView;
    private ImageView winnerImageView;
    private ImageButton[][] buttons; // Two-dimensional array for the buttons
    private int[][] board = new int[3][3];
    private int player1 = 1;
    private int player2 = 2;
    private int counter = 0;
    private GameSummaryDatabaseHelper gameSummaryDatabaseHelper;
    private GameSummary gameSummary;
    private DBHelper dbHelper;
    private Button btNext;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_game);
        btNext = findViewById(R.id.btNext);
        btNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainGame.this,SummaryActivity.class);
                startActivity(intent);
            }
        });
        gameSummaryDatabaseHelper = new GameSummaryDatabaseHelper(this);
        dbHelper = new DBHelper(this);
        zeroArr(board);
        Intent intent = getIntent();
        String name1 = intent.getStringExtra("NAME1");
        String name2 = intent.getStringExtra("NAME2");
        gameSummary = new GameSummary(name1,name2);
        // Initialize the views
        player1NameTextView = findViewById(R.id.player1_name);
        player1NameTextView.setText(name1);
        player1TimeTextView = findViewById(R.id.player1_time);

        player2NameTextView = findViewById(R.id.player2_name);
        player2NameTextView.setText(name2);
        player2TimeTextView = findViewById(R.id.player2_time);
        winnerTextTextView = findViewById(R.id.winner_text);
        winnerImageView = findViewById(R.id.winner_image);
        // Initialize the two-dimensional array of ImageButtons
        buttons = new ImageButton[3][3];

        // Map the buttons from the layout to the array
        buttons[0][0] = findViewById(R.id.button_00);
        buttons[0][1] = findViewById(R.id.button_01);
        buttons[0][2] = findViewById(R.id.button_02);
        buttons[1][0] = findViewById(R.id.button_10);
        buttons[1][1] = findViewById(R.id.button_11);
        buttons[1][2] = findViewById(R.id.button_12);
        buttons[2][0] = findViewById(R.id.button_20);
        buttons[2][1] = findViewById(R.id.button_21);
        buttons[2][2] = findViewById(R.id.button_22);
        startUpdatingTime(player1TimeTextView,true);
        for (int i=0;i<3;i++){
            for (int j=0; j<3;j++){
                int finalI = i;
                int finalJ = j;
                buttons[i][j].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                    counter++;
                    if (counter%2==0){
                        board[finalI][finalJ]=1;
                        startUpdatingTime(player1TimeTextView,false);
                        startUpdatingTime(player2TimeTextView,true);
                        if (checkLine(board)){
                            winnerTextTextView.setText("winner is "+ name1);
                            gameSummary.setWinner(name1);
                            gameSummary.setTime(player1TimeTextView.getText().toString());
                            dbHelper.addGameSummary(gameSummary);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    recreate(); // קורא ל-recreate לאחר 5 שניות
                                }
                            }, 5000); // דיליי של 5 שניות (5000 מילישניות)

                        }
                    }
                    else {

                        board[finalI][finalJ]=2;
                        startUpdatingTime(player1TimeTextView,true);
                        startUpdatingTime(player2TimeTextView,false);
                        if (checkLine(board)){
                            winnerTextTextView.setText("winner is "+ name2);
                            gameSummary.setWinner(name2);
                            gameSummary.setTime(player1TimeTextView.getText().toString());
                            dbHelper.addGameSummary(gameSummary);
                            gameSummaryDatabaseHelper.addGameSummary(gameSummary);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    recreate(); // קורא ל-recreate לאחר 5 שניות
                                }
                            }, 5000); // דיליי של 5 שניות (5000 מילישניות)

                        }
                    }

                    setScreen(buttons,board);
                    if (counter==9){
                        winnerTextTextView.setText("draw");
                        gameSummary.setWinner("draw");
                        gameSummary.setTime("00:00");
                        dbHelper.addGameSummary(gameSummary);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                recreate(); // קורא ל-recreate לאחר 5 שניות
                            }
                        }, 5000); // דיליי של 5 שניות (5000 מילישניות)

                    }
                    }
                });
            }
        }
    }
    public static boolean checkLine(int [][] board){
        for (int i =0; i<3; i++){
            if ((board[i][0]==board[i][1])&&(board[i][0]==board[i][2])&&(board[i][0]!=0)){
                return true;
            }
            if ((board[0][i]==board[1][i])&&(board[0][i]==board[2][i])&&(board[0][i]!=0)){
                return true;
            }
        }
        if (board[0][0] == board[1][1] && board[1][1] == board[2][2] && board[1][1] != 0)
            return true;
        if (board[0][2] == board[1][1] && board[1][1] == board[2][0] && board[1][1] != 0) return true;
        return false;
    }
    public void setScreen(ImageButton[][] buttons,int [][] board){
        for (int i=0;i<3;i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == 1)
                    buttons[i][j].setImageDrawable(getResources().getDrawable(R.drawable.baseline_handyman_24, getTheme()));
                if (board[i][j] == 2)
                    buttons[i][j].setImageDrawable(getResources().getDrawable(R.drawable.baseline_circle_24, getTheme()));
                if (board[i][j] == 0)
                    buttons[i][j].setImageDrawable(null);
            }
        }
            }
    private Handler handler = new Handler();
    private Runnable runnable;
    private boolean isRunning = false; // Boolean to control whether the timer is running
    public void startUpdatingTime(TextView textView, boolean shouldStart) {
        // Parse the initial time in the format "mm:ss"
        String initialTime = textView.getText().toString();
        String[] timeParts = initialTime.split(":");
        int minutes = Integer.parseInt(timeParts[0]);
        int seconds = Integer.parseInt(timeParts[1]);

        final int[] totalSeconds = {minutes * 60 + seconds};

        if (shouldStart) {
            if (!isRunning) {
                isRunning = true;

                // Runnable to update the time every second
                runnable = new Runnable() {
                    @Override
                    public void run() {
                        if (isRunning) {
                            totalSeconds[0]++;
                            int updatedMinutes = totalSeconds[0] / 60;
                            int updatedSeconds = totalSeconds[0] % 60;

                            // Format the time as "mm:ss"
                            String updatedTime = String.format("%02d:%02d", updatedMinutes, updatedSeconds);

                            // Update the TextView
                            textView.setText(updatedTime);

                            // Schedule the next update
                            handler.postDelayed(this, 1000);
                        }
                    }
                };

                // Start the Runnable
                handler.post(runnable);
            }
        } else {
            // Stop the timer
            isRunning = false;
            handler.removeCallbacks(runnable);
        }
    }
    public void zeroArr (int[][] arr) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                arr[i][j] = 0;
            }
        }
    }
}