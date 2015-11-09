package com.threshold.tts;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.threshold.tts.speech.TTSSpeech;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TTSSpeech ttsSpeech;
    private EditText editText;
    private Button btnSpeak;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bindingViews();
        initFields();
        initEvents();
    }

    private void initEvents() {
        btnSpeak.setOnClickListener(this);
    }

    private void initFields() {
        ttsSpeech = TTSSpeech.newInstance(this);
    }

    private void bindingViews() {
        editText = (EditText) findViewById(R.id.et_speak_content);
        btnSpeak = (Button) findViewById(R.id.btn_speak);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_speak:
                ttsSpeech.speak(editText.getText().toString());
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (ttsSpeech!=null) {
            ttsSpeech.stop();
            ttsSpeech.shutdown();
        }
    }
}
