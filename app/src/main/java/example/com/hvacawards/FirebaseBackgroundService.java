package example.com.hvacawards;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class FirebaseBackgroundService extends Service {
    private DatabaseReference db,mDatabase,test;
    String uid,result="";
    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();
        db = FirebaseDatabase.getInstance().getReference().child("Projects");
        db.addValueEventListener(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                test = FirebaseDatabase.getInstance().getReference().child("Accepted");
                test.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild(uid)){
                            AcceptModel acceptModel = dataSnapshot.child(uid).getValue(AcceptModel.class);
                            result = acceptModel.getStatus();
                            if (result.equals("true")) {
                                postNotif(dataSnapshot.getValue().toString());
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void postNotif(String notifString) {

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserProfile userProfile = dataSnapshot.getValue(UserProfile.class);
                String name = userProfile.getFullName();
                NotificationCompat.Builder builder =
                        new NotificationCompat.Builder(FirebaseBackgroundService.this)
                                .setSmallIcon(R.drawable.awards)
                                .setContentTitle("Hi " + name)
                                .setContentText("Check out some new projects uploaded")
                                .setAutoCancel(true);

                Intent notificationIntent = new Intent(FirebaseBackgroundService.this, Ratings.class);
                PendingIntent contentIntent = PendingIntent.getActivity(FirebaseBackgroundService.this, 0, notificationIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);
                builder.setContentIntent(contentIntent);

                NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                manager.notify(1, builder.build());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
