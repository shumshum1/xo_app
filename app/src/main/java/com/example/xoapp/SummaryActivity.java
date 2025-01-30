package com.example.xoapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class SummaryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private GameSummaryAdapter adapter;
    private List<GameSummary> gameSummaryList;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        recyclerView = findViewById(R.id.items);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        DBHelper dbHelper = new DBHelper(this);

        gameSummaryList = new ArrayList<>();
        gameSummaryList = dbHelper.getAllGameSummaries();

        adapter = new GameSummaryAdapter(gameSummaryList);
        recyclerView.setAdapter(adapter);
    }
}
