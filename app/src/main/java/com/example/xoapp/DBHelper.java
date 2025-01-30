package com.example.xoapp;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "game_summary.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "game_summary";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_PLAYER1 = "player1";
    private static final String COLUMN_PLAYER2 = "player2";
    private static final String COLUMN_WINNER = "winner";
    private static final String COLUMN_TIME = "time";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_PLAYER1 + " TEXT, "
                + COLUMN_PLAYER2 + " TEXT, "
                + COLUMN_WINNER + " TEXT, "
                + COLUMN_TIME + " TEXT);";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // Add a GameSummary to the database
    public void addGameSummary(GameSummary gameSummary) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PLAYER1, gameSummary.getPlayer1());
        values.put(COLUMN_PLAYER2, gameSummary.getPlayer2());
        values.put(COLUMN_WINNER, gameSummary.getWinner());
        values.put(COLUMN_TIME, gameSummary.getTime());
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    // Get all GameSummary objects from the database
    @SuppressLint("Range")
    public List<GameSummary> getAllGameSummaries() {
        List<GameSummary> summaries = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                 String player1 = cursor.getString(cursor.getColumnIndex(COLUMN_PLAYER1));
                String player2 = cursor.getString(cursor.getColumnIndex(COLUMN_PLAYER2));
                String winner = cursor.getString(cursor.getColumnIndex(COLUMN_WINNER));
                String time = cursor.getString(cursor.getColumnIndex(COLUMN_TIME));

                GameSummary summary = new GameSummary(player1, player2);
                summary.setWinner(winner);
                summary.setTime(time);
                summaries.add(summary);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return summaries;
    }

    // Get the count of GameSummary objects in the database
    public int getGameSummaryCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_NAME, null);
        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        db.close();
        return count;
    }

    // Delete a GameSummary by its ID
    public void deleteGameSummary(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }
}
