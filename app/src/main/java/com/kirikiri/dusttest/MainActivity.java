package com.kirikiri.dusttest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView MainMenu;
    private RadioButton readALLDB, queryRa;
    private Button startDebugButt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MainMenu = findViewById(R.id.MainMenu);
        readALLDB = findViewById(R.id.readALLDB);
        queryRa = findViewById(R.id.queryRa);
        startDebugButt = findViewById(R.id.startDebugButt);

        startDebugButt.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        Intent intent = null;
        if(readALLDB.isChecked())
            intent = new Intent(this, AllDB.class);
        if(queryRa.isChecked())
            intent = new Intent(this, QueryDB.class);

        switch (v.getId()) {
            case R.id.startDebugButt:
                if (intent != null) {
                    startActivity(intent);
                }
                break;
        }
    }
}
