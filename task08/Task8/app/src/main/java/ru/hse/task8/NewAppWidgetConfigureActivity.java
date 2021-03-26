package ru.hse.task8;

import android.app.Activity;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RemoteViews;
import android.widget.TextView;

import androidx.annotation.Nullable;

/**
 * The configuration screen for the {@link NewAppWidget NewAppWidget} AppWidget.
 */
public class NewAppWidgetConfigureActivity extends Activity {

    private NewAppWidgetConfigureActivity context;
    private int widgetID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_app_widget_configure);
        setResult(RESULT_CANCELED);

        context = this;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            widgetID = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,
                            AppWidgetManager.INVALID_APPWIDGET_ID);

            final AppWidgetManager widgetManager = AppWidgetManager.getInstance(context);
            final RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);
            final EditText editText = findViewById(R.id.appwidget_text);

            Button button = findViewById(R.id.add_button);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String text2remember = editText.getText().toString();

                    Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse(text2remember));

                    PendingIntent pending= PendingIntent.getActivity(context, 0, intent, 0);

                    views.setOnClickPendingIntent(R.id.textView2, pending);
                    views.setTextViewText(R.id.textView2, text2remember);
                    widgetManager.updateAppWidget(widgetID, views);

                    Intent resultValue = new Intent();

                    resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetID);
                    setResult(RESULT_OK, resultValue);
                    finish();
                }
            });
        }

    }
}