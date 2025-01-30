package com.example.xoapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    private EditText ETname1, ETname2;
    private Button BTNnext, BTNrealTime;
    int num;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("games");
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // כתיבה לדאטאבייס של Firebase

        int num = new Random().nextInt(100000000);
        myRef.child("id" + num).setValue(num);

        ETname1 = findViewById(R.id.player1_name);
        ETname2 = findViewById(R.id.player2_name);
        BTNnext = findViewById(R.id.next_screen_button);
        BTNrealTime = findViewById(R.id.btRealTimegame); // אתחול נכון של הכפתור

        // מעבר למסך משחק עם שמות שחקנים
        BTNnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name1 = ETname1.getText().toString();
                String name2 = ETname2.getText().toString();
                Intent intent = new Intent(MainActivity.this, MainGame.class);
                intent.putExtra("NAME1", name1);
                intent.putExtra("NAME2", name2);
                startActivity(intent);
            }
        });

        // הצגת הדיאלוג לבחירת משחק
        BTNrealTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showGameChoiceDialog();
            }
        });
    }

    private void showGameChoiceDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("בחירת משחק");

        builder.setMessage("האם אתה מצטרף למשחק או יוזם משחק?");
        builder.setPositiveButton("יוזם משחק", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                showGameInitiationDialog();
            }
        });
        builder.setNegativeButton("מצטרף למשחק", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                showJoinGameDialog();
            }
        });

        builder.show();
    }

    private void showGameInitiationDialog() {
        num = new Random().nextInt(100000000);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("יוזם משחק");
        builder.setMessage("ID של המשחק: " + num);
        builder.setPositiveButton("אישור", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MainActivity.this, "משחק נוצר עם ID: " + num, Toast.LENGTH_LONG).show();
                myRef.child(""+ num).child("player1").setValue(ETname1.getText().toString());
            }
        });

        builder.show();
    }

    private void showJoinGameDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("הצטרפות למשחק");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setHint("הכנס ID של המשחק");
        builder.setView(input);

        builder.setPositiveButton("אישור", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String gameId = input.getText().toString().trim();
                if (!gameId.isEmpty()) {
                    Toast.makeText(MainActivity.this, "הצטרפת למשחק עם ID: " + gameId, Toast.LENGTH_LONG).show();
                    searchForKey(gameId);
                } else {
                    Toast.makeText(MainActivity.this, "ID אינו יכול להיות ריק", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("ביטול", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void searchForKey(String key) {
        myRef.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // המפתח נמצא
                    String player2Name = ETname2.getText().toString().trim();
                    if (!player2Name.isEmpty()) {
                        // עדכון או יצירת player2 תחת key
                        myRef.child(key).child("player2").setValue(player2Name)
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(MainActivity.this, "שחקן 2 נוסף!", Toast.LENGTH_SHORT).show();
                                    Log.d("Firebase", "שחקן 2 נוסף: " + player2Name);
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(MainActivity.this, "שגיאה בעדכון: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    Log.e("Firebase", "שגיאה בעדכון: ", e);
                                });
                    } else {
                            Toast.makeText(MainActivity.this, "שם שחקן 2 ריק!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // המפתח לא נמצא
                    Toast.makeText(MainActivity.this, "המפתח לא נמצא!", Toast.LENGTH_SHORT).show();
                    Log.d("Firebase", "המפתח לא נמצא!");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, "שגיאה בחיפוש: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("Firebase", "שגיאה: " + databaseError.getMessage());
            }
        });
    }

}
