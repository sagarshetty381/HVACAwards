package example.com.hvacawards;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class GiveRatings extends AppCompatActivity {

    Button rateit;
    SeekBar seekbar;
    TextView textView;
    float ratings = 0;
    double total = 0;
    String Total;

    String Name,pushId;
    DatabaseReference mDatabaseRate,mDatabaseUser,mDatabaseOne;
    String rateuid,Rateuid,Projuid,Rate;

    Adapter adapter;
    RecyclerView recyclerView;
    LinearLayoutManager Manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_give_ratings);

        Name = getIntent().getExtras().getString("Name");
        String Address = getIntent().getExtras().getString("Address");
        String Area = getIntent().getExtras().getString("Area");
        String City = getIntent().getExtras().getString("City");
        String State = getIntent().getExtras().getString("State");
        pushId = getIntent().getExtras().getString("pushid");
        Rate = getIntent().getExtras().getString("Rating");
        String HVAC = getIntent().getExtras().getString("HVAC");
        final String projuid = getIntent().getExtras().getString("Userid");
        String CapacityTR = getIntent().getExtras().getString("Capacity");
        ArrayList<String> newList = (ArrayList<String>) getIntent().getSerializableExtra("urls");

        this.recyclerView = findViewById(R.id.recyclerView);
        Manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(Manager);
        adapter = new Adapter(newList, this);
        recyclerView.setAdapter(adapter);

        String title = Name;
        TextView titlle = findViewById(R.id.rprojecttitle);
        titlle.setText(title);

        TextView hvac = findViewById(R.id.hvacfields);
        hvac.setText(HVAC);

        rateit = findViewById(R.id.giverating);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        rateuid = user.getUid();

        mDatabaseRate = FirebaseDatabase.getInstance().getReference().child("Projects");
        mDatabaseUser = FirebaseDatabase.getInstance().getReference().child("Users");
        mDatabaseOne = FirebaseDatabase.getInstance().getReference().child("Rateit").child(rateuid);

        String full = Address + "," + Area + "," + City + "," + State;
        TextView fullAddress = findViewById(R.id.rfulladdress);
        fullAddress.setText(full);

        final TextView capacity = findViewById(R.id.capacityrate);
        capacity.setText(CapacityTR);

        seekbar = findViewById(R.id.seekBar);
        textView = findViewById(R.id.textView);
        textView.setText(seekbar.getProgress() + "/" + seekbar.getMax());

        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressValue = 0;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressValue = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                textView.setText(progressValue+"");

            }
        });

        Button rateit = findViewById(R.id.giverating);
        rateit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabaseUser.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        UserProfile userProfile = dataSnapshot.child(rateuid).getValue(UserProfile.class);
                        Rateuid = userProfile.getReferencePromo();

                        UserProfile pro = dataSnapshot.child(projuid).getValue(UserProfile.class);
                        Projuid = pro.getPromoCode();

                        ratings = Integer.parseInt(textView.getText().toString()) + (Integer.parseInt(capacity.getText().toString()) / 10) +
                                Float.parseFloat(Rate.toString());

                        if (Projuid.equals(Rateuid)) {
                            total = ratings + (ratings*0.1);
                        }else{
                            total = ratings;
                        }
                        Total = String.valueOf(total);
                        mDatabaseOne.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.hasChild(pushId)){
                                    Toast.makeText(GiveRatings.this, "Sorry,you have already rated this project!", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(GiveRatings.this,Ratings.class));
                                    finish();
                                }else if(projuid.equals(rateuid)){
                                    Toast.makeText(GiveRatings.this, "Sorry,you cant rate your own projects.", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(GiveRatings.this,Ratings.class));
                                    finish();
                                }else{
                                    mDatabaseRate.child(pushId).child("Rating").setValue(Total).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            mDatabaseOne.child(pushId).setValue(total);
                                            Toast.makeText(GiveRatings.this, "Thank you for rating :)", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(GiveRatings.this,Ratings.class));
                                            finish();
                                        }
                                    });
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
        });
    }
}