package com.example.leosunzh.myphone.ui;

/**
 * Created by leosunzh on 2015/12/23.
 */

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.provider.Settings;
import android.util.Log;

public class ToneTools {
    private Context mContext;
    private static final int TONE_LENGTH_MS = 150;
    private static final int TONE_RELATIVE_VOLUME = 80;
    private static final int DIAL_TONE_STREAM_TYPE = AudioManager.STREAM_MUSIC;
    private static final String TAG = null;
    private ToneGenerator mToneGenerator;
    private Object mToneGeneratorLock = new Object();
    private boolean mDTMFToneEnabled;

    public ToneTools(Context context) {
        super();
        mContext=context;

        mDTMFToneEnabled = Settings.System.getInt(mContext.getContentResolver(), Settings.System.DTMF_TONE_WHEN_DIALING, 1) == 1;

        synchronized (mToneGeneratorLock) {
            if (mToneGenerator == null) {
                try {
                    // we want the user to be ableto control the volume of the
                    // dial tones
                    // outside of a call, so we usethe stream type that is also
                    // mapped to the
                    // volume control keys for thisactivity
                    mToneGenerator = new ToneGenerator(DIAL_TONE_STREAM_TYPE, TONE_RELATIVE_VOLUME);
                    ((Activity)context).setVolumeControlStream(DIAL_TONE_STREAM_TYPE);
                } catch (RuntimeException e) {
                    Log.w(TAG, "Exceptioncaught while creating local tone generator: " + e);
                    mToneGenerator = null;
                }
            }
        }
    }

    void playTone(int tone) {
        if (!mDTMFToneEnabled) {
            return;
        }

        AudioManager audioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        int ringerMode = audioManager.getRingerMode();
        if ((ringerMode == AudioManager.RINGER_MODE_SILENT) || (ringerMode == AudioManager.RINGER_MODE_VIBRATE)) {
            return;
        }

        synchronized (mToneGeneratorLock) {
            if (mToneGenerator == null) {
                Log.w(TAG, "playTone:mToneGenerator == null, tone: " + tone);
                return;
            }
            mToneGenerator.startTone(tone, TONE_LENGTH_MS);
            close();
        }
    }

    public void close() {
        synchronized (mToneGeneratorLock) {
            if (mToneGenerator != null) {
                mToneGenerator.release();
                mToneGenerator = null;
            }
        }
    }
}

