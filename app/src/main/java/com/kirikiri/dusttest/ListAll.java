package com.kirikiri.dusttest;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ListAll extends AppCompatActivity {

    private static final String TAG = "ListAllDB";
    private ListView listView;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private String[] arrData;
    private String getPath = "";

    @Override
    public void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_all);
        overridePendingTransition(0, 0);

        listView = findViewById(R.id.listView);
        database = FirebaseDatabase.getInstance();

        Intent intent = getIntent();
        getPath = intent.getStringExtra("path");

        Log.d(TAG, "getPath: " + getPath);


        if(getPath == null) {
            myRef = database.getReference();
            fetchData();
        }
        else {
            myRef = database.getReference(getPath);
            fetchData();
        }

    }

    private void fetchData() {
        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount() != 0) {
                    int i = 0;
                    arrData = new String[((int) dataSnapshot.getChildrenCount())];

                    for(DataSnapshot data: dataSnapshot.getChildren()) {
                        arrData[i] = data.getKey();
                        i++;
                    }

                    if(countChar(getPath, '/') != 4) {
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                String itemValue = (String) listView.getItemAtPosition(position);
                                Intent nextPage = new Intent(ListAll.this, ListAll.class);
                                if(getPath == null) {
                                    nextPage.putExtra("path", itemValue);
                                }
                                else
                                    nextPage.putExtra("path", getPath + "/" +  itemValue);
                                startActivity(nextPage);
                            }
                        });
                    }
                    else {
                        int j = 0;
                        for(DataSnapshot data: dataSnapshot.getChildren()) {
                            if(data.getValue() != null)
                                arrData[j] = arrData[j] + ": " + data.getValue().toString();
                            j++;
                        }
                    }


                }
                if(arrData!=null) {
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(ListAll.this, android.R.layout.simple_list_item_1, arrData);
                    listView.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
    }

    private int countChar(@Nullable String str, char c)
    {
        if(str == null)
            return 0;
        int count = 0;

        for(int i=0; i < str.length(); i++)
        {    if(str.charAt(i) == c)
            count++;
        }

        return count;
    }
}
