package com.application.lumaque.bizlinked;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.graphics.Color;
import android.support.multidex.MultiDexApplication;

import com.google.firebase.FirebaseApp;

public class BizLinkApplication extends MultiDexApplication {

    public static final String channel_1_ID = "channel1";

    // public static String fireBaseReffID = "";

 /*   public static String getFireBaseReffID() {
        return fireBaseReffID;
    }

    public static void setFireBaseReffID(String fireBaseReffID) {
        BizLinkApplication.fireBaseReffID = fireBaseReffID;
    }

    public static boolean isLoginStatus() {
        return loginStatus;
    }

    public static void setLoginStatus(boolean loginStatus) {
        BizLinkApplication.loginStatus = loginStatus;
    }
*/
    //  public static boolean loginStatus;


    @Override
    public void onCreate() {
        super.onCreate();
        // MultiDex.install(this);
        FirebaseApp.initializeApp(this);
        createNotificationChannel();
        //Fabric.with(this, new Crashlytics());
    }


    private void createNotificationChannel() {
        int importance = NotificationManager.IMPORTANCE_HIGH;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channel_1_ID, "channel 1", importance);
            channel.setDescription("This is channel 1");
            channel.enableLights(true);
            channel.setLightColor(Color.RED);
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);


        }

    }


}
