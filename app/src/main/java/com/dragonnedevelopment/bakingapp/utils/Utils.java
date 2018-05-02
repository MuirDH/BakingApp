package com.dragonnedevelopment.bakingapp.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import butterknife.ButterKnife;

/**
 * BakingApp Created by Muir on 30/04/2018.
 * <p>
 * This class contains common methods
 */
public class Utils {

    /*
     * this is a private constructor and only meant to hold static variables and methods which can
     * be access directly from the Utils class.
     */
    private void Utils() {

    }

    /**
     * Utility method to construct a Toast message
     *
     * @param context the activity in which the toast should be displayed
     * @param toast   the toast itself
     * @param message the message displayed by the toast
     * @return Toast object
     */
    public static Toast showToastMessage(Context context, Toast toast, String message) {
        if (toast != null) toast.cancel();
        toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        return toast;
    }

    /**
     * Utility method to check if a string is empty
     *
     * @param stringToCheck the string being checked
     * @return TRUE (if the string is empty), FALSE otherwise
     */
    public static boolean isEmptyString(String stringToCheck) {
        return (stringToCheck == null) || (stringToCheck.trim().length() == 0);
    }

    /**
     * Utility method to check if a number is zero
     *
     * @param numberToCheck the number being checked
     * @return TRUE ( if the number is zero), FALSE if it is any other number
     */
    public static boolean isNumberZero(int numberToCheck) {
        return (numberToCheck == 0);
    }

    /**
     * method to check if the users device has connectivity
     *
     * @param context the app context
     * @return TRUE (if connected), FALSE if there is no connectivity
     */
    public static boolean hasConnectivity(Context context) {
        final ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (connectivityManager != null) networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null) && networkInfo.isConnected();

    }

    /**
     * method to convert the first letter of a string to Uppercase
     *
     * @param word the string being converted
     * @return word with the first letter being Uppercase
     */
    public static String convertStringToFirstCapital(String word) {
        return word.toUpperCase().charAt(0) + word.substring(1).toLowerCase();
    }

    /**
     * method to convert a string to Title Case
     *
     * @param inputString the string to be converted
     * @return string in Title Case
     */
    public static String convertStringToTitleCase(String inputString) {
        String[] wordsArray = inputString.split(" ");
        for (int i = 0; i < wordsArray.length; i++)
            wordsArray[i] = convertStringToFirstCapital(wordsArray[i]);
        return TextUtils.join(" ", wordsArray);
    }

    /**
     * Butterknife setter interface to change view visibility
     */
    public static final ButterKnife.Setter<View, Integer> VISIBILITY = new ButterKnife.Setter<View, Integer>() {
        @Override
        public void set(@NonNull View view, Integer value, int index) {
            view.setVisibility(value);
        }
    };
}
