package com.glovingpractice.app;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class GlovingMovesFragment extends Fragment
{
    private static final String ARG_SECTION_NUMBER = "section_number";

    private final GlovingMoves mGlovingMoves;

    public static GlovingMovesFragment newInstance(int sectionNumber)
    {
        GlovingMovesFragment fragment = new GlovingMovesFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public GlovingMovesFragment()
    {
        mGlovingMoves = new GlovingMoves();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_random_moves, container, false);
        TextView textView = (TextView) view.findViewById(R.id.section_label);
        textView.setText(getMoves());

        return view;
    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        ((RandomMoves) activity).onSectionAttached(getArguments().getInt(ARG_SECTION_NUMBER));
    }

    private String getMoves()
    {
        String moves = "";
        for (String move : mGlovingMoves.getMoves())
        {
            moves += move;
            moves += "\n";
        }
        return moves;
    }
}
