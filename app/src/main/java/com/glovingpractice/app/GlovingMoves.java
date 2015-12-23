package com.glovingpractice.app;

import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

class GlovingMoves
{
    private static final    String       GLOVING_MOVES_FILENAME = "gloving_moves.txt";
    private static final    File         GLOVING_MOVES_FILE     = new File(Environment.getExternalStorageDirectory(),
                                                                           GLOVING_MOVES_FILENAME);
    private static volatile GlovingMoves s_instance             = null;

    private Collection<String> mGlovingMoves;

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

    Collection<String> getMoves()
    {
        if (mGlovingMoves == null)
        {
            populateGlovingMoves();
        }

        return mGlovingMoves;
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

        mGlovingMoves = Collections.unmodifiableCollection(glovingMoves);
    }
}
