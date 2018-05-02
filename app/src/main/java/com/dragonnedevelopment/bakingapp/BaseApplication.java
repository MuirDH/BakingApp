package com.dragonnedevelopment.bakingapp;

import android.app.Application;
import android.content.IntentFilter;

import com.dragonnedevelopment.bakingapp.network.ConnectivityReceiver;

import timber.log.Timber;

/**
 * BakingApp Created by Muir on 01/05/2018.
 */
public class BaseApplication extends Application {

    private static IntentFilter intentFilter;
    private static ConnectivityReceiver receiver;
    private static BaseApplication applicationInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        applicationInstance = this;
        receiver = new ConnectivityReceiver();

        intentFilter = new IntentFilter();
        intentFilter.addAction(getString(R.string.intent_filter_connectivity_change));
        intentFilter.addAction(getString(R.string.intent_filter_wifi_state_changed));

        Timber.plant(new Timber.DebugTree());

        setReceiverStatus(true);
    }

    /**
     * registers and unregisters the receiver based on whether the application is visible or not
     *
     * @param isActivityVisible boolean which says if the activity is visible
     */
    public static void setReceiverStatus(boolean isActivityVisible) {
        // register broadcast receiver
        // unregister broadcast receiver to prevent memory leaks
        if (isActivityVisible) getApplicationInstance().registerReceiver(receiver, intentFilter);
        else getApplicationInstance().unregisterReceiver(receiver);
    }

    /**
     * @return the applicationInstance
     */
    private static synchronized BaseApplication getApplicationInstance() {
        return applicationInstance;
    }

    /**
     * sets the connectivity listener
     *
     * @param listener - checks for connectivity
     */
    public static void setConnectivityListener
    (ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }
}
