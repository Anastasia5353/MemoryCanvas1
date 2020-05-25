package com.example.memorycanvas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.sax.StartElementListener;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    TilesView view;
    Boolean start;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        view = findViewById(R.id.view);
        button = findViewById(R.id.button);
        start = true;
    }

    public void onNewGameClick(View v) {
        view.newGame();
        button.setText("Играть заново");

    }// запустить игру заново
}



