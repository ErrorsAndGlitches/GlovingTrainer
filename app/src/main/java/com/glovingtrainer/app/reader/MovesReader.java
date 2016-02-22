package com.glovingtrainer.app.reader;

import android.os.Environment;
import android.util.JsonReader;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class MovesReader
{
    private static final String GLOVING_MOVES_FILENAME = "gloving_moves.json";
    static final         File   GLOVING_MOVES_FILE     = new File(Environment.getExternalStorageDirectory(),
                                                                  GLOVING_MOVES_FILENAME);

    List<MoveGroup> readMoves()
    {
        return readMoves(GLOVING_MOVES_FILE);
    }

    List<MoveGroup> readMoves(File file)
    {
        List<MoveGroup> glovingMoveGroups = new ArrayList<>();
        try (JsonReader reader = new JsonReader(new FileReader(file)))
        {
            reader.beginObject();
            while (reader.hasNext())
            {
                glovingMoveGroups.add(readMoveGroup(reader));
            }
            reader.endObject();
        }
        catch (IOException e)
        {
            MoveGroup moveGroup = new MoveGroup("ERROR");
            moveGroup.add(e.getMessage());
            glovingMoveGroups.add(moveGroup);
        }

        return glovingMoveGroups;
    }

    private MoveGroup readMoveGroup(JsonReader reader) throws IOException
    {
        MoveGroup moveGroup = new MoveGroup(reader.nextName());
        reader.beginArray();
        while (reader.hasNext())
        {
            moveGroup.add(reader.nextString());
        }
        reader.endArray();

        return moveGroup;
    }
}
