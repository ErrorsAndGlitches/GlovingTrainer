package com.glovingtrainer.app;

import android.content.Context;
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
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static com.glovingtrainer.app.AppState.MovesMode;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, manifest = Config.NONE)
@PowerMockIgnore({"org.mockito.*", "org.robolectric.*", "android.*"})
@PrepareForTest(AppState.class)
public class AppStateTest
{
    // starts PowerMock
    @Rule
    public PowerMockRule rule = new PowerMockRule();

    private AppState mAppState;

    @Before
    public void setup() throws Exception
    {
        clearSharedPreferences();
        mAppState = new AppState(RuntimeEnvironment.application);
    }

    @Test
    public void testDefaults()
    {
        Assert.assertEquals(MovesMode.Timeout, mAppState.getMode());
        Assert.assertEquals(getDefaultTimeout(), mAppState.getTimeoutDuration());
    }

    @Test
    public void testSaveMode()
    {
        mAppState.saveMode(MovesMode.Speech);
        Assert.assertEquals(MovesMode.Speech, mAppState.getMode());

        mAppState.saveMode(MovesMode.Timeout);
        Assert.assertEquals(MovesMode.Timeout, mAppState.getMode());
    }

    @Test
    public void testSaveDuration()
    {
        int duration = 99;
        mAppState.saveTimeoutDuration(duration);
        Assert.assertEquals(duration, mAppState.getTimeoutDuration());

        duration = 88;
        mAppState.saveTimeoutDuration(duration);
        Assert.assertEquals(duration, mAppState.getTimeoutDuration());
    }

    private void clearSharedPreferences()
    {
        RuntimeEnvironment.application.getSharedPreferences(getSharedPreferencesName(), Context.MODE_PRIVATE)
            .edit()
            .clear()
            .apply();
    }

    private static String getSharedPreferencesName()
    {
        return Whitebox.getInternalState(AppState.class, "PREFS_NAME");
    }

    private static int getDefaultTimeout()
    {
        return Whitebox.getInternalState(AppState.class, "DEFAULT_TIMEOUT_DURATION");
    }
}
