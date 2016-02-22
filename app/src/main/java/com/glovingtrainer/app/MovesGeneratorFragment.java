package com.glovingtrainer.app;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.glovingtrainer.app.reader.GlovingMoves;

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
    private final MoveTriggerTypeOnCheckedListener mTypeChangeListener;
    private final TimeoutPickerScrollListener      mTimeoutScrollListener;
    private       SpeechListener                   mSpeechListener;
    private       AppState                         mAppState;
    private       TextToSpeechHandler              mTextToSpeechHandler;
    private       TextView                         mGlovingMoveTextView;
    private       NumberPicker                     mMoveTimeoutPicker;
    private       int                              mTimeoutDuration;

    public MovesGeneratorFragment()
    {
        mGlovingMoves = GlovingMoves.getInstance();
        mUpdateTextHandler = new Handler(Looper.getMainLooper());
        mGlovingMoveUpdater = new GlovingMoveUpdater();
        mTypeChangeListener = new MoveTriggerTypeOnCheckedListener();
        mTimeoutScrollListener = new TimeoutPickerScrollListener();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mSpeechListener = new SpeechListener(getActivity());
        mAppState = new AppState(getActivity());
        mTimeoutDuration = mAppState.getTimeoutDuration();
        mTextToSpeechHandler = new TextToSpeechHandler(getActivity());
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

        mTypeChangeListener.ingest((RadioGroup) view.findViewById(R.id.move_trigger_type_group));

        CheckBox textToSpeechCheckedBox = (CheckBox) view.findViewById(R.id.text_to_speech_checked_box);
        textToSpeechCheckedBox.setOnCheckedChangeListener(mTextToSpeechHandler);

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

    void setRandomMove()
    {
        String randomMove = mGlovingMoves.getRandomMove();
        mGlovingMoveTextView.setText(randomMove);
        mTextToSpeechHandler.onNewMove(randomMove);
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
        mTypeChangeListener.setModeAsChecked(mode);
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

        @Override
        public void run()
        {
            if (mRunTimeout)
            {
                setRandomMove();
                mUpdateTextHandler.postDelayed(mGlovingMoveUpdater, TimeUnit.SECONDS.toMillis(getDuration()));
            }
        }

        void start()
        {
            mRunTimeout = true;
            run();
        }

        void stop()
        {
            mRunTimeout = false;
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

    private final class TextToSpeechHandler implements CheckBox.OnCheckedChangeListener, TextToSpeech.OnInitListener
    {
        private final Context      mContext;
        private       boolean      mEnabled;
        private       boolean      mTextToSpeechEngineReady;
        private       TextToSpeech mTextToSpeech;

        TextToSpeechHandler(Context context)
        {
            mContext = context;
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
        {
            mEnabled = isChecked;
            if (mEnabled)
            {
                mTextToSpeech = new TextToSpeech(mContext, this);
            }
            else if (mTextToSpeech != null)
            {
                mTextToSpeech.shutdown();
                mTextToSpeech = null;
                mTextToSpeechEngineReady = false;
            }
        }

        @Override
        public void onInit(int status)
        {
            mTextToSpeechEngineReady = TextToSpeech.SUCCESS == status;
        }

        void onNewMove(String move)
        {
            if (!mEnabled || !mTextToSpeechEngineReady)
            {
                return;
            }

            mTextToSpeech.speak(move, TextToSpeech.QUEUE_ADD, null);
        }
    }
}
