package com.application.lumaque.bizlinked.fireBase;


import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.TaskStackBuilder;
import android.util.Log;

import com.application.lumaque.bizlinked.R;
import com.application.lumaque.bizlinked.activities.HomeActivity;
import com.application.lumaque.bizlinked.data_models.bizlinked.FireBaseDataMode;
import com.application.lumaque.bizlinked.helpers.preference.BasePreferenceHelper;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import static com.application.lumaque.bizlinked.BizLinkApplication.channel_1_ID;


public class BackgroundService extends Service {

    private NotificationManagerCompat notificationManagerCompat;
    BasePreferenceHelper basePreferenceHelper;
    String msgID;
    boolean isLogin;
    FirebaseDatabase firebaseDatabase;

    DatabaseReference reference;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Bundle extras = intent.getExtras();

        if (extras == null)
            Log.d("Service", "null");
        else {
            Log.d("Service", "not null");
            msgID = (String) extras.get("msgID");
            isLogin = (boolean) extras.get("islogin");


        }

       //   Toast.makeText(this,isLogin + "msgID = "+msgID,Toast.LENGTH_LONG).show();
        if (isLogin) {
            notificationListener();
            //    Toast.makeText(this, "notification created", Toast.LENGTH_SHORT).show();
        }
        return START_NOT_STICKY;
    }

    @Override
    public void onCreate() {
      //  Toast.makeText(this, "Service Started", Toast.LENGTH_SHORT).show();
        initialize();
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        msgID = "";
        isLogin = false;
        reference = null;
        super.onDestroy();
    }


    private void initialize() {

        FirebaseApp.initializeApp(this);
        notificationManagerCompat = NotificationManagerCompat.from(this);
        basePreferenceHelper = new BasePreferenceHelper(this);
        firebaseDatabase = FirebaseDatabase.getInstance();

    }

    private void notificationListener() {

        reference = firebaseDatabase.getReference().child("notifications").child(msgID);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap<String, Object> value = (HashMap<String, Object>) dataSnapshot.getValue();
                ArrayList<FireBaseDataMode> list = new ArrayList<FireBaseDataMode>();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    list.add(child.getValue(FireBaseDataMode.class));
                }
                if (list.size() != 0 && list.get(list.size()-1).getScreen().equalsIgnoreCase("view_profile")){
                    showNotification("Request", list.get(list.size()-1).getText(), (String) list.get(list.size()-1).getData().get("companyId").toString(),list.get(list.size()-1).getScreen());
                    dataSnapshot.getRef().removeValue();
                }else  if (list.size() != 0 && list.get(list.size()-1).getScreen().equalsIgnoreCase("VIEW_PRODUCT")){
                    showProductNotification("New Product", list.get(list.size()-1).getText(), (String) list.get(list.size()-1).getData().get("companyId").toString(),(String) list.get(list.size()-1).getData().get("productId").toString(),list.get(list.size()-1).getScreen());
                    dataSnapshot.getRef().removeValue();
                }


                //    isForeground("com.application.lumaque.bizlinked");

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }


    private void showNotification(String title, String message, String ID,String screen) {


        Intent viewProfileIntent = new Intent(this, HomeActivity.class);
        viewProfileIntent.putExtra("VIEWID", ID);
        viewProfileIntent.putExtra("SCREEN", screen);
        viewProfileIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);


        //  Intent launcherIntent = new Intent(this , HomeActivity.class);
        //  launcherIntent.putExtra("VIEWID", ID);
        // resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);


        PendingIntent resultPendingIntent = PendingIntent.getActivity(this,
                0 /* Request code */, viewProfileIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        //   addBackStack(this,resultIntent);


        Notification notification = new NotificationCompat.Builder(this, channel_1_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(1)
                .setAutoCancel(true)
                .setCategory(NotificationCompat.CATEGORY_SOCIAL)
                .setContentIntent(resultPendingIntent)
                .build();

        notificationManagerCompat.notify(Integer.parseInt(ID), notification);


    }



    private void showProductNotification(String title, String message, String CompanyID,String productID,String screen) {


        Intent viewProfileIntent = new Intent(this, HomeActivity.class);
        viewProfileIntent.putExtra("VIEWID", CompanyID);
        viewProfileIntent.putExtra("PRODUCT", productID);
        viewProfileIntent.putExtra("SCREEN", screen);
        viewProfileIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);


        //  Intent launcherIntent = new Intent(this , HomeActivity.class);
        //  launcherIntent.putExtra("VIEWID", ID);
        // resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);


        PendingIntent resultPendingIntent = PendingIntent.getActivity(this,
                0 /* Request code */, viewProfileIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        //   addBackStack(this,resultIntent);


        Notification notification = new NotificationCompat.Builder(this, channel_1_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(1)
                .setAutoCancel(true)
                .setCategory(NotificationCompat.CATEGORY_SOCIAL)
                .setContentIntent(resultPendingIntent)
                .build();

        notificationManagerCompat.notify(Integer.parseInt(CompanyID), notification);


    }

    public static PendingIntent addBackStack(final Context context, final Intent intent) {
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context.getApplicationContext());
        stackBuilder.addNextIntentWithParentStack(intent);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
    }

}


