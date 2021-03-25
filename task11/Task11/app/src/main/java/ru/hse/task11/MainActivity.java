package ru.hse.task11;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.util.List;

public class MainActivity extends AppCompatActivity {


    NotificationManager notificationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button button = findViewById(R.id.button);

        if (Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            notificationManager = (NotificationManager)
                    getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

            NotificationChannel notificationChannel = new
                    NotificationChannel("mychannel1", "mychannel", NotificationManager
                    .IMPORTANCE_HIGH);

            AudioAttributes audioAttributes = new
                    AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build();

            notificationChannel.setSound(Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                            + "://" + getPackageName() + "/" + R.raw.al_heylisten),
                    audioAttributes);

            notificationManager.createNotificationChannel(notificationChannel);
        }
        else {
            notificationManager = (NotificationManager)
                    getSystemService(Context.NOTIFICATION_SERVICE);
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = MainActivity.this;
                Notification notification;

                if (Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    notification = new
                            Notification.Builder(context,"mychannel1")
                            .setContentTitle("Hi from Alex")
                            .setContentText("Hey, listen!")
                            .setTicker("new notification!")
                            .setChannelId("mychannel1")
                            .setSmallIcon(android.R.drawable.ic_dialog_alert)
                            .setOngoing(true)
                            .build();
                }
                else {

                    notification = new Notification.Builder(context)
                            .setContentTitle("Hi from Alex")
                            .setContentText("Hey, listen!")
                            .setTicker("new notification!")
                            .setSmallIcon(android.R.drawable.ic_dialog_alert)

                            .setSound(Uri.parse("android.resource://"+getPackageName()+"/" +
                                    R.raw.al_heylisten))
                            .build();
                }

                notificationManager.notify(0, notification);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.about) {
            AlertDialog.Builder dialog = new
                    AlertDialog.Builder(MainActivity.this);
            dialog.setMessage(getString(R.string.fio));
            dialog.setTitle("О программе");
            dialog.setNeutralButton("OK", new
                    DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            dialog.setIcon(R.mipmap.ic_launcher_round);
            AlertDialog alertDialog = dialog.create();
            alertDialog.show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}