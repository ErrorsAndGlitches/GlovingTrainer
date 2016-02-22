package com.glovingtrainer.app;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import com.glovingtrainer.app.reader.GlovingMoves;

import java.util.ArrayList;
import java.util.List;

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
        View view = inflater.inflate(R.layout.fragment_moves_list, container, false);
        ExpandableListView listView = (ExpandableListView) view.findViewById(R.id.move_groups);
        listView.setAdapter(new ListMovesExpandableAdapter(getContext(), mGlovingMoves.getMoveGroups()));
        return view;
    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        ((GlovingTrainerActivity) activity).onSectionAttached(1);
    }

    static List<String> enumerateList(List<String> list)
    {
        List<String> enumeratedList = new ArrayList<>(list.size());
        int index = 0;
        for (String string : list)
        {
            enumeratedList.add(String.valueOf(++index) + ". " + string);
        }

        return enumeratedList;
    }
}
