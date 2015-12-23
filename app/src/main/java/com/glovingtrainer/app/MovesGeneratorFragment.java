package com.glovingtrainer.app;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

public class MovesGeneratorFragment extends Fragment
{
    private final GlovingMoves       mGlovingMoves;
    private final Handler            mUpdateTextHandler;
    private final GlovingMoveUpdater mGlovingMoveUpdater;
    private       TextView           mGlovingMoveTextView;
    private       boolean            mShouldUpdateMoves;

    public MovesGeneratorFragment()
    {
        mGlovingMoves = GlovingMoves.getInstance();
        mUpdateTextHandler = new Handler(Looper.getMainLooper());
        mGlovingMoveUpdater = new GlovingMoveUpdater();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_moves_generator, container, false);
        mGlovingMoveTextView = (TextView) view.findViewById(R.id.section_label);
        setRandomMove();
        return view;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        mShouldUpdateMoves = true;
        mGlovingMoveUpdater.run();
    }

    @Override
    public void onPause()
    {
        super.onPause();
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

    private final class GlovingMoveUpdater implements Runnable
    {
        @Override
        public void run()
        {
            if (mShouldUpdateMoves)
            {
                setRandomMove();
                mUpdateTextHandler.postDelayed(mGlovingMoveUpdater, TimeUnit.SECONDS.toMillis(5));
            }
        }
    }
}
