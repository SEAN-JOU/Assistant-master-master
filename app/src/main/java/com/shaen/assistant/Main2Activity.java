package com.shaen.assistant;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.AsyncTask;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;



public class Main2Activity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    private TextView textInput, second;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    private final int MY_DATA_CHECK_CODE = 150;
    private static final int REQ_TTS_STATUS_CHECK = 1;
    MyAsyncTask task;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference dbled,dbmotor,dbservo,dbai,dbultrasound,dbDISTANCE;
    private TextToSpeech mTts;
    HashMap<String, String> hashMap = new HashMap<>();
    TextView ttt;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        mTts = new TextToSpeech(this, this);
        textInput = (TextView) findViewById(R.id.txtSpeechInput);
        second = (TextView) findViewById(R.id.second);
        ttt=findViewById(R.id.value);
        reference = FirebaseDatabase.getInstance().getReference().child("Talk");
        dbled = database.getReference("dbLED");
        dbai = database.getReference("dbAI");
        dbmotor = database.getReference("dbMOTOR");
        dbservo = database.getReference("dbSERVO");
        dbultrasound =database.getReference("dbULTRASOUND");
        dbDISTANCE=database.getReference("dbDISTANCE");


        second.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Main2Activity.this.finish();
                Intent it = new Intent(Main2Activity.this, Main2Activity.class);
                startActivity(it);
                return false;
            }
        });


    }

//        hashMap.put("帥","summer");
//        hashMap.put("美","新垣結依");


    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = mTts.setLanguage(Locale.CHINESE);
            if (result == TextToSpeech.LANG_NOT_SUPPORTED || result == TextToSpeech.LANG_MISSING_DATA) {
            } else {
            }
        }
    }

    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.CHINESE);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, R.string.speech_prompt);
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    R.string.speech_not_supported,
                    Toast.LENGTH_SHORT).show();
        }
    }


    public void onResume() {
        super.onResume();
        hashMap.clear();

        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                append_chat(dataSnapshot);

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                append_chat(dataSnapshot);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                append_chat(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {


            }
        });

        dbDISTANCE.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                ttt.setText(value);
                int aaa =Integer.valueOf(value);
                Log.d("aaaaaaaaaaaaaaaaaaaaa",aaa+"");
                if(10>aaa){
                    dbmotor.setValue("stop");
                    Log.d("aaaaaaaaaaaaaaaaaaaaa","stop");
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        task = new MyAsyncTask();
        task.execute(3);
        new Thread(new Runnable() {
            @Override
            public void run() {


            }}).start();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    textInput.setText(String.valueOf(result.get(0)).replaceAll("\\s", ""));

                    if (String.valueOf(result.get(0)).contains("打開LED"))
                    {
                        dbled.setValue(true+"");
                        return;
                    }
                    if (String.valueOf(result.get(0)).contains("關閉LED"))
                    {
                        dbled.setValue(false+"");
                        return;
                    }
                    if (String.valueOf(result.get(0)).contains("前進"))
                    {
                        dbmotor.setValue("go");
                        return;
                    }
                    if (String.valueOf(result.get(0)).contains("後退"))
                    {
                        dbmotor.setValue("back");
                        return;
                    }
                    if (String.valueOf(result.get(0)).contains("左轉"))
                    {
                        LeftAsyncTask leftAsyncTask = new LeftAsyncTask();
                        leftAsyncTask.execute(1);
                        return;
                    }
                    if (String.valueOf(result.get(0)).contains("右轉"))
                    {
                        RightAsyncTask rightAsyncTask = new RightAsyncTask();
                        rightAsyncTask.execute(1);
                        return;
                    }
                    if(String.valueOf(result.get(0)).contains("向前")){
                        dbservo.setValue("front");
                        return;
                    }
                    if(String.valueOf(result.get(0)).contains("向右邊")){
                        dbservo.setValue("right");
                        return;
                    }
                    if(String.valueOf(result.get(0)).contains("向左邊")){
                        dbservo.setValue("left");
                        return;
                    }
                    if (String.valueOf(result.get(0)).contains("停止"))
                    {
                        dbmotor.setValue("stop");
                        return;
                    }
                    if (String.valueOf(result.get(0)).contains("開啟感應器"))
                    {
                        dbultrasound.setValue(true+"");
                        return;
                    }
                    if (String.valueOf(result.get(0)).contains("關閉感應器"))
                    {
                        dbultrasound.setValue(false+"");
                        return;
                    }
                    if (result.get(0) != "" || result.get(0).trim() != "") {
                        mTts.speak(Answer.getIntence().sentence(String.valueOf(result.get(0)), hashMap), TextToSpeech.QUEUE_ADD, null);
                    }
                }
            }
            break;
            case MY_DATA_CHECK_CODE: {
                if (resultCode != TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
                    Intent installIntent = new Intent();
                    installIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                    Log.v("Main", "need intallation");
                    startActivity(installIntent);
                }

            }
            case REQ_TTS_STATUS_CHECK: {
                switch (resultCode) {
                    case TextToSpeech.Engine.CHECK_VOICE_DATA_PASS:

                        break;
                    case TextToSpeech.Engine.CHECK_VOICE_DATA_BAD_DATA:
                        //文件已经损坏
                    case TextToSpeech.Engine.CHECK_VOICE_DATA_MISSING_VOLUME:
                        //缺少发音文件
                    case TextToSpeech.Engine.CHECK_VOICE_DATA_MISSING_DATA:
                        //数据文件丢失

                        //从新更新TTS数据文件
                        Intent mUpdateData = new Intent();
                        mUpdateData.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                        startActivity(mUpdateData);

                        break;

                    case TextToSpeech.Engine.CHECK_VOICE_DATA_FAIL:
                        //检测失败应该重新检测
                        break;
                    default:
                        break;
                }
            }
        }
    }

    public class MyAsyncTask extends AsyncTask<Integer, Integer, String> {
        @Override
        protected String doInBackground(Integer... integers) {

            int n = integers[0];
            int i;
            for (i = n; i >= 0; i--) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                final int finalI = i;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        second.setText(String.valueOf(finalI));
                    }
                });
            }
            return "OK";
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            promptSpeechInput();
        }

        @Override
        protected void onCancelled(String s) {
            super.onCancelled(s);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mTts!=null){
            mTts.shutdown();
        }
        this.finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mTts!=null){
            mTts.shutdown();
        }
        this.finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        this.finish();
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

    public void append_chat(DataSnapshot ss) {
        String question, anwser;
        Iterator i = ss.getChildren().iterator();
        while (i.hasNext()) {
            question = ((DataSnapshot) i.next()).getValue().toString();
            anwser = ((DataSnapshot) i.next()).getValue().toString();
            hashMap.put(question, anwser);
        }
    }
}
