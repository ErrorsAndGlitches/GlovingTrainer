package com.glovingtrainer.app;

import junit.framework.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.reflect.Whitebox;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.Arrays;
import java.util.List;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, manifest = Config.NONE)
@PowerMockIgnore({"org.mockito.*", "org.robolectric.*", "android.*"})
@PrepareForTest(ListMovesFragment.class)
public class ListMovesFragmentTest
{
    @Test
    public void testListEnumeration() throws Exception
    {
        List<String> list = Arrays.asList("a", "b", "c");
        List<String> expected = Arrays.asList("1. a", "2. b", "3. c");

        Assert.assertEquals(expected, Whitebox.invokeMethod(ListMovesFragment.class, "enumerateList", list));
    }
}
