package com.example.pulsemeasurement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private DatabaseReference dataSensor;
    private TextView tv_heartRate, tv_SpO2, tv_beatState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dataSensor = FirebaseDatabase.getInstance().getReference();
        tv_heartRate = (TextView) findViewById(R.id.tv_HeartRateShow);
        tv_SpO2 = (TextView) findViewById(R.id.tv_SpO2Show);
        //tv_beatState = (TextView)findViewById(R.id.tv_beatstateshow);
        dataSensor.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int heartRate = snapshot.child("Heart").getValue(Integer.class);
                int SpO2 = snapshot.child("SpO2").getValue(Integer.class);
                //double beatstate = snapshot.child("BeatState").getValue(Double.class);
                tv_heartRate.setText(String.valueOf(heartRate));
                tv_SpO2.setText(String.valueOf(SpO2) + "%");
                /*if(beatstate == 1){
                    tv_beatState.setText("Bình Thường");
                } else if (beatstate == 0){
                    tv_beatState.setText("Không bình thường");
                }*/
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}