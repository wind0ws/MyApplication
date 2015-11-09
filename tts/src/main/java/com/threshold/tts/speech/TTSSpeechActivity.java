package com.threshold.tts.speech;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;
import android.widget.Toast;


/**
 * Created by Jiang on 2015/6/3.
 */
public abstract class TTSSpeechActivity extends Activity implements TextToSpeech.OnInitListener {

    protected TextToSpeech textToSpeech;
    protected static final int REQ_CHECK_TTS_DATA = 110;
    protected static final String TAG = TTSSpeechActivity.class.getSimpleName();

    protected TTSSpeechActivity(){

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTextToSpeech();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(textToSpeech!=null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQ_CHECK_TTS_DATA){
            switch (resultCode){
                case TextToSpeech.Engine.CHECK_VOICE_DATA_PASS:
                    if(textToSpeech!=null) {
                        textToSpeech.stop();
                        textToSpeech.shutdown();
                    }
                    textToSpeech = new TextToSpeech(this, this);
                    break;
                case TextToSpeech.Engine.CHECK_VOICE_DATA_FAIL:
                    toastMsg("检查TTS数据失败,请安装语音引擎");
                    break;
                default:
                    toastMsg("检查TTS引擎失败，请检查是否安装TTS语音引擎");
                    break;
            }
        }
    }


    protected  void initTextToSpeech(){
        try {
            Intent checkIntent = new Intent();
            checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
            startActivityForResult(checkIntent, REQ_CHECK_TTS_DATA);
            Log.i(TAG, "启动检查TTS Data Activity成功");
//            return true;
        }catch (ActivityNotFoundException e) {
            Log.i(TAG, "启动检查TTS Data Activity失败");
//            return false;
        }
    }

    /** 跳转到“语音输入与输出”设置界面 */
    protected boolean toTtsSettings() {
        try {
            startActivity(new Intent("com.android.settings.TTS_SETTINGS"));
            return true;
        } catch (ActivityNotFoundException e) {
            return false;
        }
    }

    protected void toastMsg(String msg){
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }

    protected class TtsUtteranceListener extends UtteranceProgressListener{

        @Override
        public void onStart(String utteranceId) {

        }

        @Override
        public void onDone(String utteranceId) {

        }

        @Override
        public void onError(String utteranceId) {

        }
    }
}
