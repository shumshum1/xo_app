package com.example.xoapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
private EditText ETname1, ETname2;
private Button BTNnext;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ETname1 = findViewById(R.id.player1_name);
        ETname2 = findViewById(R.id.player2_name);
        BTNnext = findViewById(R.id.next_screen_button);
        BTNnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name1 = ETname1.getText().toString();
                String name2 = ETname2.getText().toString();
                Intent intent = new Intent(MainActivity.this,MainGame.class);
                intent.putExtra("NAME1",name1);
                intent.putExtra("NAME2",name2);
                startActivity(intent);
            }
        });
    }
}