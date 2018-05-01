package com.dragonnedevelopment.bakingapp.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.dragonnedevelopment.bakingapp.R;
import com.dragonnedevelopment.bakingapp.utils.Utils;

/**
 * BakingApp Created by Muir on 01/05/2018.
 *
 * Notifies when there is a change in network/internet connection
 */
public class ConnectivityReceiver extends BroadcastReceiver {

    public static ConnectivityReceiverListener connectivityReceiverListener;

    public interface ConnectivityReceiverListener {
        void onConnectivityChanged(boolean isConnected);
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        String intentAction;

        if (connectivityReceiverListener != null) {
            intentAction = intent.getAction();
            if (!Utils.isEmptyString(intentAction)) {
                if (intentAction.equals(context.getString(R.string.intent_filter_connectivity_change)) ||
                        intentAction.equals(context.getString(R.string.intent_filter_wifi_state_changed))) {
                    connectivityReceiverListener.onConnectivityChanged(Utils.hasConnectivity(context));
                }
            }
        }
    }
}
