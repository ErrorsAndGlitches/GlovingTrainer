package com.glovingtrainer.app.reader;

import com.glovingtrainer.app.BuildConfig;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.io.File;
import java.net.URISyntaxException;
import java.util.List;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, manifest = Config.NONE)
public class MovesReaderTests
{
    private MovesReader mMovesReader;

    @Before
    public void testSetup()
    {
        mMovesReader = new MovesReader();
    }

    @Test
    public void readGloveMovesFile() throws URISyntaxException
    {
        List<MoveGroup> moveGroup = mMovesReader.readMoves(new File(SharedTestData.TEST_GLOVING_MOVES_RESOURCE_URL.toURI()));
        Assert.assertEquals(SharedTestData.EXPECTED_GLOVING_MOVES, moveGroup);
    }
}
