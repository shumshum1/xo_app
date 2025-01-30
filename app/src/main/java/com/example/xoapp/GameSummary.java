package com.example.xoapp;

public class GameSummary {
    public GameSummary(String player1, String player2) {
        this.player1 = player1;
        this.player2 = player2;
    }

    public GameSummary(String winner, String time, String player1, String player2) {
        this.winner = winner;
        this.time = time;
        this.player1 = player1;
        this.player2 = player2;
    }

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPlayer1() {
        return player1;
    }

    public void setPlayer1(String player1) {
        this.player1 = player1;
    }

    public String getPlayer2() {
        return player2;
    }

    public void setPlayer2(String player2) {
        this.player2 = player2;
    }

    private String winner;
    private String time;
    private String player1, player2;
}