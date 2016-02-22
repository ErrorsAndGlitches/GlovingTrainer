package com.glovingtrainer.app.reader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SharedTestData
{
    public static final List<MoveGroup> EXPECTED_GLOVING_MOVES;
    static final String TEST_GLOVING_MOVES_FILENAME     = "test_gloving_moves.json";
    static final URL    TEST_GLOVING_MOVES_RESOURCE_URL = SharedTestData.class.getResource(TEST_GLOVING_MOVES_FILENAME);

    static
    {
        List<MoveGroup> moveGroups = new ArrayList<>(2);
        MoveGroup group = new MoveGroup("moves_group_0");
        group.add("a");
        group.add("b");
        group.add("c");
        moveGroups.add(group);
        group = new MoveGroup("moves_group_1");
        group.add("d");
        group.add("e");
        group.add("f");
        moveGroups.add(group);

        EXPECTED_GLOVING_MOVES = Collections.unmodifiableList(moveGroups);
    }

    static void writeMovesFile(File file) throws IOException, URISyntaxException
    {
        try (PrintWriter writer = new PrintWriter(new FileWriter(file)))
        {
            try (BufferedReader reader = new BufferedReader(new FileReader(new File(TEST_GLOVING_MOVES_RESOURCE_URL.toURI()))))
            {
                String line;
                while ((line = reader.readLine()) != null)
                {
                    writer.println(line);
                }
            }
        }
    }
}
