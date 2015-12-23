package com.glovingtrainer.app;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ListMovesFragment extends Fragment
{
    private static final String ARG_SECTION_NUMBER = "section_number";

    private final GlovingMoves mGlovingMoves;

    public static ListMovesFragment newInstance(int sectionNumber)
    {
        ListMovesFragment fragment = new ListMovesFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public ListMovesFragment()
    {
        mGlovingMoves = GlovingMoves.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_random_moves, container, false);
        TextView textView = (TextView) view.findViewById(R.id.section_label);
        textView.setText(getEnumeratedMoves());

        return view;
    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        ((GlovingTrainerActivity) activity).onSectionAttached(getArguments().getInt(ARG_SECTION_NUMBER));
    }

    private String getEnumeratedMoves()
    {
        StringBuilder builder = new StringBuilder();
        int count = 0;
        for (String move : mGlovingMoves.getMoves())
        {
            builder.append(++count)
                .append(") ")
                .append(move)
                .append('\n');
        }
        return builder.toString();
    }
}
