package com.glovingtrainer.app.reader;

import com.glovingtrainer.app.BuildConfig;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, manifest = Config.NONE)
public class GlovingMovesTest
{
    private static boolean s_isSetup = false;

    private GlovingMoves mGlovingMoves;

    @Before
    public void testSetup() throws Exception
    {
        if (!s_isSetup)
        {
            SharedTestData.writeMovesFile(MovesReader.GLOVING_MOVES_FILE);
            s_isSetup = true;
        }

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
        Assert.assertEquals(SharedTestData.EXPECTED_GLOVING_MOVES, mGlovingMoves.getMoveGroups());
    }
}
