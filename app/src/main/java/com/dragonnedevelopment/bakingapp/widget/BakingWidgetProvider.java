package com.dragonnedevelopment.bakingapp.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.dragonnedevelopment.bakingapp.R;
import com.dragonnedevelopment.bakingapp.activities.WidgetActivity;
import com.dragonnedevelopment.bakingapp.utils.Config;

/**
 * BakingApp Created by Muir on 01/05/2018.
 * <p>
 * Implementation of App Widget functionality
 */
public class BakingWidgetProvider extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId, String[] recipe) {

        // create an Intent to launch the Activity when clicked
        Intent intent = new Intent(context, WidgetActivity.class);
        intent.putExtra(Config.INTENT_KEY_WIDGET_RECIPE, recipe);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // construct the Remoteviews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking_widget);

        // update View
        if (!recipe[0].equals("0")) {
            // check if a recipe ID is available. if so, update the UI
            views.setTextViewText(R.id.app_widget_header, recipe[1]);
            views.setTextViewText(R.id.app_widget_text, recipe[2]);
        } else {
            // update the UI with default text if there is no recipe ID available
            views.setTextViewText(R.id.app_widget_header, "");
            views.setTextViewText(R.id.app_widget_text, context.getString(R.string.app_widget_text));
        }

        // widgets allow click handlers to only launch pending intents
        views.setOnClickPendingIntent(R.id.app_widget_text, pendingIntent);

        // instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        String[] recipe = new String[3];
        recipe[0] = "0";
        recipe[1] = "";
        recipe[2] = context.getString(R.string.app_widget_text);
        updateBakingWidgets(context, appWidgetManager, appWidgetIds, recipe);
    }

    public static void updateBakingWidgets(Context context, AppWidgetManager appWidgetManager,
                                           int[] appWidgetIds, String[] recipe) {
        for (int appWidgetId : appWidgetIds)
            updateAppWidget(context, appWidgetManager, appWidgetId, recipe);
    }

    @Override
    public void onEnabled(Context context) {
        // enter relevant functionality for when the first widget is created. Not yet implemented.
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled. Not yet implemented.
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
    }
}
