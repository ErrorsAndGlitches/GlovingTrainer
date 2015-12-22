package com.glovingpractice.app;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class GlovingMovesFragment extends Fragment
{
    private static final String ARG_SECTION_NUMBER = "section_number";

    public static GlovingMovesFragment newInstance(int sectionNumber)
    {
        GlovingMovesFragment fragment = new GlovingMovesFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_random_moves, container, false);
    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        ((RandomMoves) activity).onSectionAttached(getArguments().getInt(ARG_SECTION_NUMBER));
    }
}
