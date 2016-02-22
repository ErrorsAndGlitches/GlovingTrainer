package com.glovingtrainer.app.reader;

import org.apache.commons.lang3.builder.EqualsBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MoveGroup
{
    public final  String       moveType;
    public final  List<String> moves;
    private final List<String> mMoves;

    MoveGroup(String moveType)
    {
        this.moveType = moveType;
        mMoves = new ArrayList<>();
        moves = Collections.unmodifiableList(mMoves);
    }

    void add(String move)
    {
        mMoves.add(move);
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }

        if (o == null || getClass() != o.getClass())
        {
            return false;
        }

        MoveGroup that = (MoveGroup) o;

        return new EqualsBuilder()
            .append(moveType, that.moveType)
            .append(moves, that.moves)
            .isEquals();
    }

    @Override
    public int hashCode()
    {
        return new org.apache.commons.lang3.builder.HashCodeBuilder(17, 37)
            .append(moveType)
            .append(moves)
            .toHashCode();
    }
}
