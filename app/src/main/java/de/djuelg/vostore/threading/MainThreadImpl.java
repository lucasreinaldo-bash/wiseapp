package de.djuelg.vostore.threading;

import android.os.Handler;
import android.os.Looper;

import com.fernandocejas.arrow.optional.Optional;

import de.djuelg.vostore.domain.executor.MainThread;


/**
 * This class makes sure that the runnable we provide will be run on the main UI thread.
 */
public class MainThreadImpl implements MainThread {

    private static MainThread sMainThread;

    private Handler mHandler;

    private MainThreadImpl() {
        mHandler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void post(Runnable runnable) {
        mHandler.post(runnable);
    }

    public static MainThread getInstance() {
        return Optional.fromNullable(sMainThread).or(new MainThreadImpl());
    }
}
