package com.glovingtrainer.app;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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

import java.util.concurrent.TimeUnit;

public class MovesGeneratorFragment extends Fragment
{
    private static final int TIMEOUT_MIN     = 1;
    private static final int TIMEOUT_MAX     = 20;
    private static final int TIMEOUT_DEFAULT = 5;

    private final GlovingMoves                     mGlovingMoves;
    private final Handler                          mUpdateTextHandler;
    private final GlovingMoveUpdater               mGlovingMoveUpdater;
    private final MoveTriggerTypeOnCheckedListener mCheckedListener;
    private final TimeoutPickerScrollListener      mTimeoutScrollListener;
    private       TextView                         mGlovingMoveTextView;
    private       NumberPicker                     mMoveTimeoutPicker;
    private       boolean                          mShouldUpdateMoves;
    private       int                              mTimeoutDuration;

    public MovesGeneratorFragment()
    {
        mGlovingMoves = GlovingMoves.getInstance();
        mUpdateTextHandler = new Handler(Looper.getMainLooper());
        mGlovingMoveUpdater = new GlovingMoveUpdater();
        mCheckedListener = new MoveTriggerTypeOnCheckedListener();
        mTimeoutScrollListener = new TimeoutPickerScrollListener();
        mTimeoutDuration = TIMEOUT_DEFAULT;
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
        mMoveTimeoutPicker.setValue(TIMEOUT_DEFAULT);
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
        mShouldUpdateMoves = true;
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        mGlovingMoveUpdater.run();
    }

    @Override
    public void onPause()
    {
        super.onPause();
        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        mShouldUpdateMoves = false;
    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        ((GlovingTrainerActivity) activity).onSectionAttached(1);
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
    }

    private final class GlovingMoveUpdater implements Runnable
    {
        @Override
        public void run()
        {
            if (mShouldUpdateMoves)
            {
                setRandomMove();
                mUpdateTextHandler.postDelayed(mGlovingMoveUpdater, TimeUnit.SECONDS.toMillis(getDuration()));
            }
        }
    }

    private final class MoveTriggerTypeOnCheckedListener implements RadioGroup.OnCheckedChangeListener
    {
        private static final float DISABLED_ALPHA = 0.5f;
        private static final float ENABLED_ALPHA  = 1.0f;

        private RadioButton mSpeechRadioButton;
        private RadioButton mTimerRadioButton;

        private void ingest(RadioGroup radioGroup)
        {
            radioGroup.setOnCheckedChangeListener(this);
            mSpeechRadioButton = (RadioButton) radioGroup.findViewById(R.id.speech_button);
            mTimerRadioButton = (RadioButton) radioGroup.findViewById(R.id.timer_button);
        }

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId)
        {
            if (checkedId == mSpeechRadioButton.getId())
            {
                mMoveTimeoutPicker.setEnabled(false);
                mMoveTimeoutPicker.setAlpha(DISABLED_ALPHA);
            }
            else if (checkedId == mTimerRadioButton.getId())
            {
                mMoveTimeoutPicker.setEnabled(true);
                mMoveTimeoutPicker.setAlpha(ENABLED_ALPHA);
            }
            else
            {
                Toast.makeText(getActivity(), "Unknown id: " + checkedId, Toast.LENGTH_SHORT).show();
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
