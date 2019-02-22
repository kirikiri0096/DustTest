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

        //Initialize database
        database = FirebaseDatabase.getInstance();

        //get path from previous intent if exist
        Intent intent = getIntent();
        getPath = intent.getStringExtra("path");
        //If path is not available then fetch all data
        //else if path is available then fetch that value
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
                //Handle if this path have data
                if(dataSnapshot.getChildrenCount() != 0) {

                    //Create new array of child name
                    int i = 0;
                    arrData = new String[((int) dataSnapshot.getChildrenCount())];
                    for(DataSnapshot data: dataSnapshot.getChildren()) {
                        arrData[i] = data.getKey();
                        i++;
                    }

                    //if path have / less or more than 4 times then add onClick action
                    if(countChar(getPath, '/') != 4) {
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                String itemValue = (String) listView.getItemAtPosition(position);
                                Intent nextPage = new Intent(ListAll.this, ListAll.class);
                                //Send path to next intent
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
                        //if path have / 4 times (Ex. D0000/2019/2/20/1234) that display value of each child
                        int j = 0;
                        for(DataSnapshot data: dataSnapshot.getChildren()) {
                            if(data.getValue() != null)
                                arrData[j] = arrData[j] + ": " + data.getValue().toString();
                            j++;
                        }
                    }
                }

                //Set adapter for ListView if array is not empty
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

    //Function for counting character in String
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
