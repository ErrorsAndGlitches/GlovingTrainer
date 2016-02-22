package com.glovingtrainer.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
import com.glovingtrainer.app.reader.MoveGroup;

import java.util.List;

class ListMovesExpandableAdapter extends BaseExpandableListAdapter
{
    private final Context         mContext;
    private final List<MoveGroup> mMoveGroups;

    ListMovesExpandableAdapter(Context context, List<MoveGroup> moveGroups)
    {
        mContext = context;
        mMoveGroups = moveGroups;
    }

    @Override
    public int getGroupCount()
    {
        return mMoveGroups.size();
    }

    @Override
    public int getChildrenCount(int groupPosition)
    {
        return mMoveGroups.get(groupPosition).moves.size();
    }

    @Override
    public Object getGroup(int groupPosition)
    {
        return mMoveGroups.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition)
    {
        return mMoveGroups.get(groupPosition).moves.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition)
    {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition)
    {
        return childPosition;
    }

    @Override
    public boolean hasStableIds()
    {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent)
    {
        MoveGroup moveGroup = mMoveGroups.get(groupPosition);
        String groupName = moveGroup.moveType;

        if (convertView == null)
        {
            convertView = getTextView(R.layout.group_item);
        }

        TextView groupTextView = (TextView) convertView;
        groupTextView.setText(groupName);
        return groupTextView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView,
                             ViewGroup parent)
    {
        if (convertView == null)
        {
            convertView = getTextView(R.layout.child_item);
        }

        TextView childTextView = (TextView) convertView;
        childTextView.setText(mMoveGroups.get(groupPosition).moves.get(childPosition));

        return childTextView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition)
    {
        return false;
    }

    private View getTextView(int res_id)
    {
        return ((LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(res_id, null);
    }
}
