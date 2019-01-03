package com.shaen.assistant;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ToggleButton led,ultrasound;

    DatabaseReference dbled,dbmotor,dbservo,dbai,dbultrasound,dbDISTANCE;
    FirebaseDatabase database;
    RadioGroup motor,servo;
    TextView ttt;
    Button Rbtn,Lbtn,ai;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Rbtn=findViewById(R.id.rightbtn);
        Lbtn=findViewById(R.id.leftbtn);
        ttt=findViewById(R.id.ttt);
        led=findViewById(R.id.led);
        ai=findViewById(R.id.ai);
        motor=findViewById(R.id.motor);
        servo=findViewById(R.id.servo);
        ultrasound=findViewById(R.id.ultrasound);
        database = FirebaseDatabase.getInstance();
        dbled = database.getReference("dbLED");
        dbai = database.getReference("dbAI");
        dbmotor = database.getReference("dbMOTOR");
        dbservo = database.getReference("dbSERVO");
        dbultrasound =database.getReference("dbULTRASOUND");
        dbDISTANCE=database.getReference("dbDISTANCE");

       ai.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               startActivity(new Intent(MainActivity.this,Main2Activity.class));
           }});

        led.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                dbled.setValue(isChecked+"");
            }});

        Rbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RightAsyncTask rightAsyncTask = new RightAsyncTask();
                rightAsyncTask.execute(1);

            }});
        Lbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LeftAsyncTask leftAsyncTask = new LeftAsyncTask();
                leftAsyncTask.execute(1);


            }});


        motor.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId){
                case R.id.go_motor:
                    dbmotor.setValue("go");
                    break;
                case R.id.stop_motor:
                    dbmotor.setValue("stop");
                    break;
                case R.id.back_motor:
                    dbmotor.setValue("back");
                    break;

            }}});
        servo.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.right:
                        dbservo.setValue("right");
                        break;
                    case R.id.front:
                        dbservo.setValue("front");
                        break;
                    case R.id.left:
                        dbservo.setValue("left");
                        break;
            }}});
        ultrasound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                dbultrasound.setValue(isChecked+"");
            }
        });
        dbDISTANCE.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                ttt.setText(value);
                int aaa =Integer.valueOf(value);
                Log.d("aaaaaaaaaaaaaaaaaaaaa",aaa+"");
                if(50>aaa){
                    dbmotor.setValue("stop");
                    Log.d("aaaaaaaaaaaaaaaaaaaaa","stop");
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void onResume() {
        super.onResume();
    }



    public  class RightAsyncTask extends AsyncTask<Integer, Integer, String>
    {
        @Override
        protected String doInBackground(Integer... integers) {

            dbmotor.setValue("right");
            int n = integers[0];
            int i;
            for(i=n;i>=0;i--)
            {
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }}
            return "OK";
        }
        @Override
        protected void onProgressUpdate(Integer... values){
            super.onProgressUpdate(values);
        }
        @Override
        protected void onPostExecute(String s){
            super.onPostExecute(s);
            dbmotor.setValue("stop");
        }
        @Override
        protected void onCancelled(String s) {
            super.onCancelled(s);
        }
        @Override
        protected void onCancelled() {
            super.onCancelled();
        }}


    public class LeftAsyncTask extends AsyncTask<Integer, Integer, String>
    {
        @Override
        protected String doInBackground(Integer... integers) {

            dbmotor.setValue("left");
            int n = integers[0];
            int i;
            for(i=n;i>=0;i--)
            {
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }}
            return "OK";
        }
        @Override
        protected void onProgressUpdate(Integer... values){
            super.onProgressUpdate(values);
        }
        @Override
        protected void onPostExecute(String s){
            super.onPostExecute(s);
            dbmotor.setValue("stop");
        }
        @Override
        protected void onCancelled(String s) {
            super.onCancelled(s);
        }
        @Override
        protected void onCancelled() {
            super.onCancelled();
        }}
}
