package com.glovingtrainer.app;

import junit.framework.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.Arrays;
import java.util.List;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, manifest = Config.NONE)
public class ListMovesFragmentTest
{
    @Test
    public void testListEnumeration() throws Exception
    {
        List<String> list = Arrays.asList("a", "b", "c");
        List<String> expected = Arrays.asList("1. a", "2. b", "3. c");

        Assert.assertEquals(expected, ListMovesFragment.enumerateList(list));
    }
}
