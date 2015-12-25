package com.glovingtrainer.app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.SpeechRecognizer;

import java.util.List;

class SpeechListener implements RecognitionListener
{
    private static final Intent EMPTY_INTENT = new Intent();

    interface SpeechCallback
    {
        void onResults(List<String> results);
    }

    private final Context          mContext;
    private       SpeechRecognizer mSpeechRecognizer;
    private       SpeechCallback   mCallback;

    SpeechListener(Context context)
    {
        mContext = context;
    }

    @Override
    public void onError(int error)
    {
        stopListening();
        startListening();
    }

    @Override
    public void onResults(Bundle results)
    {
        mCallback.onResults(results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION));
        mSpeechRecognizer.startListening(EMPTY_INTENT);
    }

    @Override
    public void onReadyForSpeech(Bundle params)
    {
    }

    @Override
    public void onBeginningOfSpeech()
    {
    }

    @Override
    public void onRmsChanged(float rmsdB)
    {
    }

    @Override
    public void onBufferReceived(byte[] buffer)
    {
    }

    @Override
    public void onEndOfSpeech()
    {
    }

    @Override
    public void onPartialResults(Bundle partialResults)
    {
    }

    @Override
    public void onEvent(int eventType, Bundle params)
    {
    }

    void startListening(SpeechCallback callback)
    {
        if (callback != null)
        {
            mCallback = callback;
            startListening();
        }
    }

    void stopListening()
    {
        if (mSpeechRecognizer == null)
        {
            return;
        }

        mSpeechRecognizer.stopListening();
        mSpeechRecognizer.destroy();
        mSpeechRecognizer = null;
    }

    private void startListening()
    {
        mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(mContext);
        mSpeechRecognizer.setRecognitionListener(this);
        mSpeechRecognizer.startListening(EMPTY_INTENT);
    }
}
