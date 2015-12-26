package com.glovingtrainer.app;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class MovesGeneratorFragment extends Fragment implements SpeechListener.SpeechCallback
{
    private static final int    TIMEOUT_MIN    = 1;
    private static final int    TIMEOUT_MAX    = 20;
    private static final float  DISABLED_ALPHA = 0.5f;
    private static final float  ENABLED_ALPHA  = 1.0f;
    private static final String NEXT           = "next";


    private final GlovingMoves                     mGlovingMoves;
    private final Handler                          mUpdateTextHandler;
    private final GlovingMoveUpdater               mGlovingMoveUpdater;
    private final MoveTriggerTypeOnCheckedListener mCheckedListener;
    private final TimeoutPickerScrollListener      mTimeoutScrollListener;
    private       SpeechListener                   mSpeechListener;
    private       AppState                         mAppState;
    private       TextView                         mGlovingMoveTextView;
    private       NumberPicker                     mMoveTimeoutPicker;
    private       int                              mTimeoutDuration;

    public MovesGeneratorFragment()
    {
        mGlovingMoves = GlovingMoves.getInstance();
        mUpdateTextHandler = new Handler(Looper.getMainLooper());
        mGlovingMoveUpdater = new GlovingMoveUpdater();
        mCheckedListener = new MoveTriggerTypeOnCheckedListener();
        mTimeoutScrollListener = new TimeoutPickerScrollListener();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mSpeechListener = new SpeechListener(getActivity());
        AudioManager am = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
        am.setStreamMute(AudioManager.STREAM_MUSIC, true);

        mAppState = new AppState(getActivity());
        mTimeoutDuration = mAppState.getTimeoutDuration();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_moves_generator, container, false);
        mGlovingMoveTextView = (TextView) view.findViewById(R.id.section_label);

        mMoveTimeoutPicker = (NumberPicker) view.findViewById(R.id.move_timeout_picker);
        mMoveTimeoutPicker.setMinValue(TIMEOUT_MIN);
        mMoveTimeoutPicker.setMaxValue(TIMEOUT_MAX);
        mMoveTimeoutPicker.setValue(mAppState.getTimeoutDuration());
        mMoveTimeoutPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        mMoveTimeoutPicker.setOnScrollListener(mTimeoutScrollListener);

        mCheckedListener.ingest((RadioGroup) view.findViewById(R.id.move_trigger_type_group));

        setRandomMove();
        return view;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        startMode(mAppState.getMode());
    }

    @Override
    public void onPause()
    {
        super.onPause();
        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        mSpeechListener.stopListening();
        mGlovingMoveUpdater.stop();
    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        ((GlovingTrainerActivity) activity).onSectionAttached(0);
    }

    @Override
    public void onResults(List<String> results)
    {
        if (!results.isEmpty() && results.get(0).trim().toLowerCase().equals(NEXT))
        {
            setRandomMove();
        }
    }

    private void setRandomMove()
    {
        mGlovingMoveTextView.setText(mGlovingMoves.getRandomMove());
    }

    private int getDuration()
    {
        return mTimeoutDuration;
    }

    private void setDuration(int duration)
    {
        mTimeoutDuration = duration;
        mAppState.saveTimeoutDuration(duration);
    }

    private void startMode(AppState.MovesMode mode)
    {
        mCheckedListener.setModeAsChecked(mode);

        switch (mode)
        {
        case Speech:
            enableSpeechMode();
            break;
        case Timeout:
            enableTimeoutMode();
            break;
        }
    }

    private void enableTimeoutMode()
    {
        mAppState.saveMode(AppState.MovesMode.Timeout);
        mSpeechListener.stopListening();
        mMoveTimeoutPicker.setEnabled(true);
        mMoveTimeoutPicker.setAlpha(ENABLED_ALPHA);
        mGlovingMoveUpdater.start();
    }

    private void enableSpeechMode()
    {
        mAppState.saveMode(AppState.MovesMode.Speech);
        mMoveTimeoutPicker.setEnabled(false);
        mMoveTimeoutPicker.setAlpha(DISABLED_ALPHA);
        mSpeechListener.startListening(MovesGeneratorFragment.this);
        mGlovingMoveUpdater.stop();
    }

    private final class GlovingMoveUpdater implements Runnable
    {
        private boolean mRunTimeout;

        public void start()
        {
            mRunTimeout = true;
            run();
        }

        public void stop()
        {
            mRunTimeout = false;
        }

        @Override
        public void run()
        {
            if (mRunTimeout)
            {
                setRandomMove();
                mUpdateTextHandler.postDelayed(mGlovingMoveUpdater, TimeUnit.SECONDS.toMillis(getDuration()));
            }
        }
    }

    private final class MoveTriggerTypeOnCheckedListener implements RadioGroup.OnCheckedChangeListener
    {
        private RadioButton mSpeechRadioButton;
        private RadioButton mTimerRadioButton;

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId)
        {
            if (checkedId == mSpeechRadioButton.getId())
            {
                enableSpeechMode();
            }
            else if (checkedId == mTimerRadioButton.getId())
            {
                enableTimeoutMode();
            }
            else
            {
                Toast.makeText(getActivity(), "Unknown id: " + checkedId, Toast.LENGTH_SHORT).show();
            }
        }

        private void ingest(RadioGroup radioGroup)
        {
            radioGroup.setOnCheckedChangeListener(this);
            mSpeechRadioButton = (RadioButton) radioGroup.findViewById(R.id.speech_button);
            mTimerRadioButton = (RadioButton) radioGroup.findViewById(R.id.timer_button);
        }

        private void setModeAsChecked(AppState.MovesMode mode)
        {
            switch (mode)
            {
            case Speech:
                mSpeechRadioButton.setChecked(true);
                break;
            case Timeout:
                mTimerRadioButton.setChecked(true);
                break;
            }
        }
    }

    private final class TimeoutPickerScrollListener implements NumberPicker.OnScrollListener
    {
        @Override
        public void onScrollStateChange(NumberPicker view, int scrollState)
        {
            if (scrollState == SCROLL_STATE_IDLE)
            {
                setDuration(view.getValue());
            }
        }
    }
}
