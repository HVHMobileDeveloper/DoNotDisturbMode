package com.example.donotdisturb;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    //context
    private Context mContext;
    private Activity mActivity;

    //declare view
    private LinearLayout mRootLayout;
    private Button mBtnFilterNone;
    private Button mBtnFilterPriority;
    private Button mBtnFilterAlarms;
    private Button mBtnFilterAll;
    private TextView mTVStats;


    //service
    private NotificationManager mNotificationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Get the application context
        mContext = getApplicationContext();
        mActivity = MainActivity.this;



        // Get the widget reference from xml layout
        mRootLayout = findViewById(R.id.root_layout);
        mTVStats = findViewById(R.id.tv_stats);
        mBtnFilterAll = findViewById(R.id.btn_all);
        mBtnFilterPriority = findViewById(R.id.btn_priority);
        mBtnFilterAlarms = findViewById(R.id.btn_alarms);
        mBtnFilterNone = findViewById(R.id.btn_none);


        /*
            NotificationManager
                Class to notify the user of events that happen. This is how you tell
                the user that something has happened in the background.
        */

        // Get the notification manager instance
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);


        // Total silence the device, turn off all notifications
        mBtnFilterNone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*
                    int INTERRUPTION_FILTER_NONE
                        Interruption filter constant - No interruptions filter - all notifications
                        are suppressed and all audio streams (except those used for phone calls)
                        and vibrations are muted.
                */
                changeInterruptionFiler(NotificationManager.INTERRUPTION_FILTER_NONE);
                mRootLayout.setBackgroundColor(Color.RED);
                mTVStats.setText("Now on do not disturb mode.");
            }
        });


        // Turn off do not disturb mode, allow all notifications
        mBtnFilterAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*
                    int INTERRUPTION_FILTER_ALL
                        Interruption filter constant - Normal interruption
                        filter - no notifications are suppressed.
                */
                changeInterruptionFiler(NotificationManager.INTERRUPTION_FILTER_ALL);
                mRootLayout.setBackgroundColor(Color.GREEN);
                mTVStats.setText("Now off do not disturb mode.");
            }
        });


        // Allow priority only notifications
        // Partially turn on do not disturb mode
        mBtnFilterPriority.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*
                    int INTERRUPTION_FILTER_PRIORITY
                        Interruption filter constant - Priority interruption filter - all notifications
                        are suppressed except those that match the priority criteria. Some audio
                        streams are muted.
                */
                changeInterruptionFiler(NotificationManager.INTERRUPTION_FILTER_PRIORITY);
                mRootLayout.setBackgroundColor(Color.YELLOW);
                mTVStats.setText("Now on do not disturb mode for priority only.");
            }
        });


        // Only allow alarms notification
        // Partially turn on do not disturb mode
        mBtnFilterAlarms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*
                    int INTERRUPTION_FILTER_ALARMS
                        Interruption filter constant - Alarms only interruption filter - all
                        notifications except those of category CATEGORY_ALARM are suppressed.
                        Some audio streams are muted.
                */
                changeInterruptionFiler(NotificationManager.INTERRUPTION_FILTER_ALARMS);
                mRootLayout.setBackgroundColor(Color.MAGENTA);
                mTVStats.setText("Now on do not disturb mode for alarms only.");
            }
        });


    }


    protected void changeInterruptionFiler(int interruptionFilter){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){ // If api level minimum 23
            /*
                boolean isNotificationPolicyAccessGranted ()
                    Checks the ability to read/modify notification policy for the calling package.
                    Returns true if the calling package can read/modify notification policy.
                    Request policy access by sending the user to the activity that matches the
                    system intent action ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS.

                    Use ACTION_NOTIFICATION_POLICY_ACCESS_GRANTED_CHANGED to listen for
                    user grant or denial of this access.

                Returns
                    boolean

            */
            // If notification policy access granted for this package
            if(mNotificationManager.isNotificationPolicyAccessGranted()){
                /*
                    void setInterruptionFilter (int interruptionFilter)
                        Sets the current notification interruption filter.

                        The interruption filter defines which notifications are allowed to interrupt
                        the user (e.g. via sound & vibration) and is applied globally.

                        Only available if policy access is granted to this package.

                    Parameters
                        interruptionFilter : int
                        Value is INTERRUPTION_FILTER_NONE, INTERRUPTION_FILTER_PRIORITY,
                        INTERRUPTION_FILTER_ALARMS, INTERRUPTION_FILTER_ALL
                        or INTERRUPTION_FILTER_UNKNOWN.
                */

                // Set the interruption filter
                mNotificationManager.setInterruptionFilter(interruptionFilter);
            }else {
                /*
                    String ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS
                        Activity Action : Show Do Not Disturb access settings.
                        Users can grant and deny access to Do Not Disturb configuration from here.

                    Input : Nothing.
                    Output : Nothing.
                    Constant Value : "android.settings.NOTIFICATION_POLICY_ACCESS_SETTINGS"
                */
                // If notification policy access not granted for this package
                Intent intent = new Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
                startActivity(intent);
            }
        }
    }


}
