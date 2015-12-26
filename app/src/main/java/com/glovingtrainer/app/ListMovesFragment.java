package com.glovingtrainer.app;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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
        ListView listView = (ListView) view.findViewById(R.id.moves_list);
        listView.setAdapter(new ArrayAdapter<>(getActivity(),
                                               android.R.layout.simple_list_item_1,
                                               enumerateList(mGlovingMoves.getMoves())));
        return view;
    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        ((GlovingTrainerActivity) activity).onSectionAttached(1);
    }

    private static List<String> enumerateList(List<String> list)
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
