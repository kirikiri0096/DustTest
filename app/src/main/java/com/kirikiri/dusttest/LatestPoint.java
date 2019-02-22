package com.kirikiri.dusttest;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class LatestPoint extends AppCompatActivity {

    private static final String TAG = "GetLastestData";
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private String[] path;
    private String[][] deviceData;
    private String[][] pmData;
    private TextView latestTxt;
    private String[] strData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_latest_point);
        overridePendingTransition(0, 0);

        latestTxt = findViewById(R.id.latestTxt);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    int i = 0;
                    strData = new String[(int) dataSnapshot.getChildrenCount()];
                    deviceData = new String[(int) dataSnapshot.getChildrenCount()][5];
                    for (DataSnapshot deviceName : dataSnapshot.getChildren()) {
                        deviceData[i][0] = deviceName.getKey();
                        i++;
                    }
                    path = new String[i + 1];
                    pmData = new String[i + 1][3];
                    for (int j = 0; j < i; j++) {
                        queryAll(deviceData[j][0], j);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }

    private void queryAll(String deviceName, final int position) {
        Query latestYear = myRef.child(deviceName).orderByKey().limitToLast(1);
        latestYear.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    deviceData[position][1] = "/" + data.getKey();
                    path[position] = deviceData[0][0] + deviceData[0][1];
                }
                Query latestMonth = myRef.child(path[position]).orderByKey().limitToLast(1);
                latestMonth.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            deviceData[position][2] = "/" + data.getKey();
                            path[position] = deviceData[position][0] + deviceData[position][1] + deviceData[position][2];
                        }
                        Query latestDate = myRef.child(path[position]).orderByKey().limitToLast(1);
                        latestDate.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot data : dataSnapshot.getChildren()) {
                                    deviceData[position][3] = "/" + data.getKey();
                                    path[position] = deviceData[position][0] + deviceData[position][1] + deviceData[position][2] + deviceData[position][3];
                                }
                                Query latestTime = myRef.child(path[position]).orderByKey().limitToLast(1);
                                latestTime.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                                            deviceData[position][4] = "/" + data.getKey();
                                            path[position] = deviceData[position][0] + deviceData[position][1] + deviceData[position][2] + deviceData[position][3] + deviceData[position][4];
                                        }
                                        DatabaseReference localRef = database.getReference(path[position]);
                                        localRef.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                StringBuilder sb = null;
                                                if (dataSnapshot.hasChildren()) {
                                                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                                                        sb = new StringBuilder();
                                                        if (data.getKey().equals("PM01"))
                                                            pmData[position][0] = data.getValue().toString();
                                                        if (data.getKey().equals("PM10"))
                                                            pmData[position][1] = data.getValue().toString();
                                                        if (data.getKey().equals("PM25"))
                                                            pmData[position][2] = data.getValue().toString();
                                                        sb.append("Device: ");
                                                        sb.append(deviceData[position][0]);
                                                        sb.append("; PM01 = ");
                                                        sb.append(pmData[position][0]);
                                                        sb.append(" PM10 = ");
                                                        sb.append(pmData[position][1]);
                                                        sb.append(" PM25 = ");
                                                        sb.append(pmData[position][2]);
                                                        sb.append("\n");
                                                    }
                                                    strData[position] = sb.toString();
                                                }
                                                if(sb!=null) {
                                                    String a = "";
                                                    for(String data: strData) {
                                                        a += data;
                                                    }
                                                    latestTxt.setText(a);
                                                }

                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        //Handle possible errors.
                                    }
                                });
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                //Handle possible errors.
                            }
                        });
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //Handle possible errors.
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //Handle possible errors.
            }
        });
    }

    private int countChar(@Nullable String str, char c) {
        if (str == null)
            return 0;
        int count = 0;

        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == c)
                count++;
        }

        return count;
    }

}
