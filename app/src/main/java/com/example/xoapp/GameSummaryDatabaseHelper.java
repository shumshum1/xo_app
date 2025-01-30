package com.example.xoapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class GameSummaryDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "games.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "game_summary";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_PLAYER1 = "player1";
    private static final String COLUMN_PLAYER2 = "player2";
    private static final String COLUMN_WINNER = "winner";
    private static final String COLUMN_TIME = "time";

    public GameSummaryDatabaseHelper(Context context) {
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

    public long addGameSummary(GameSummary gameSummary) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PLAYER1, gameSummary.getPlayer1());
        values.put(COLUMN_PLAYER2, gameSummary.getPlayer2());
        values.put(COLUMN_WINNER, gameSummary.getWinner());
        values.put(COLUMN_TIME, gameSummary.getTime());

        long result = db.insert(TABLE_NAME, null, values);
        db.close();
        return result;
    }

    public List<GameSummary> getAllGameSummaries() {
        List<GameSummary> gameSummaries = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                String player1 = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PLAYER1));
                String player2 = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PLAYER2));
                String winner = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_WINNER));
                String time = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TIME));

                GameSummary gameSummary = new GameSummary(winner, time, player1, player2);
                gameSummaries.add(gameSummary);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return gameSummaries;
    }

    public int deleteGameSummary(String winner) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsDeleted = db.delete(TABLE_NAME, COLUMN_WINNER + " = ?", new String[]{winner});
        db.close();
        return rowsDeleted;
    }

    public int getRowCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT COUNT(*) FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        db.close();
        return count;
    }
}
