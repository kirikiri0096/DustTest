package com.kirikiri.dusttest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class QueryDB extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "QueryDB";
    private EditText queryInp;
    private Button querySendBtn;
    private TextView queryTxt;
    private FirebaseDatabase database;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query_db);

        queryInp = findViewById(R.id.queryInp);
        querySendBtn = findViewById(R.id.querySendBtn);
        queryTxt = findViewById(R.id.queryTxt);

        querySendBtn.setOnClickListener(this);

        database = FirebaseDatabase.getInstance();

    }

    @Override
    public void onClick(View v) {
        if(!queryInp.getText().toString().isEmpty()) {
            myRef = database.getReference(queryInp.getText().toString());

        }
        else {
            myRef = database.getReference();
        }

        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() != null)
                    queryTxt.setText(dataSnapshot.getValue().toString());
                else
                    queryTxt.setText(getString(R.string.noValueKey) + queryInp.getText().toString());
                Log.d(TAG, "Data is: " + dataSnapshot.toString());
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                queryTxt.setText(error.toException().toString());
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }
}
