package com.application.lumaque.bizlinked.fireBase;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class StartFirebaseAtBoot extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
      //  context.startService(new Intent(context,BackgroundService.class));




        FirebaseApp.initializeApp(context);
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference reference = firebaseDatabase.getReference().child("notifications").child("comp_1");
        //DatabaseReference reference = firebaseDatabase.getReference("comp_1");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                HashMap<String,Object> value = (HashMap<String, Object>) dataSnapshot.getValue();


           //     createNotification("tittle", String.valueOf(value.size()));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




      //  activityReference.StartBackgroundService(BackgroundService.class);

    }
}