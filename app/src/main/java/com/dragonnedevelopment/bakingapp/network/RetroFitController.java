package com.dragonnedevelopment.bakingapp.network;

import android.content.Context;

import com.dragonnedevelopment.bakingapp.exceptions.NoConnectivityException;
import com.dragonnedevelopment.bakingapp.utils.Config;
import com.dragonnedevelopment.bakingapp.utils.Utils;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * BakingApp Created by Muir on 02/05/2018.
 * Issues network requests to the URL used to retrieve data
 */
public class RetroFitController {

    private static Retrofit retrofit = null;

    public static Retrofit getClient(Context context) throws NoConnectivityException {
        // if device does not have a connection, throw an exception error and exit
        if (!Utils.hasConnectivity(context)) throw new NoConnectivityException();

        if (retrofit == null) {
            // create OkHttpClient.Build object
            OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();

            // create HttpLoggingInterceptor object and set logging level
            HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
            // BASIC prints request methods and response codes
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);

            /*
             * couple OkHttpClient.Builder object with HtppLoggingInterceptor object and set
             * connection timeout duration
             */
            okHttpClientBuilder.addInterceptor(httpLoggingInterceptor);
            okHttpClientBuilder.connectTimeout(Config.DURATION_CONNECTION_TIMEOUT, TimeUnit.MILLISECONDS);

            // Create Retrofit object and attach OkHttp client to it
            retrofit = new Retrofit.Builder()
                    .baseUrl(Config.RECIPE_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClientBuilder.build())
                    .build();
        }

        return retrofit;
    }
}
