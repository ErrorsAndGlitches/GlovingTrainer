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
    private final GlovingMoves mGlovingMoves;

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
        ((GlovingTrainerActivity) activity).onSectionAttached(2);
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
