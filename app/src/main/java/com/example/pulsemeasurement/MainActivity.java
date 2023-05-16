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
    private TextView tv_heartRate, tv_SpO2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dataSensor = FirebaseDatabase.getInstance().getReference();
        tv_heartRate = (TextView) findViewById(R.id.tv_HeartRateShow);
        tv_SpO2 = (TextView) findViewById(R.id.tv_SpO2Show);
        dataSensor.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                double heartRate = snapshot.child("Heart").getValue(Double.class);
                double SpO2 = snapshot.child("SpO2").getValue(Double.class);
                tv_heartRate.setText(String.valueOf(heartRate));
                tv_SpO2.setText(String.valueOf(SpO2));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}