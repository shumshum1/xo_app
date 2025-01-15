package com.example.xoapp;

public class GameSummary {
    private String winner;
    private String time;
    private String player1, player2;

    public GameSummary(String player1, String player2) {
        this.player1 = player1;
        this.player2 = player2;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
