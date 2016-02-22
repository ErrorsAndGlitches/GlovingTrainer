package com.glovingtrainer.app;

import android.content.Context;
import android.content.SharedPreferences;

class AppState
{
    static final         String PREFS_NAME               = "APP_STATE_SHARED_PREFS";
    static final         int    DEFAULT_TIMEOUT_DURATION = 7;
    private static final String MOVES_MODE_KEY           = "MOVES_MODE";
    private static final String TIMEOUT_DURATION_KEY     = "TIMEOUT_DURATION";

    private final SharedPreferences mPrefs;

    enum MovesMode
    {
        Speech(0),
        Timeout(1);

        MovesMode(int value)
        {
            mValue = value;
        }

        private final int mValue;
    }

    AppState(Context context)
    {
        mPrefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    void saveMode(MovesMode mode)
    {
        mPrefs.edit().putInt(MOVES_MODE_KEY, mode.mValue).apply();
    }

    MovesMode getMode()
    {
        int mode = mPrefs.getInt(MOVES_MODE_KEY, MovesMode.Timeout.mValue);

        for (MovesMode movesMode : MovesMode.values())
        {
            if (mode == movesMode.mValue)
            {
                return movesMode;
            }
        }

        return null;
    }

    void saveTimeoutDuration(int duration)
    {
        mPrefs.edit().putInt(TIMEOUT_DURATION_KEY, duration).apply();
    }

    int getTimeoutDuration()
    {
        return mPrefs.getInt(TIMEOUT_DURATION_KEY, DEFAULT_TIMEOUT_DURATION);
    }
}
