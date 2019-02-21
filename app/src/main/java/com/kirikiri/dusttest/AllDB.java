package com.kirikiri.dusttest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AllDB extends AppCompatActivity {

    private static final String TAG = "AllDB";
    private TextView dataView;

    @Override
    public void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_db);
        overridePendingTransition(0, 0);

        dataView = findViewById(R.id.dataView);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();

        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dataView.setText(dataSnapshot.getValue().toString());
                Log.d(TAG, "Value is: " + dataSnapshot.toString());
//                String keyV = "";
//                for(DataSnapshot data: dataSnapshot.getChildren()) {
//                    keyV += data.getKey() + ", ";
//                }
//                Log.d(TAG, "Key is: " + keyV);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }
}
