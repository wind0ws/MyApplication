package com.threshold.tts.speech;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;
import android.widget.Toast;


import java.util.HashMap;

/**
 * TTS 发音帮助类
 */
public class TTSSpeech  {
    private static TTSSpeech ttsSpeech;
    private Context context;
    private TextToSpeech textToSpeech;
    private boolean isInitSpeechSuccess;
    private static final String TAG = TTSSpeech.class.getSimpleName();
    private UtteranceProgressListener utteranceProgressListener=null;

    /**
     * Denotes a successful operation.
     */
    public static final int SUCCESS = 0;
    /**
     * Denotes a generic operation failure.
     */
    public static final int ERROR = -1;


    protected TTSSpeech(Context context) {
        this.context = context;
        initSpeech(false);
    }

    protected TTSSpeech(Context context,UtteranceProgressListener utteranceProgressListener) {
        this.context = context;
        this.utteranceProgressListener = utteranceProgressListener;
        initSpeech(false);
    }

    protected TTSSpeech(Context context,boolean needReInitSpeech) {
        this.context = context;
        initSpeech(needReInitSpeech);
    }

    protected TTSSpeech(Context context, TextToSpeech textToSpeech) {
        this.context = context;
        this.textToSpeech = textToSpeech;
        initSpeech(false);
    }

    public void initSpeech(boolean needReInitSpeech) {
        isInitSpeechSuccess = false;
        if(needReInitSpeech||textToSpeech==null) {
            if(textToSpeech!=null) {
                textToSpeech.stop();
                textToSpeech.shutdown();
                textToSpeech = null;
            }
            textToSpeech = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int status) {
                    isInitSpeechSuccess = status == TextToSpeech.SUCCESS;
                    if(utteranceProgressListener!=null) {
                        textToSpeech.setOnUtteranceProgressListener(utteranceProgressListener);
                    }
                    Toast.makeText(context,"语音状态："+status,Toast.LENGTH_SHORT).show();
                   /* Log.i(TAG, "初始化TextToSpeech，status：" + status);*/
                }
            });
        }else {
            isInitSpeechSuccess = true;
        }
        ttsSpeech = this;
    }

    public static TTSSpeech newInstance(Context context, boolean needReInitSpeech) {
        return new TTSSpeech(context,needReInitSpeech);
    }

    public static TTSSpeech newInstance(Context context, TextToSpeech textToSpeech){
         return new TTSSpeech(context,textToSpeech);
    }

    public static TTSSpeech newInstance(Context context) {
        return new TTSSpeech(context);
    }

    public static TTSSpeech newInstance(Context context,
                                        UtteranceProgressListener utteranceProgressListener) {
        return new TTSSpeech(context,utteranceProgressListener);
    }

    public TextToSpeech getTextToSpeech() {
        return textToSpeech;
    }

    public  boolean toTTSSettings(){
        try {
            context.startActivity(new Intent("com.android.settings.TTS_SETTINGS"));
            return true;
        }catch (ActivityNotFoundException e) {
            Log.w(TAG, "打开设置中文字转语音设置失败:" + e);
            return false;
        }
    }

    public int speak(String text) {
       return   speak(text, TextToSpeech.QUEUE_FLUSH);
    }

    public int speak(String text,int queueMode){
        if(!isInitSpeechSuccess) {
            return TextToSpeech.ERROR;
        }
//        HashMap<String, String> params = new HashMap<>();
//        params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "标题");
        return textToSpeech.speak(text, queueMode, null);
    }

    /**
     * 如果加了OnUtteranceProgressListener，请在speak时用这个方法，
     * 否则无法监听说话事件
     * @param text 发音内容
     * @param queueMode 发音模式
     *                  1.1）QUEUE_ADD：增加模式。增加在队列尾，继续原来的说话。
     *                  1.2）QUEUE_FLUSH：刷新模式。中断正在进行的说话，说新的内容（推荐）。
     * @param params 发音参数
     *               2.1）请求的参数，可以为null。
     *               2.2）注意KEY_PARAM_UTTERANCE_ID,在上面的接口中的参数utteranceId就是这个ID对应的内容
     *               HashMap<String, String> params = new HashMap<String, String>();
     *               params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, textTitleID);
     * @return 返回是否发音成功{@link #ERROR} or {@link #SUCCESS} of <b>queuing</b> the speak operation.
     */
    public int speak(String text,int queueMode, final HashMap<String, String>  params){
        if(!isInitSpeechSuccess) {
            return TextToSpeech.ERROR;
        }
        return textToSpeech.speak(text, queueMode, params);
    }

    /**
     * 判断是否在朗读。
     * @return true说明在朗读中
     */
    public boolean isSpeaking() {
        return textToSpeech.isSpeaking();
    }

    /**
     * 停止朗读
     * 返回的是状态码
     * 如果textToSpeech是null，也返回ERROR
     * @return TextToSpeech.ERROR错误，SUCCESS成功
     */
    public int stop(){
        if(textToSpeech!=null) {
            return textToSpeech.stop();
        }
        return TextToSpeech.ERROR;
    }

    /**
     * 关闭语音引擎并清理使用的资源
     */
    public void shutdown(){
        if(textToSpeech!=null){
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
    }




}
