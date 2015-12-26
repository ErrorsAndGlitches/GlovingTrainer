package com.glovingtrainer.app;

import junit.framework.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.rule.PowerMockRule;
import org.powermock.reflect.Whitebox;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, manifest = Config.NONE)
@PowerMockIgnore({"org.mockito.*", "org.robolectric.*", "android.*"})
@PrepareForTest(GlovingMoves.class)
public class GlovingMovesTest
{
    private static final String TEST_GLOVING_MOVES_RESOURCE = "test_gloving_moves.txt";

    private static File getGlovingMovesFile()
    {
        return Whitebox.getInternalState(GlovingMoves.class, "GLOVING_MOVES_FILE");
    }

    private GlovingMoves mGlovingMoves;

    // starts PowerMock
    @Rule
    public PowerMockRule rule = new PowerMockRule();

    @Before
    public void setup() throws Exception
    {
        writeMovesFile();
        mGlovingMoves = GlovingMoves.getInstance();
    }

    @Test
    public void testSingleton() throws Exception
    {
        Assert.assertEquals(GlovingMoves.getInstance(), mGlovingMoves);
    }

    @Test
    public void testReadingFile() throws Exception
    {
        List<String> expected = Arrays.asList("this", "is", "a", "list", "of moves");
        Assert.assertEquals(expected, mGlovingMoves.getMoves());
    }

    private void writeMovesFile() throws IOException
    {
        String resourceFilename = getClass().getClassLoader().getResource(TEST_GLOVING_MOVES_RESOURCE).getFile();
        try (PrintWriter writer = new PrintWriter(new FileWriter(getGlovingMovesFile())))
        {
            try (BufferedReader reader = new BufferedReader(new FileReader(resourceFilename)))
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
