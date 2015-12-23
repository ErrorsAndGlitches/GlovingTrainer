package com.glovingtrainer.app;

import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

class GlovingMoves
{
    private static final    String       GLOVING_MOVES_FILENAME = "gloving_moves.txt";
    private static final    File         GLOVING_MOVES_FILE     = new File(Environment.getExternalStorageDirectory(),
                                                                           GLOVING_MOVES_FILENAME);
    private static final    Random       sRandom                = new Random(System.currentTimeMillis());
    private static volatile GlovingMoves s_instance             = null;

    private List<String> mGlovingMoves;

    static GlovingMoves getInstance()
    {
        if (s_instance == null)
        {
            synchronized (GLOVING_MOVES_FILE)
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
    }

    List<String> getMoves()
    {
        initIfNeeded();
        return mGlovingMoves;
    }

    String getRandomMove()
    {
        initIfNeeded();
        return mGlovingMoves.get(sRandom.nextInt(mGlovingMoves.size()));
    }

    private void initIfNeeded()
    {
        if (mGlovingMoves == null)
        {
            populateGlovingMoves();
        }
    }

    private void populateGlovingMoves()
    {
        List<String> glovingMoves = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(GLOVING_MOVES_FILE)))
        {
            String line;
            while ((line = reader.readLine()) != null)
            {
                glovingMoves.add(line.trim());
            }
        }
        catch (IOException e)
        {
            glovingMoves.add(e.getMessage());
        }

        mGlovingMoves = Collections.unmodifiableList(glovingMoves);
    }
}
