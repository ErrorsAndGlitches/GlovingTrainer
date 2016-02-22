package com.glovingtrainer.app.reader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class GlovingMoves
{
    private static final    Random       sRandom    = new Random(System.currentTimeMillis());
    private static volatile GlovingMoves s_instance = null;

    private final MovesReader     mMovesReader;
    private       List<MoveGroup> mGlovingMoveGroups;
    private       List<String>    mGlovingMoves;

    public static GlovingMoves getInstance()
    {
        if (s_instance == null)
        {
            synchronized (sRandom)
            {
                if (s_instance == null)
                {
                    s_instance = new GlovingMoves();
                }
            }
        }

        return s_instance;
    }

    private GlovingMoves()
    {
        mMovesReader = new MovesReader();
    }

    public List<String> getMoves()
    {
        initIfNeeded();
        return mGlovingMoves;
    }

    public List<MoveGroup> getMoveGroups()
    {
        initIfNeeded();
        return mGlovingMoveGroups;
    }

    public String getRandomMove()
    {
        initIfNeeded();
        return mGlovingMoves.get(sRandom.nextInt(mGlovingMoves.size()));
    }

    private void initIfNeeded()
    {
        if (mGlovingMoveGroups == null)
        {
            mGlovingMoveGroups = Collections.unmodifiableList(mMovesReader.readMoves());

            int arrSize = 0;
            for (MoveGroup group : mGlovingMoveGroups)
            {
                arrSize += group.moves.size();
            }
            mGlovingMoves = new ArrayList<>(arrSize);

            for (MoveGroup group : mGlovingMoveGroups)
            {
                for (String move : group.moves)
                {
                    mGlovingMoves.add(move);
                }
            }

            mGlovingMoves = Collections.unmodifiableList(mGlovingMoves);
        }
    }
}
